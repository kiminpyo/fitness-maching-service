package com.kang.backup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.kang.backup.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    private EditText email, name, password, callnum;
    private EditText career_1, career_2, career_3, cert_1, cert_2, cert_3;
    private EditText introduce_txt;
    private String splash_background;
    private ImageView profile;
    private Uri imageUri;
    RadioButton rbtn_1, rbtn_2;

    ProgressDialog dialog;

    FirebaseAuth auth;
    DatabaseReference reference;
    UserModel userModel = new UserModel();

    private Animation tranlateToLeftAnim, tranlateFromLeftAnim;

    private LinearLayout sign_view, major_view, area_view, career_view, cert_view, introduce_view;

    private Button btn_sign, btn_major, btn_area, btn_career, btn_cert, btn_introduce;

    private ImageView major_rbtn_1, major_rbtn_2, major_rbtn_3, major_rbtn_4, major_rbtn_5, major_rbtn_6;

    private ImageView area_rbtn_1, area_rbtn_2, area_rbtn_3, area_rbtn_4, area_rbtn_5, area_rbtn_6;

    private CheckBox trainer;

    List<String> uList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //anim 폴더의 애니메이션을 가져와서 준비
        tranlateToLeftAnim = AnimationUtils.loadAnimation(this,R.anim.toleft);
        tranlateFromLeftAnim = AnimationUtils.loadAnimation(this,R.anim.fromleft);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(80);

        //페이지 슬라이딩 이벤트가 발생했을때 애니메이션이 시작 됐는지 종료 됐는지 감지할 수 있다.
        SignupActivity.SlidingPageAnimationListener animListener = new SignupActivity.SlidingPageAnimationListener();

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        splash_background = mFirebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        profile = (ImageView)findViewById(R.id.profile_image);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });



        // 트레이너 유무
        trainer = (CheckBox) findViewById(R.id.trainer);

        // 기본정보
        email = (EditText) findViewById(R.id.signupActivity_edittext_email);
        password = (EditText) findViewById(R.id.signupActivity_edittext_password);
        name = (EditText) findViewById(R.id.signupActivity_edittext_name);
        callnum = (EditText) findViewById(R.id.signupActivity_edittext_call);

        // 성별
        rbtn_1 = (RadioButton) findViewById(R.id.man);
        rbtn_2 = (RadioButton) findViewById(R.id.woman);
        
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

        // 경력
        career_1 = (EditText) findViewById(R.id.career_1);
        career_2 = (EditText) findViewById(R.id.career_2);
        career_3 = (EditText) findViewById(R.id.career_3);

        // 자격증
        cert_1 = (EditText) findViewById(R.id.cert_1);
        cert_2 = (EditText) findViewById(R.id.cert_2);
        cert_3 = (EditText) findViewById(R.id.cert_3);

        // 트레이너 소개
        introduce_txt = (EditText) findViewById(R.id.introduce_txt);

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
                userModel.setMajor(major_rbtn_1.getTag().toString());
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
                userModel.setMajor(major_rbtn_2.getTag().toString());
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
                userModel.setMajor(major_rbtn_3.getTag().toString());
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
                userModel.setMajor(major_rbtn_4.getTag().toString());
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
                userModel.setMajor(major_rbtn_5.getTag().toString());
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
                userModel.setMajor(major_rbtn_6.getTag().toString());
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
                userModel.setArea(area_rbtn_1.getTag().toString());
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
                userModel.setArea(area_rbtn_2.getTag().toString());
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
                userModel.setArea(area_rbtn_3.getTag().toString());
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
                userModel.setArea(area_rbtn_4.getTag().toString());
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
                userModel.setArea(area_rbtn_5.getTag().toString());
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
                userModel.setArea(area_rbtn_6.getTag().toString());
            }
        });
        btn_sign = (Button) findViewById(R.id.btn_sign);
        btn_major = (Button) findViewById(R.id.btn_major);
        btn_area = (Button) findViewById(R.id.btn_area);
        btn_career = (Button) findViewById(R.id.btn_career);
        btn_cert = (Button) findViewById(R.id.btn_cert);
        btn_introduce = (Button) findViewById(R.id.btn_introduce);

        sign_view = findViewById(R.id.sign_view);
        major_view = findViewById(R.id.major_view);
        area_view = findViewById(R.id.area_view);
        career_view = findViewById(R.id.career_view);
        cert_view = findViewById(R.id.cert_view);
        introduce_view = findViewById(R.id.introduce_view);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn : snapshot.getChildren()) {
                    UserModel user = sn.getValue(UserModel.class);
                    uList.add(user.getId());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.setId(email.getText().toString());
                userModel.setUsername(name.getText().toString());
                userModel.setCallnum(callnum.getText().toString());
                userModel.setPwd(password.getText().toString());

                // 성별 체크
                if(rbtn_1.isChecked()){
                    userModel.setGender(rbtn_1.getText().toString());
                } else {
                    userModel.setGender(rbtn_2.getText().toString());
                }

                if (email.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if(name.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if(password.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if(callnum.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "전화번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean flag = true;
                    for(String u : uList) {
                        if(email.getText().toString().equals(u)) {
                            flag = false;
                        }
                    }
                    if(flag){
                        if(trainer.isChecked()) {
                            // 화면이동 (일반 회원 가입 -> 전문가 회원 가입(전문분야 선택))
                            userModel.setTrainer(true);
                            sign_view.setVisibility(View.INVISIBLE);
                            sign_view.startAnimation(tranlateFromLeftAnim);
                            major_view.setVisibility(View.VISIBLE);
                            major_view.startAnimation(tranlateToLeftAnim);
                        } else {
                            System.out.println("일반 회원가입");
                            dialog = new ProgressDialog(SignupActivity.this);
                            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            dialog.setMessage("회원가입을 진행하는 중입니다.");

                            dialog.show();
                            register();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "중복된 회원 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_major.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 유저 모델에 전공 데이터 삽입
                if (userModel.getMajor().equals("")) {
                    Toast.makeText(SignupActivity.this, "전문분야를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println(userModel.getMajor());

                // 화면이동 (전문분야선택 -> 지역선택)
                major_view.setVisibility(View.INVISIBLE);
                major_view.startAnimation(tranlateFromLeftAnim);
                area_view.setVisibility(View.VISIBLE);
                area_view.startAnimation(tranlateToLeftAnim);
            }
        });

        btn_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 유저 모델에 지역 데이터 삽입
                if (userModel.getArea().equals("")) {
                    Toast.makeText(SignupActivity.this, "활동지역을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println(userModel.getArea());

                // 화면이동 (지역화면 -> 경력화면)
                area_view.setVisibility(View.INVISIBLE);
                area_view.startAnimation(tranlateFromLeftAnim);
                career_view.setVisibility(View.VISIBLE);
                career_view.startAnimation(tranlateToLeftAnim);
            }
        });

        btn_career.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 유저 모델에 경력 데이터 삽입(3개)
                if (career_1.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "대표경력 한가지를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    userModel.setCareer_1(career_1.getText().toString());
                }

                if (!career_2.getText().toString().equals("")) {
                    userModel.setCareer_2(career_2.getText().toString());
                }

                if (!career_3.getText().toString().equals("")) {
                    userModel.setCareer_3(career_3.getText().toString());
                }
                System.out.println("Career_1: "+userModel.getCareer_1());
                System.out.println("Career_2: "+userModel.getCareer_2());   //공백("")이 삽입됨
                System.out.println("Career_3: "+userModel.getCareer_3());

                // 화면이동 (경력화면 -> 자격증화면)
                career_view.setVisibility(View.INVISIBLE);
                career_view.startAnimation(tranlateFromLeftAnim);
                cert_view.setVisibility(View.VISIBLE);
                cert_view.startAnimation(tranlateToLeftAnim);
            }
        });

        btn_cert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 유저 모델에 자격증 데이터 삽입(3개)
                if (!cert_1.getText().toString().equals("")) {
                    userModel.setCert_1(cert_1.getText().toString());
                }

                if (!cert_2.getText().toString().equals("")) {
                    userModel.setCert_2(cert_2.getText().toString());
                }

                if (!cert_3.getText().toString().equals("")) {
                    userModel.setCert_3(cert_3.getText().toString());
                }
                System.out.println("Cert_1: "+userModel.getCert_1());
                System.out.println("Cert_2: "+userModel.getCert_2());   //공백("")이 삽입됨
                System.out.println("Cert_3: "+userModel.getCert_3());

                // 화면이동 (자격증화면 -> 소개화면)
                cert_view.setVisibility(View.INVISIBLE);
                cert_view.startAnimation(tranlateFromLeftAnim);
                introduce_view.setVisibility(View.VISIBLE);
                introduce_view.startAnimation(tranlateToLeftAnim);
            }
        });

        btn_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 유저 모델에 소개 데이터 삽입
                if (introduce_txt.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "회원님들께 트레이너님을 소개해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    userModel.setIntroduce(introduce_txt.getText().toString());
                }

                System.out.println("User_Introduce: "+userModel.getIntroduce());

                // 화면이동 (소개화면 -> 회원가입)
                introduce_view.startAnimation(tranlateFromLeftAnim);
                System.out.println("트레이너 회원 가입");

                dialog = new ProgressDialog(SignupActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("회원가입을 진행하는 중입니다.");

                dialog.show();

                register();

            }
        });
    }

    public void register() {
        auth = FirebaseAuth.getInstance();
        System.out.println("userId : " + userModel.getId());
        System.out.println("userName : " + userModel.getUsername());
        System.out.println("Pwd : " + userModel.getPwd());

        auth.createUserWithEmailAndPassword(userModel.getId(), userModel.getPwd())
            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        System.out.println("유저 생성 성공");
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        final String uid = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("id", userModel.getId());
                        hashMap.put("username", userModel.getUsername());
                        hashMap.put("callnum", userModel.getCallnum());
                        hashMap.put("gender", userModel.getGender());
                        hashMap.put("trainer", userModel.isTrainer());
                        hashMap.put("major", userModel.getMajor());
                        hashMap.put("area", userModel.getArea());
                        hashMap.put("career_1", userModel.getCareer_1());
                        hashMap.put("career_2", userModel.getCareer_2());
                        hashMap.put("career_3", userModel.getCareer_3());
                        hashMap.put("cert_1", userModel.getCert_1());
                        hashMap.put("cert_2", userModel.getCert_2());
                        hashMap.put("cert_3", userModel.getCert_3());
                        hashMap.put("introduce", userModel.getIntroduce());
                        hashMap.put("publisher", uid);
                        hashMap.put("pwd", userModel.getPwd());

                        if(imageUri == null) {
                            userModel.setImageurl("https://firebasestorage.googleapis.com/v0/b/instagram-9e3f7.appspot.com/o/placeholder.png?alt=media&token=72fd8bf1-8929-42b9-87a0-7a15997e0d9b");
                            hashMap.put("imageurl", userModel.getImageurl());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        System.out.println("데이터 베이스 저장 성공");
                                        SignupActivity.this.finish();
                                    }
                                }
                            });
                        } else {
                            FirebaseStorage.getInstance().getReference().child("userImages").child(userModel.getId()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            System.out.println("이미지 저장 성공");

                                            @SuppressWarnings("VisibleForTests")
                                            String imageUrl = uri.toString();

                                            System.out.println(imageUrl);

                                            userModel.setImageurl(imageUrl);

                                            System.out.println("userModel : "+userModel.getImageurl());
                                            hashMap.put("imageurl", userModel.getImageurl());

                                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        System.out.println("데이터 베이스 저장 성공");
                                                        SignupActivity.this.finish();
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });

                                        }
                                    });
                                }
                            });
                        }

                    }
                }
            });

    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
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



