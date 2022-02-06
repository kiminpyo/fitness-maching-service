package com.kang.backup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.R;
import com.kang.backup.adapter.TrainerListAdapter;
import com.kang.backup.dialog.TrainerSearchDialog;
import com.kang.backup.model.UserModel;

import java.util.ArrayList;
import java.util.List;


public class TrainerListFragment extends Fragment {

    RecyclerView recyclerView;
    TrainerListAdapter trainerListAdapter;
    Button searchBtn;
    String gender="", area="", major="";


    List<UserModel> userLists = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trainer_list, container, false);
        searchBtn = view.findViewById(R.id.search_btn);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        readTrainerList(recyclerView, gender, area, major);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                TrainerSearchDialog customDialog = new TrainerSearchDialog(getContext(), new TrainerSearchDialog.TrainerSearchDialogListener() {
                    @Override
                    public void clickBtn(String sGender, String sArea, String sMajor) {
                        gender = sGender;
                        area = sArea;
                        major = sMajor;

                        readTrainerList(recyclerView, gender, area, major);

                        trainerListAdapter = new TrainerListAdapter(getContext(),userLists);
                        recyclerView.setAdapter(trainerListAdapter);

                    }
                });
                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction();
            }
        });

        return  view;
    }

    public void readTrainerList(RecyclerView recyclerView, String sGender, String sArea, String sMajor) {

        // 참조할 주소 설정 (파이어 베이스의 Posts및의 문서들 참조)
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        // 포스트아래의 문서들의 정보를 Model의 Post.class를 이용해 각각의 객체로 저장
        // Post.class -> postid, postimage, publisher, description으로 구성 파이어 베이스와 똑같아야함

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userLists.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    boolean flag = true;
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    if(user.isTrainer()) {
                        if(!sMajor.equals("")) {
                            if(!user.getMajor().equals(sMajor))
                                flag = false;
                        }
                        if(!sGender.equals("")) {
                            if(!user.getGender().equals(sGender))
                                flag = false;
                        }
                        if(!sArea.equals("")) {
                            if(!user.getArea().equals(sArea))
                                flag = false;
                        }
                    } else {
                        flag = false;
                    }
                    if(flag)
                        userLists.add(user);
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                trainerListAdapter = new TrainerListAdapter(getContext(),userLists);
                recyclerView.setAdapter(trainerListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}