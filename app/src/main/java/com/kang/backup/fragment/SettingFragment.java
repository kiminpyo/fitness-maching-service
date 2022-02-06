package com.kang.backup.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.MypageActivity;
import com.kang.backup.R;
import com.kang.backup.SplashActivity;
import com.kang.backup.model.UserModel;

public class SettingFragment extends Fragment {

    LinearLayout mypage_btn;
    ImageView profile_image;
    TextView username, userid, logout_btn;

    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", "none");

        mypage_btn = view.findViewById(R.id.mypage_btn);
        profile_image = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.user_name);
        userid = view.findViewById(R.id.user_id);
        logout_btn = view.findViewById(R.id.logout_btn);

        FirebaseDatabase.getInstance().getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel user = snapshot.getValue(UserModel.class);

                Glide.with(getActivity()).load(user.getImageurl()).into(profile_image);
                userid.setText(user.getId());

                if(user.isTrainer())
                    username.setText(user.getUsername() + " 트레이너님");
                else
                    username.setText(user.getUsername() + " 회원님");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mypage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MypageActivity.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SplashActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        return view;
    }
}