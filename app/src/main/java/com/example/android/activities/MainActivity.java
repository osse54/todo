package com.example.android.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import com.example.android.R;
import com.example.android.activities.fragment.Calendar;
import com.example.android.activities.fragment.Category;
import com.example.android.activities.fragment.Daily;
import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Fragment calendarFragment;
    private Fragment categoryFragment;
    private Fragment dailyFragment;

    private FloatingActionButton addTodo;

    private long backKeyPressedTime = 0;

    private List<Todo> list;

    public MainActivity() {
        list = new ArrayList<>();
        list.add(new Todo("하체 운동", "운동", "3일 마다 돌아오는 하체 운동 시간", false));
        list.add(new Todo("C언어 2주차", "학교 과제", "", false));
        list.add(new Todo("안드로이드 3주차", "팀노바 과제", "", false));
        list.add(new Todo("점심 약속", "일상", "", true));
        list.add(new Todo("안드로이드 1주차", "팀노바 과제", "", true));
        MyUtils.setMainActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assign();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return true;
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
        tabLayout = findViewById(R.id.mainTab);
        calendarFragment = new Calendar();
        categoryFragment = new Category();
        dailyFragment = new Daily();
        addTodo = findViewById(R.id.addTodo);
        setFragment();
        setListener();
    }

    private void setListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFragment();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddTodo.class));
            }
        });
    }

    private void setFragment() {
        Fragment selected = null;
        int position = tabLayout.getSelectedTabPosition();
        if(position == 0) {
            selected = dailyFragment;
        } else if(position == 1) {
            selected = categoryFragment;
        } else if(position == 2) {
            selected = calendarFragment;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, selected).commit();
    }

    // activity_add_todo 수정 시 고쳐야 함
    public void showTodo(Todo todo) {
        startActivity(MyUtils.getIntent(todo).setClass(getApplicationContext(), AddTodo.class));
    }

    public List<Todo> getList() {
        return list;
    }

}