package com.kang.backup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.kang.backup.model.UserModel;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    UserModel user;
    boolean isTrainer;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        linearLayout = (LinearLayout) findViewById(R.id.splashactivity_linaerlayout);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.default_config);

        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.fetchAndActivate();
                        } else {

                        }
                        displayMessage();
                    }
                });


    }

    void displayMessage() {
        String splash_background = mFirebaseRemoteConfig.getString("splash_background");
        boolean caps = mFirebaseRemoteConfig.getBoolean("splash_message_caps");
        String splash_message = mFirebaseRemoteConfig.getString("splash_message");

        linearLayout.setBackgroundColor(Color.parseColor(splash_background));

        if(caps){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(splash_message).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            builder.create().show();

        }else {
            if(firebaseUser != null) {
                reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                System.out.println(reference);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(UserModel.class);
                        isTrainer = user.isTrainer();
                        SharedPreferences.Editor editor = SplashActivity.this.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                        editor.putBoolean("isTrainer", isTrainer);
                        editor.putString("uid", user.getPublisher());
                        editor.apply();

                        changeActivity();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            }
        }
    }

    void changeActivity() {
        if(isTrainer){
            startActivity(new Intent(this, TrainerMainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, UserMainActivity.class));
            finish();
        }
    }

}