package com.kang.backup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.kang.backup.model.UserModel;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    private EditText id;
    private EditText password;

    private Button login ;
    private Button signup;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        String splash_background = firebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }
        id = (EditText) findViewById(R.id.loginActivity_edittext_id);
        password = (EditText) findViewById(R.id.loginActivity_edittext_password);

        login = (Button) findViewById(R.id.loginActivity_button_login);
        signup = (Button) findViewById(R.id.loginActivity_button_signup);
        login.setBackgroundColor(Color.parseColor(splash_background));
        signup.setBackgroundColor(Color.parseColor(splash_background));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                }
                if (password.getText().toString().equals("") & id.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginEvent();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        //로그인 인터페이스 리스너

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //로그인
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserModel mUser = dataSnapshot.getValue(UserModel.class);

                            SharedPreferences.Editor editor = LoginActivity.this.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                            editor.putBoolean("isTrainer", mUser.isTrainer());
                            editor.putString("uid", user.getUid());
                            editor.apply();
                            if(mUser.isTrainer()){
                                Intent intent = new Intent(LoginActivity.this,TrainerMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(LoginActivity.this,UserMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    //로그아웃
                }

            }
        };

    }


    void loginEvent() {
        firebaseAuth.signInWithEmailAndPassword(id.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            //로그인 실패한부분
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
