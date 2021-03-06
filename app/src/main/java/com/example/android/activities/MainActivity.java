package com.example.android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

import java.util.ArrayList;
import java.util.List;

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

                builder.setTitle("????????? ??????????????? ??????????????????");
                builder.setView(input);
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MyUtils.addCategory(input.getText().toString());
                        getSelectedFragment().refresh();
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        menu.getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("????????? ??????????????? ??????????????????");
                builder.setItems(MyUtils.getCategoriesMain(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyUtils.removeCategory(MyUtils.getCategoriesMain()[i]);
                        getSelectedFragment().refresh();
                    }
                });
                builder.create().show();
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
        menu.getItem(4).setVisible(true);

        // https://stackoverflow.com/questions/15479522/substitute-to-menucompat-setshowasaction-on-android
        menu.getItem(2).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.getItem(2).setTitle("????????????");
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
        menu.getItem(4).setVisible(false);

        // https://stackoverflow.com/questions/15479522/substitute-to-menucompat-setshowasaction-on-android
        menu.getItem(2).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(2).setTitle("??????");
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
        for(TodoViewHolder temp : getSelectedFragment().getViewHolderList()) {
            temp.prepareDelete();
        }
    }

    private void delete(Menu menu) {
        for(TodoViewHolder temp : getSelectedFragment().getViewHolderList()) {
            if(temp.isChecked()) {
                deleteTodo(temp.getNo());
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
            Toast.makeText(this, "???????????? ????????? ??? ??? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
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
                ?????? CalendarFragment??????
                CalendarFragment?????? ????????? ?????? ????????????
                Intent??? putString ?????? ???
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

    // activity_add_todo ?????? ??? ????????? ???
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
                    Toast.makeText(MainActivity.this, "????????? ?????? : " + amount, Toast.LENGTH_SHORT).show();
                }
            }
        };
        Handler handler = new Handler();
        new Thread() {
            @Override
            public void run() {
                while(flag) {
                    try {
                        Thread.sleep(5000);
                        handler.post(runnable);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}