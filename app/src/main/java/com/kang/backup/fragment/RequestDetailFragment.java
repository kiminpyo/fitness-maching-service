package com.kang.backup.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.kang.backup.model.RequestModel;
import com.kang.backup.model.UserModel;

import java.util.HashMap;

public class RequestDetailFragment extends Fragment {

    private String uid;
    private boolean isTrainer;
    private UserModel user;
    private String request_publisher;
    private String request_cnt;
    RequestModel request;
    ImageView back_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        request_publisher = prefs.getString("request_publisher", "none");
        request_cnt = prefs.getString("request_cnt", "none");
        uid = prefs.getString("uid", "none");
        isTrainer = prefs.getBoolean("isTrainer", true);

        View view = inflater.inflate(R.layout.fragment_request_detail, container, false);
        ImageView profile_image;
        TextView user_name, user_id, major, area, calendar_view, stime_view, etime_view, request_view, state_view;
        Button accept_btn, cancel_btn;
        LinearLayout btn_layout;

        profile_image = view.findViewById(R.id.profile_image);
        user_name = view.findViewById(R.id.user_name);
        user_id = view.findViewById(R.id.user_id);
        major = view.findViewById(R.id.major);
        area = view.findViewById(R.id.area);
        calendar_view = view.findViewById(R.id.calendar_view);
        stime_view = view.findViewById(R.id.stime_view);
        etime_view = view.findViewById(R.id.etime_view);
        request_view = view.findViewById(R.id.request_view);
        state_view = view.findViewById(R.id.state_view);
        btn_layout = view.findViewById(R.id.btn_layout);

        back_btn = view.findViewById(R.id.back_btn);
        accept_btn = view.findViewById(R.id.accept_btn);
        cancel_btn = view.findViewById(R.id.cancel_btn);

        // 로그인 회원이 유저라면 요청 퍼블리셔 -> 트레이너
        // 로그인 회원이 트레이너라면 요청 퍼블리셔 -> 유저
        // 이기 때문에 퍼블리셔 정보만 가져오면 됨
        // 화면에 보여줄 퍼블리셔 정보 가져오기
        DatabaseReference user_reference= FirebaseDatabase.getInstance().getReference("Users").child(request_publisher);
        user_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(UserModel.class);
                Glide.with(getContext()).load(user.getImageurl()).into(profile_image);
                user_name.setText(user.getUsername());
                user_id.setText(user.getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference;
        if(isTrainer) {
            // 요청서를 보는 사람(나)이 트레이너 라면
            // 받은 요청 조회
            // receive request(ReceiveRequest->트레이너id->유저id->번호) 저장
            reference = FirebaseDatabase.getInstance().getReference("ReceiveRequest");

        } else {
            // 요청서를 보는 사람(나)이 일반 유저 라면
            // 보낸 요청 조회
            // send request(SendRequest->유저id->트레이너id->번호) 저장
            reference = FirebaseDatabase.getInstance().getReference("SendRequest");
            btn_layout.setVisibility(View.GONE);
        }
        System.out.println(reference);
        reference.child(uid).child(request_publisher).child(request_cnt).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 요청 정보 담을 모델 객체 생성
                request = snapshot.getValue(RequestModel.class);

                System.out.println("request_publisher : " + request_publisher);
                System.out.println("uid : " + uid);
                System.out.println("request_cnt : " + request_cnt);
                major.setText(request.getMajor());
                area.setText(request.getArea());
                calendar_view.setText(request.getRequestData());
                stime_view.setText(request.getRequestStime());
                etime_view.setText(request.getRequestEtime());
                request_view.setText(request.getRequestTxt());
                System.out.println(request.getRequestTxt());
                System.out.println(request.getState());
                if(request.getState().equals("0")) {
                    state_view.setText("대기중");
                } else if(request.getState().equals("1")) {
                    btn_layout.setVisibility(View.GONE);
                    state_view.setText("수락");
                } else if(request.getState().equals("2")) {
                    btn_layout.setVisibility(View.GONE);
                    state_view.setText("거절");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 글 작성자의 프로필 프레그먼트로 화면 전환
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,
                        new RequestListFragment()).commit();
            }
        });

        // 수락
        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("state", "1");
                hashMap.put("memoKey", uid+"_"+request_publisher+"_"+request_cnt);
                FirebaseDatabase.getInstance().getReference("ReceiveRequest").child(uid).child(request_publisher).child(request_cnt).updateChildren(hashMap);
                FirebaseDatabase.getInstance().getReference("SendRequest").child(request_publisher).child(uid).child(request_cnt).updateChildren(hashMap);
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,
                        new RequestListFragment()).commit();
            }
        });

        // 거절
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("state", "2");
                FirebaseDatabase.getInstance().getReference("ReceiveRequest").child(uid).child(request_publisher).child(request_cnt).updateChildren(hashMap);
                FirebaseDatabase.getInstance().getReference("SendRequest").child(request_publisher).child(uid).child(request_cnt).updateChildren(hashMap);
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,
                        new RequestListFragment()).commit();
            }
        });

        return view;
    }
}