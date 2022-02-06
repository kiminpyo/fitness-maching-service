package com.kang.backup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.kang.backup.adapter.TrainerAdapter;
import com.kang.backup.fragment.TrainerDetailFragment;
import com.kang.backup.model.UserModel;

import java.util.HashMap;

public class TrainerInfoEditActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;

    private FirebaseUser uid;

    private Uri imageUri;
    private Button callView;
    private ImageView back_btn, profile;
    private TextView saveBtn, userId, userName, callNum, majorTxt, careerTxt, introduceView;
    private TextView certTxt_1, careerTxt_1,certTxt_2, careerTxt_2,certTxt_3, careerTxt_3;
    private ImageView major_rbtn_1, major_rbtn_2, major_rbtn_3, major_rbtn_4, major_rbtn_5, major_rbtn_6;
    private ImageView area_rbtn_1, area_rbtn_2, area_rbtn_3, area_rbtn_4, area_rbtn_5, area_rbtn_6;
    UserModel user;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_info_edit);

        // 파이어 베이스 현재 접속중인 유저 아이디 리턴
        uid = FirebaseAuth.getInstance().getCurrentUser();

        ProgressBar progressBar = findViewById(R.id.progressBar2);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(80);

        saveBtn = findViewById(R.id.save_btn);
        back_btn = findViewById(R.id.back_btn);

        profile = (ImageView)findViewById(R.id.profile_image);
        userId = findViewById(R.id.userId);

        userName = findViewById(R.id.userName);
        callNum = findViewById(R.id.callNum);
        majorTxt = findViewById(R.id.major_txt);
        careerTxt = findViewById(R.id.career_txt);
        careerTxt_1 = findViewById(R.id.career_txt_1);
        careerTxt_2 = findViewById(R.id.career_txt_2);
        careerTxt_3 = findViewById(R.id.career_txt_3);
        certTxt_1 = findViewById(R.id.cert_txt_1);
        certTxt_2 = findViewById(R.id.cert_txt_2);
        certTxt_3 = findViewById(R.id.cert_txt_3);
        introduceView = findViewById(R.id.introduce_view);

        // 전문분야
        major_rbtn_1 = (ImageView) findViewById(R.id.major_1);
        major_rbtn_2 = (ImageView) findViewById(R.id.major_2);
        major_rbtn_3 = (ImageView) findViewById(R.id.major_3);
        major_rbtn_4 = (ImageView) findViewById(R.id.major_4);
        major_rbtn_5 = (ImageView) findViewById(R.id.major_5);
        major_rbtn_6 = (ImageView) findViewById(R.id.major_6);

        // 활동지역
        area_rbtn_1 = (ImageView) findViewById(R.id.area_1);
        area_rbtn_2 = (ImageView) findViewById(R.id.area_2);
        area_rbtn_3 = (ImageView) findViewById(R.id.area_3);
        area_rbtn_4 = (ImageView) findViewById(R.id.area_4);
        area_rbtn_5 = (ImageView) findViewById(R.id.area_5);
        area_rbtn_6 = (ImageView) findViewById(R.id.area_6);

        // 전문분야 선택
        major_rbtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major_rbtn_1.setImageResource(R.drawable.ic_area_check_foreground);
                major_rbtn_2.setImageResource(R.drawable.ic_major_pill_foreground);
                major_rbtn_3.setImageResource(R.drawable.ic_major_fitness_foreground);
                major_rbtn_4.setImageResource(R.drawable.ic_major_fix_foreground);
                major_rbtn_5.setImageResource(R.drawable.ic_major_power_foreground);
                major_rbtn_6.setImageResource(R.drawable.ic_major_yoga_foreground);
                user.setMajor(major_rbtn_1.getTag().toString());
            }
        });

        major_rbtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major_rbtn_1.setImageResource(R.drawable.ic_major_diet_foreground);
                major_rbtn_2.setImageResource(R.drawable.ic_area_check_foreground);
                major_rbtn_3.setImageResource(R.drawable.ic_major_fitness_foreground);
                major_rbtn_4.setImageResource(R.drawable.ic_major_fix_foreground);
                major_rbtn_5.setImageResource(R.drawable.ic_major_power_foreground);
                major_rbtn_6.setImageResource(R.drawable.ic_major_yoga_foreground);
                user.setMajor(major_rbtn_2.getTag().toString());
            }
        });

        major_rbtn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major_rbtn_1.setImageResource(R.drawable.ic_major_diet_foreground);
                major_rbtn_2.setImageResource(R.drawable.ic_major_pill_foreground);
                major_rbtn_3.setImageResource(R.drawable.ic_area_check_foreground);
                major_rbtn_4.setImageResource(R.drawable.ic_major_fix_foreground);
                major_rbtn_5.setImageResource(R.drawable.ic_major_power_foreground);
                major_rbtn_6.setImageResource(R.drawable.ic_major_yoga_foreground);
                user.setMajor(major_rbtn_3.getTag().toString());
            }
        });

        major_rbtn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major_rbtn_1.setImageResource(R.drawable.ic_major_diet_foreground);
                major_rbtn_2.setImageResource(R.drawable.ic_major_pill_foreground);
                major_rbtn_3.setImageResource(R.drawable.ic_major_fitness_foreground);
                major_rbtn_4.setImageResource(R.drawable.ic_area_check_foreground);
                major_rbtn_5.setImageResource(R.drawable.ic_major_power_foreground);
                major_rbtn_6.setImageResource(R.drawable.ic_major_yoga_foreground);
                user.setMajor(major_rbtn_4.getTag().toString());
            }
        });

        major_rbtn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major_rbtn_1.setImageResource(R.drawable.ic_major_diet_foreground);
                major_rbtn_2.setImageResource(R.drawable.ic_major_pill_foreground);
                major_rbtn_3.setImageResource(R.drawable.ic_major_fitness_foreground);
                major_rbtn_4.setImageResource(R.drawable.ic_major_fix_foreground);
                major_rbtn_5.setImageResource(R.drawable.ic_area_check_foreground);
                major_rbtn_6.setImageResource(R.drawable.ic_major_yoga_foreground);
                user.setMajor(major_rbtn_5.getTag().toString());
            }
        });

        major_rbtn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major_rbtn_1.setImageResource(R.drawable.ic_major_diet_foreground);
                major_rbtn_2.setImageResource(R.drawable.ic_major_pill_foreground);
                major_rbtn_3.setImageResource(R.drawable.ic_major_fitness_foreground);
                major_rbtn_4.setImageResource(R.drawable.ic_major_fix_foreground);
                major_rbtn_5.setImageResource(R.drawable.ic_major_power_foreground);
                major_rbtn_6.setImageResource(R.drawable.ic_area_check_foreground);
                user.setMajor(major_rbtn_6.getTag().toString());
            }
        });


        // 활동지역 선택  (중 동 서 남 북 달 순으로 버튼
        area_rbtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area_rbtn_1.setImageResource(R.drawable.ic_area_check_foreground);
                area_rbtn_2.setImageResource(R.drawable.ic_area_deagu_donggu_foreground);
                area_rbtn_3.setImageResource(R.drawable.ic_area_deagu_seogu_foreground);
                area_rbtn_4.setImageResource(R.drawable.ic_area_deagu_namgu_foreground);
                area_rbtn_5.setImageResource(R.drawable.ic_area_deagu_buckgu_foreground);
                area_rbtn_6.setImageResource(R.drawable.ic_area_deagu_dalseo_foreground);
                user.setArea(area_rbtn_1.getTag().toString());
            }
        });

        area_rbtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area_rbtn_1.setImageResource(R.drawable.ic_area_deagu_junggu_foreground);
                area_rbtn_2.setImageResource(R.drawable.ic_area_check_foreground);
                area_rbtn_3.setImageResource(R.drawable.ic_area_deagu_seogu_foreground);
                area_rbtn_4.setImageResource(R.drawable.ic_area_deagu_namgu_foreground);
                area_rbtn_5.setImageResource(R.drawable.ic_area_deagu_buckgu_foreground);
                area_rbtn_6.setImageResource(R.drawable.ic_area_deagu_dalseo_foreground);
                user.setArea(area_rbtn_2.getTag().toString());
            }
        });

        area_rbtn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area_rbtn_1.setImageResource(R.drawable.ic_area_deagu_junggu_foreground);
                area_rbtn_2.setImageResource(R.drawable.ic_area_deagu_donggu_foreground);
                area_rbtn_3.setImageResource(R.drawable.ic_area_check_foreground);
                area_rbtn_4.setImageResource(R.drawable.ic_area_deagu_namgu_foreground);
                area_rbtn_5.setImageResource(R.drawable.ic_area_deagu_buckgu_foreground);
                area_rbtn_6.setImageResource(R.drawable.ic_area_deagu_dalseo_foreground);
                user.setArea(area_rbtn_3.getTag().toString());
            }
        });

        area_rbtn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area_rbtn_1.setImageResource(R.drawable.ic_area_deagu_junggu_foreground);
                area_rbtn_2.setImageResource(R.drawable.ic_area_deagu_donggu_foreground);
                area_rbtn_3.setImageResource(R.drawable.ic_area_deagu_seogu_foreground);
                area_rbtn_4.setImageResource(R.drawable.ic_area_check_foreground);
                area_rbtn_5.setImageResource(R.drawable.ic_area_deagu_buckgu_foreground);
                area_rbtn_6.setImageResource(R.drawable.ic_area_deagu_dalseo_foreground);
                user.setArea(area_rbtn_4.getTag().toString());
            }
        });

        area_rbtn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area_rbtn_1.setImageResource(R.drawable.ic_area_deagu_junggu_foreground);
                area_rbtn_2.setImageResource(R.drawable.ic_area_deagu_donggu_foreground);
                area_rbtn_3.setImageResource(R.drawable.ic_area_deagu_seogu_foreground);
                area_rbtn_4.setImageResource(R.drawable.ic_area_deagu_namgu_foreground);
                area_rbtn_5.setImageResource(R.drawable.ic_area_check_foreground);
                area_rbtn_6.setImageResource(R.drawable.ic_area_deagu_dalseo_foreground);
                user.setArea(area_rbtn_5.getTag().toString());
            }
        });

        area_rbtn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area_rbtn_1.setImageResource(R.drawable.ic_area_deagu_junggu_foreground);
                area_rbtn_2.setImageResource(R.drawable.ic_area_deagu_donggu_foreground);
                area_rbtn_3.setImageResource(R.drawable.ic_area_deagu_seogu_foreground);
                area_rbtn_4.setImageResource(R.drawable.ic_area_deagu_namgu_foreground);
                area_rbtn_5.setImageResource(R.drawable.ic_area_deagu_buckgu_foreground);
                area_rbtn_6.setImageResource(R.drawable.ic_area_check_foreground);
                user.setArea(area_rbtn_6.getTag().toString());
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserModel.class);

                userId.setText(user.getId());

                // 대표 경력, 이력(#)
                majorTxt.setText("#"+user.getMajor());
                careerTxt.setText("#"+user.getCareer_1());

                userName.setText(user.getUsername());
                callNum.setText((user.getCallnum()));

                switch (user.getMajor()) {
                    case "다이어트":
                        major_rbtn_1.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "필라테스":
                        major_rbtn_2.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "피트니스":
                        major_rbtn_3.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "체형교정":
                        major_rbtn_4.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "파워리프팅":
                        major_rbtn_5.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "요가":
                        major_rbtn_6.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                }

                switch (user.getArea()) {
                    case "중구":
                        area_rbtn_1.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "동구":
                        area_rbtn_2.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "서구":
                        area_rbtn_3.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "남구":
                        area_rbtn_4.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "북구":
                        area_rbtn_5.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                    case "달서구":
                        area_rbtn_6.setImageResource(R.drawable.ic_area_check_foreground);
                        break;
                }

                // 이력, 자격증 리스트
                careerTxt_1.setText(user.getCareer_1());
                if(!user.getCareer_2().equals(""))
                    careerTxt_2.setText(user.getCareer_2());
                if(!user.getCareer_3().equals(""))
                    careerTxt_3.setText(user.getCareer_3());

                certTxt_1.setText(user.getCert_1());
                certTxt_2.setText(user.getCert_2());
                certTxt_3.setText(user.getCert_3());

                introduceView.setText(user.getIntroduce());
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("트레이너화면 홈 돌아가기");
                Intent intent;
                intent = new Intent(TrainerInfoEditActivity.this, TrainerMainActivity.class);
                intent.putExtra("select", 1);
                startActivity(intent);
            }
        });

        //



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                user.setId(userId.getText().toString());
                user.setUsername(userName.getText().toString());
                user.setCallnum(callNum.getText().toString());

                // 이력, 자격증 리스트
                user.setCareer_1(careerTxt_1.getText().toString());
                user.setCareer_2(careerTxt_2.getText().toString());
                user.setCareer_3(careerTxt_3.getText().toString());

                user.setCert_1(certTxt_1.getText().toString());
                user.setCert_2(certTxt_2.getText().toString());
                user.setCert_3(certTxt_3.getText().toString());

                user.setIntroduce(introduceView.getText().toString());

                dialog = new ProgressDialog(TrainerInfoEditActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("대표 프로필을 수정하는 중입니다.");

                dialog.show();

                updateImg();

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

                            updateProfile(user);

                        }
                    });
                }
            });
        } else {
            updateProfile(user);
        }
    }

    private void updateProfile(UserModel user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid.getUid());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id", user.getId());
        hashMap.put("username", user.getUsername());
        hashMap.put("callnum", user.getCallnum());
        hashMap.put("gender", user.getGender());
        hashMap.put("trainer", user.isTrainer());
        hashMap.put("major", user.getMajor());
        hashMap.put("area", user.getArea());
        hashMap.put("career_1", user.getCareer_1());
        hashMap.put("career_2", user.getCareer_2());
        hashMap.put("career_3", user.getCareer_3());
        hashMap.put("cert_1", user.getCert_1());
        hashMap.put("cert_2", user.getCert_2());
        hashMap.put("cert_3", user.getCert_3());
        hashMap.put("introduce", user.getIntroduce());
        hashMap.put("imageurl", user.getImageurl());

        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    System.out.println("데이터 베이스 수정 성공");
                    Intent intent;
                    intent = new Intent(TrainerInfoEditActivity.this, TrainerMainActivity.class);
                    intent.putExtra("select", 1);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            profile.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData();// 이미지 경로 원본
        }
    }
}