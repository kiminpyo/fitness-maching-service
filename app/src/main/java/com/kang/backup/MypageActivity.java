package com.kang.backup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.kang.backup.fragment.SettingFragment;
import com.kang.backup.fragment.TrainerDetailFragment;
import com.kang.backup.model.UserModel;

import java.util.HashMap;

public class MypageActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;

    private FirebaseUser uid;

    private Uri imageUri;

    ImageView back_btn, profile_image;
    TextView  save_btn, user_name, user_id, user_pwd, user_callnum, logout_btn;

    LinearLayout callnum_view;

    UserModel user;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // 파이어 베이스 현재 접속중인 유저 아이디 리턴
        uid = FirebaseAuth.getInstance().getCurrentUser();

        ProgressBar progressBar = findViewById(R.id.progressBar3);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(80);


        profile_image = findViewById(R.id.profile_image);
        back_btn = findViewById(R.id.back_btn);
        save_btn = findViewById(R.id.save_btn);

        user_name = findViewById(R.id.user_name);
        user_id = findViewById(R.id.user_id);
        user_pwd = findViewById(R.id.user_pwd);
        user_callnum = findViewById(R.id.user_callnum);
        callnum_view = findViewById(R.id.callnum_view);

        logout_btn = findViewById(R.id.logout_btn);

        FirebaseDatabase.getInstance().getReference("Users").child(uid.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(UserModel.class);

                Glide.with(getApplicationContext()).load(user.getImageurl()).into(profile_image);
                user_name.setText(user.getUsername());
                user_id.setText(user.getId());
                user_pwd.setText(user.getPwd());
                user_callnum.setText(user.getCallnum());
                callnum_view.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.setId(user_id.getText().toString());
                user.setUsername(user_name.getText().toString());
                user.setPwd(user_pwd.getText().toString());
                user.setCallnum(user_callnum.getText().toString());

                dialog = new ProgressDialog(MypageActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("정보를 수정 중입니다.");

                dialog.show();

                updateImg();

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("뒤로가기");
                Intent intent;
                if(user.isTrainer())
                    intent = new Intent(MypageActivity.this, TrainerMainActivity.class);
                else
                    intent = new Intent(MypageActivity.this, UserMainActivity.class);
                intent.putExtra("select", 4);
                startActivity(intent);
            }
        });

    }

    private void updateImg() {

        if(imageUri != null) {
            FirebaseStorage.getInstance().getReference().child("userImages").child(uid.getUid()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            System.out.println("이미지 저장 성공");
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid.getUid());

                            @SuppressWarnings("VisibleForTests")
                            String imageUrl = uri.toString();

                            user.setImageurl(imageUrl);

                            updateProfile();

                        }
                    });
                }
            });
        } else {
            updateProfile();
        }
    }

    private void updateProfile() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid.getUid());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id", user.getId());
        hashMap.put("pwd", user.getPwd());
        hashMap.put("username", user.getUsername());
        hashMap.put("callnum", user.getCallnum());
        hashMap.put("imageurl", user.getImageurl());
        System.out.println(user.getImageurl());

        FirebaseAuth.getInstance().getCurrentUser().updateEmail(user.getId());
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(user.getPwd());

        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    System.out.println("데이터 베이스 수정 성공");
                    Intent intent;
                    if(user.isTrainer())
                        intent = new Intent(MypageActivity.this, TrainerMainActivity.class);
                    else
                        intent = new Intent(MypageActivity.this, UserMainActivity.class);
                    intent.putExtra("select", 4);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            profile_image.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData();// 이미지 경로 원본
        }
    }
}