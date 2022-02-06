package com.kang.backup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.kang.backup.fragment.TrainerDetailFragment;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedFragment = new TrainerDetailFragment();

        //1.매니저 생성
        FragmentManager fragmentManager = getSupportFragmentManager();
        //2.시작
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //3.추가, 삭제, 교체
        fragmentTransaction.replace(R.id.mainactivity_framelayout, selectedFragment);
        //4.수행
        fragmentTransaction.commit();
    }
}