package com.example.android.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private TabItem calendar;
    private TabItem category;
    private TabItem daily;

    private View setting;

    private Button addTodo;

    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        assign();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if(time > backKeyPressedTime + 2500) {
            backKeyPressedTime = time;
            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void assign() {
//        tabLayout = (TabLayout) findViewById(R.id.mainTab);
//        calendar = (TabItem) findViewById(R.id.calendar);
//        category = (TabItem) findViewById(R.id.category);
//        daily = (TabItem) findViewById(R.id.daily);
//        addTodo = (Button) findViewById(R.id.addTodo);
    }

    private void setListener() {
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if(tab.equals(calendar)) {
//
//                } else if(tab.equals(category)) {
//
//                } else if(tab.equals(daily)) {
//
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        addTodo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

}