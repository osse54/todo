package com.example.android.activities;
import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import android.content.Intent;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.example.android.R;
import com.example.android.activities.adapter.TodoViewHolder;
import com.example.android.activities.fragment.CalendarFragment;
import com.example.android.activities.fragment.Category;
import com.example.android.activities.fragment.Daily;
import com.example.android.activities.fragment.TodoFragment;
import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private TodoFragment calendarFragment;
    private TodoFragment categoryFragment;
    private TodoFragment dailyFragment;

    private FloatingActionButton addTodo;

    private long backKeyPressedTime = 0;

    private List<Todo> list;
    private boolean flag;

    public MainActivity() {
        list = new ArrayList<>();
        MyUtils.setMainActivity(this);
        flag = true;
        toastMsg();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, LoadingActivity.class));
        assign();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);

        MenuItem item = menu.getItem(0);
        item.setCheckable(true);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                menuItem.setChecked(!menuItem.isChecked());
                flag = !menuItem.isChecked();
                if(!menuItem.isChecked()) {
                    toastMsg();
                }
                return menuItem.isChecked();
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                prepareDelete(menu);
                return false;
            }
        });
        menu.getItem(1).setVisible(false);
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                cancelDelete(menu);
                return false;
            }
        });
        menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                EditText input = new EditText(MainActivity.this);

                builder.setTitle("추가할 카테고리를 입력해주세요");
                builder.setView(input);
                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MyUtils.addCategory(input.getText().toString());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        return true;
    }

    private void cancelDelete(Menu menu) {
        addTodo.setVisibility(View.VISIBLE);
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(false);
        menu.getItem(3).setVisible(true);
        menu.getItem(2).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.getItem(2).setTitle("삭제하기");
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                prepareDelete(menu);
                return false;
            }
        });
        for(TodoViewHolder temp : getSelectedFragment().getViewHolderList()) {
            temp.returnToNormal();
        }
    }

    private void prepareDelete(Menu menu) {
        addTodo.setVisibility(View.INVISIBLE);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
        menu.getItem(3).setVisible(false);
        menu.getItem(2).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(2).setTitle("삭제");
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                delete(menu);
                return false;
            }
        });
        prepareView();
    }

    private void prepareView() {
        Log.i(getSelectedFragment().toString(), String.valueOf(getSelectedFragment().getViewHolderList().size()));
        for(TodoViewHolder temp : getSelectedFragment().getViewHolderList()) {
            temp.prepareDelete();
        }
    }

    private void delete(Menu menu) {
        for(TodoViewHolder temp : getSelectedFragment().getViewHolderList()) {
            if(temp.isChecked()) {
                deleteTodo(temp.getNo());
                Log.i("지우기", String.valueOf(temp.getNo()));
            }
        }
        getSelectedFragment().refresh();
        cancelDelete(menu);
        MyUtils.updateTodo();
    }

    private void deleteTodo(long no) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).no == no) {
                list.remove(i);
            }
        }
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
        calendarFragment = new CalendarFragment();
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
                /*
                만약 CalendarFragment이면
                CalendarFragment에서 선택한 날짜 받아오기
                Intent에 putString 또는 뭔
                 */
                Intent intent = new Intent(getApplicationContext(), AddTodo.class);
                if(getSelectedFragment() == calendarFragment && MyUtils.isSetDate()) {
                    intent.putExtra("end", MyUtils.getDate());
                }
                startActivity(intent);
            }
        });
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MyUtils.loadList();
                        getSelectedFragment().refresh();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        MyUtils.swipeRefreshLayout = swipeRefreshLayout;
    }

    private void setFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, getSelectedFragment()).commit();
    }

    private TodoFragment getSelectedFragment() {
        TodoFragment selected = null;
        int position = tabLayout.getSelectedTabPosition();
        if(position == 0) {
            selected = dailyFragment;
        } else if(position == 1) {
            selected = categoryFragment;
        } else if(position == 2) {
            selected = calendarFragment;
        }
        return selected;
    }

    @Override
    protected void onResume() {
        MyUtils.loadList();
        super.onResume();
    }

    // activity_add_todo 수정 시 고쳐야 함
    public void showTodo(Todo todo) {
        startActivity(MyUtils.getIntent(todo).setClass(getApplicationContext(), AddTodo.class));
    }

    public List<Todo> getList() {
        return list;
    }

    private void toastMsg() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int amount = 0;
                for(Todo temp : list) {
                    if(!temp.isComplete()) {
                        amount++;
                    }
                }
                if(amount > 0 && flag) {
                    Toast.makeText(MainActivity.this, "미완료 일정 : " + amount, Toast.LENGTH_SHORT).show();
                }
            }
        };
        Handler handler = new Handler();
        new Thread() {
            @Override
            public void run() {
                while(flag) {
                    try {
                        Log.i("스레드", "잠");
                        Thread.sleep(5000);
                        Log.i("스레드", "일남");
                        handler.post(runnable);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}