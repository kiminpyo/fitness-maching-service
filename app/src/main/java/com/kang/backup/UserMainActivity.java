package com.kang.backup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kang.backup.fragment.ManagementListFragment;
import com.kang.backup.fragment.TrainerListFragment;
import com.kang.backup.fragment.RequestListFragment;
import com.kang.backup.fragment.SettingFragment;

public class UserMainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        BottomNavigationView btnNav = findViewById(R.id.mainactivity_bottomnavigationview);
        btnNav.setOnNavigationItemSelectedListener(navListener);

        Bundle intent = getIntent().getExtras();
        if(intent == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,
                    new TrainerListFragment()).commit();
        } else {
            switch (intent.getInt("select")) {
                case 1 :
                    btnNav.setSelectedItemId(R.id.trainer_list);
                    break;
                case 2 :
                    btnNav.setSelectedItemId(R.id.management_list);
                    break;
                case 3 :
                    btnNav.setSelectedItemId(R.id.request_list);
                    break;
                case 4 :
                    btnNav.setSelectedItemId(R.id.my_setting);
                    break;
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {

                public boolean onNavigationItemSelected(MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.trainer_list:
                            selectedFragment = new TrainerListFragment();
                            break;

                        case R.id.management_list:
                            selectedFragment = new ManagementListFragment();
                            break;

                        case R.id.request_list:
                            selectedFragment = new RequestListFragment();
                            break;

                        case R.id.my_setting:
                            selectedFragment = new SettingFragment();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + item.getItemId());
                    }
                    getSupportFragmentManager().beginTransaction().replace((R.id.mainactivity_framelayout), selectedFragment).commit();

                    return true;

                }
            };
}