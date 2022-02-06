package com.kang.backup.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.R;
import com.kang.backup.adapter.ManagementDetailAdapter;
import com.kang.backup.adapter.ManagementListAdapter;
import com.kang.backup.model.RequestModel;
import com.kang.backup.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ManagementDetailFragment extends Fragment {

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private List<RequestModel> requestLists = new ArrayList<>();

    private ManagementDetailAdapter managementdetailAdapter;

    private LinearLayout cal_btn, message_btn;

    private String request_publisher, cnt, uid;

    private ImageView profile_image, back_btn;

    private TextView user_name, gender, user_id, call, cnt_view;

    boolean isTrainer;

    UserModel user;

    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_management_detail, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        uid = prefs.getString("uid", "none");

        isTrainer = prefs.getBoolean("isTrainer", true);
        
        // 접속 회원이 유저라면 퍼블리셔 - 트레이너
        // 접속 회원이 트레이너라면 퍼블리셔 - 유저
        request_publisher= prefs.getString("request_publisher", "none");

        cnt= prefs.getString("cnt", "none");

        profile_image = view.findViewById(R.id.profile_image);

        user_name = view.findViewById(R.id.user_name);
        user_id = view.findViewById(R.id.user_id);
        gender = view.findViewById(R.id.gender);
        call = view.findViewById(R.id.call);
        cnt_view = view.findViewById(R.id.cnt_view);
        back_btn = view.findViewById(R.id.back_btn);
        cal_btn = view.findViewById(R.id.cal_btn);
        message_btn = view.findViewById(R.id.message_btn);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(request_publisher);

        cnt_view.setText(cnt);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(UserModel.class);

                Glide.with(getContext()).load(user.getImageurl()).into(profile_image);
                if(user.isTrainer())
                    user_name.setText(user.getUsername()+"트레이너님");
                else
                    user_name.setText(user.getUsername()+"회원님");
                user_id.setText(user.getId());
                gender.setText(user.getGender());
                call.setText(user.getCallnum());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 걸기
        cal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.getCallnum();
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+user.getCallnum()));
                startActivity(intent);
            }
        });
        // 문자보내기
        message_btn.setOnClickListener(new View.OnClickListener() {
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
                // 글 작성자의 프로필 프레그먼트로 화면 전환
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,
                        new ManagementListFragment()).commit();
            }
        });

        getRequestList();

        return view;
    }

    private void getRequestList() {

        if(isTrainer) {
            // 보는사람이 트레이너라면 receiveRequest에 접근
            reference = FirebaseDatabase.getInstance().getReference("ReceiveRequest").child(uid).child(request_publisher);
            System.out.println("트레이너 접근");
        } else {
            // 보는사람이 트레이너라면 sendRequest에 접근
            reference = FirebaseDatabase.getInstance().getReference("SendRequest").child(uid).child(request_publisher);
            System.out.println("유저 접근");
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    RequestModel request = snapshot.getValue(RequestModel.class);
                    // 수락(1) 상태면 리스트에 담는다.
                    if(request.getState().equals("1")) {
                        requestLists.add(request);
                    }
                }

                if(getContext() == null) {
                    return;
                } else {
                    System.out.println(requestLists.size());
                    managementdetailAdapter = new ManagementDetailAdapter(getContext(), requestLists);

                    recyclerView.setAdapter(managementdetailAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}