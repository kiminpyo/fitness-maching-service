package com.kang.backup.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.R;
import com.kang.backup.TrainerInfoEditActivity;
import com.kang.backup.UserMainActivity;
import com.kang.backup.adapter.TrainerAdapter;
import com.kang.backup.adapter.TrainerListAdapter;
import com.kang.backup.dialog.RequestDialog;
import com.kang.backup.dialog.TrainerSearchDialog;
import com.kang.backup.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class TrainerDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private TrainerAdapter trainerAdapter;

    private ImageView back_btn, profileImage;
    private TextView edit_btn, userId, userName, majorTxt, careerTxt, majorView, areaView, introduceView, request_btn;
    private LinearLayout btn_layout, callview, textview;

    private List<String> careerLists = new ArrayList<>();
    private List<String> certLists = new ArrayList<>();

    DatabaseReference reference;

    UserModel user;

    private String uid;
    String publisher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainer_detail, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        publisher = prefs.getString("publisher", "none");
        uid = prefs.getString("uid", "none");
        back_btn = view.findViewById(R.id.back_btn);
        edit_btn = view.findViewById(R.id.edit_btn);

        profileImage = view.findViewById(R.id.profile_image);
        userId = view.findViewById(R.id.userId);
        userName = view.findViewById(R.id.userName);
        majorTxt = view.findViewById(R.id.major_txt);
        careerTxt = view.findViewById(R.id.career_txt);
        majorView = view.findViewById(R.id.major_view);
        areaView = view.findViewById(R.id.area_view);
        introduceView = view.findViewById(R.id.introduce_view);
        request_btn = view.findViewById(R.id.request_btn);
        btn_layout = view.findViewById(R.id.btn_layout);

        callview=   view.findViewById(R.id.cal_btn);
        textview= view.findViewById(R.id.message_btn);

        // 경력 사항 리사이클 뷰
        recyclerView = view.findViewById(R.id.career_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // 자격 사항 리사이클 뷰
        recyclerView1 = view.findViewById(R.id.cert_view);
        recyclerView1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(linearLayoutManager1);

        // 현재 접속중인 아이디와, 프로필 화면의 아이디가 일치
        if(publisher.equals(uid)) {
            userInfo(uid);
            back_btn.setVisibility(View.GONE);
            btn_layout.setVisibility(View.GONE);
        } else {
            userInfo(publisher);
            edit_btn.setVisibility(View.GONE);
        }

        //전화걸기
        callview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.getCallnum();
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+user.getCallnum()));
                startActivity(intent);
            }
        });

        //문자보내기
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.getCallnum();
                Intent intent= new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+user.getCallnum()));
                startActivity(intent);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getContext(), UserMainActivity.class);
                intent.putExtra("select", 1);
                startActivity(intent);
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to Edit Profile
                startActivity(new Intent(getContext(), TrainerInfoEditActivity.class));
            }

        });

        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                RequestDialog customDialog = new RequestDialog(getContext(), publisher);
                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction();
            }
        });

        return view;
    }

    private void userInfo(String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getContext() == null) {
                    return;
                }

                user = dataSnapshot.getValue(UserModel.class);

                Glide.with(getContext()).load(user.getImageurl()).into(profileImage);

                System.out.println("getImageUrl : " + user.getImageurl());

                // 트레이너 이름, 이메일
                userId.setText(user.getId());
                userName.setText(user.getUsername()+" 트레이너님");

                // 대표 경력, 이력(#)
                majorTxt.setText("#"+user.getMajor());
                careerTxt.setText("#"+user.getCareer_1());

                // 트레이너 경력
                majorView.setText(user.getMajor());
                areaView.setText(user.getArea());

                // 이력, 자격증 리스트
                careerLists.add(user.getCareer_1());
                if(!user.getCareer_2().equals(""))
                    careerLists.add(user.getCareer_2());
                if(!user.getCareer_3().equals(""))
                    careerLists.add(user.getCareer_3());
                trainerAdapter = new TrainerAdapter(getContext(), careerLists);
                recyclerView.setAdapter(trainerAdapter);
// 전화



                certLists.add(user.getCert_1());
                if(!user.getCert_2().equals(""))
                    certLists.add(user.getCert_2());
                if(!user.getCert_3().equals(""))
                    certLists.add(user.getCert_3());
                trainerAdapter = new TrainerAdapter(getContext(), certLists);
                recyclerView1.setAdapter(trainerAdapter);

                introduceView.setText(user.getIntroduce());

                if(user.isTrainer()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}