package com.example.android.activities;
import android.util.Log;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.example.android.R;
import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.*;
import com.naver.maps.map.overlay.Marker;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class AddTodo extends AppCompatActivity implements OnMapReadyCallback {

    private boolean normalPause;

    private EditText name;
    private Spinner category;
    private EditText explain;
    private TextView date;
    private ImageView calendarImage;
    private ImageView delete;
    private Calendar calendar;
    private EditText location;
    private ImageView searchIcon;

    private MenuItem add;
    private ArrayAdapter<CharSequence> categoryAdapter;

    private MapFragment map;
    private Marker marker;
    private LatLng latLng;
    private NaverMap naverMap;
    private ActivityResultLauncher<Intent> mPreContractStartActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        if(result.getData().getStringExtra("location") != null) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    location.setText(result.getData().getStringExtra("location"));
                                }
                            });
                        }
                        if(result.getData().getParcelableExtra("latLng") != null) {
                            latLng = result.getData().getParcelableExtra("latLng");
                            mark();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        assign();
        normalPause = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        if(getIntent().getLongExtra("no", -1) == -1) {
            // 추가 버튼
            menu.getItem(1).setVisible(false);
            add = menu.getItem(0);
            add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(name.getText().toString().equals("")) {
                        MyUtils.alertDialog("제목을 입력해주세요.", AddTodo.this);
                    } else if(category.getSelectedItem().equals(MyUtils.getCategories()[0])) {
                        MyUtils.alertDialog("카테고리를 선택해주세요.", AddTodo.this);
                    } else {
                        Todo todo = Todo.getTodo(name.getText().toString(), category.getSelectedItem().toString(), explain.getText().toString(), false);
                        if(latLng != null) {
                            todo.setLatLng(latLng);
                        }
                        if(!location.getText().toString().equals("") && location.getText().toString() != null) {
                            todo.setLocationName(location.getText().toString());
                        }
                        if(!date.getText().toString().equals("")) {
                            todo.setEnd(MyUtils.getStringFromDate(calendar.getTime()));
                        }
                        MyUtils.addTodo(todo);
                        onBackPressed();
                    }
                    return false;
                }
            });
        } else {
            // 수정 버튼
            menu.getItem(0).setVisible(false);
            MenuItem update = menu.getItem(1);
            update.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(name.getText().toString().equals("")) {
                        MyUtils.alertDialog("제목을 입력해주세요.", AddTodo.this);
                    } else if(category.getSelectedItem().equals(MyUtils.getCategories()[0])) {
                        MyUtils.alertDialog("카테고리를 선택해주세요.", AddTodo.this);
                    } else {
                        Todo todo = null;
                        for(int i = 0; i <MyUtils.getMainActivity().getList().size() && todo == null; i++) {
                            if(MyUtils.getMainActivity().getList().get(i).getNo() == getIntent().getLongExtra("no", -1)) {
                                todo = MyUtils.getMainActivity().getList().get(i);
                                todo.setCategory(category.getSelectedItem().toString());
                                todo.setName(name.getText().toString());
                                todo.setExplain(explain.getText().toString());
                                if(!date.getText().toString().equals("")) {
                                    todo.setEnd(MyUtils.getStringFromDate(calendar.getTime()));
                                } else {
                                    todo.setEnd(null);
                                }
                                if(latLng != null) {
                                    todo.setLatLng(latLng);
                                }
                                if(!location.getText().toString().equals("") && location.getText().toString() != null) {
                                    todo.setLocationName(location.getText().toString());
                                }
                            }
                        }
                        MyUtils.updateTodo();
                        onBackPressed();
                    }
                    return false;
                }
            });
        }
        return true;
    }

    private void assign() {
        name = findViewById(R.id.todoName);
        category = findViewById(R.id.todoCategory);
        explain = findViewById(R.id.todoExplain);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        categoryAdapter.addAll(MyUtils.getCategories());
        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);
        date = findViewById(R.id.todoDate);
        calendarImage = findViewById(R.id.calendarImage);
        calendar = Calendar.getInstance();
        delete = findViewById(R.id.addTodoClose);
        location = findViewById(R.id.addTodoLocation);
        searchIcon = findViewById(R.id.addTodoSearchIcon);

        FragmentManager fm = getSupportFragmentManager();
        map = (MapFragment)fm.findFragmentById(R.id.addTodoMap);
        if (map == null) {
            map = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.addTodoMap, map).commit();
        }

        setListener();
        post();
    }

    private void setListener() {
        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AddTodo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        setDate();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete.setVisibility(View.INVISIBLE);
                date.setText("");
            }
        });
        if(getIntent().getBooleanExtra("complete", false)) {
            Drawable d = name.getForeground();
            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b) {
                        view.setForeground(null);
                    } else {
                        view.setForeground(d);
                    }
                }
            });
        }
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTodo.this, MapActivity.class);
                if(latLng != null) {
                    intent.putExtra("latLng", latLng);
                }
                if(location.getText().toString() != null) {
                    if(!location.getText().toString().equals("")) {
                        intent.putExtra("location", location.getText().toString());
                    }
                }
                mPreContractStartActivityResult.launch(intent);
//                startActivity(new Intent(AddTodo.this, MapActivity.class));
            }
        });
    }

    private void setDate() {
        date.setText(MyUtils.getDateText(calendar) + "  -  일정 기한 설정");
        delete.setVisibility(View.VISIBLE);
    }

    private void post() {
        normalPause = true;
        Intent i = getIntent();
        String strName = i.getStringExtra("name");
        String strCategory = i.getStringExtra("category");
        String strExplain = i.getStringExtra("explain");
        boolean isComplete = i.getBooleanExtra("complete", false);
        String strEnd = i.getStringExtra("end");
        String strLocation = i.getStringExtra("location");
        String strLatitude = i.getStringExtra("latitude");
        String strLongitude = i.getStringExtra("longitude");

        if(strName != null && strName != "") {
            name.setText(strName);
        }
        if(strCategory != null && strCategory != "") {
            String[] arr = MyUtils.getCategories();
            for(int idx = 0; idx < arr.length; idx++) {
                if(strCategory.equals(arr[idx])) {
                    category.setSelection(idx);
                }
            }
        }
        if(strExplain != null && strExplain != "") {
            explain.setText(strExplain);
        }
        if(isComplete) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    setTitle("완료된 일정");
                }
            });
        } else {
            name.setForeground(null);
        }
        if(strEnd != null) {
            int[] ymd = MyUtils.getDateToInt(strEnd); // ymd = Year Month Day
            calendar.set(ymd[0], ymd[1] - 1, ymd[2]);
            setDate();
        } else if(MyUtils.isSetDate()) {
            calendar.set(MyUtils.year, MyUtils.month, MyUtils.day);
            setDate();
        }
        if(strLocation != null && strLocation != "") {
            location.setText(strLocation);
        }
        if(strLatitude != null && strLatitude != "" && strLongitude != null && strLongitude != "") {
            latLng = new LatLng(Double.parseDouble(strLatitude), Double.parseDouble(strLongitude));
        }
    }

    @Override
    protected void onResume() {
        if(getIntent().getLongExtra("no", -1) == -1) {
            try {
                String[] strings = MyUtils.loadAddingTodo();
                if(strings[0] != null) {
                    name.setText(strings[0]);
                }
                if(strings[1] != null) {
                    category.setSelection(Integer.parseInt(strings[1]));
                }
                if(strings[2] != null) {
                    explain.setText(strings[2]);
                }
                if(strings[3] != null) {
                    int[] ymd = MyUtils.getDateToInt(strings[3]); // ymd = Year Month Day
                    calendar.set(ymd[0], ymd[1] - 1, ymd[2]);
                    setDate();
                }
                if(strings[4] != null) {
                    location.setText(strings[4]);
                }
                if(strings[5] != null && strings[6] != null) {
                    latLng = new LatLng(Double.parseDouble(strings[5]), Double.parseDouble(strings[6]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(latLng == null) {
            findViewById(R.id.addTodoMap).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.addTodoMap).setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            map.getMapAsync(AddTodo.this);
                            mark();
                        }
                    });
                }
            }).start();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if(!normalPause) {
            save();
        }
        super.onStop();
    }

    // 뒤로가기 버튼
    @Override
    public void onBackPressed() {
        normalPause = true;
        super.onBackPressed();
    }

    // 해당 화면에서 일정 작성 중에 추가 또는 뒤로가기로 인한 onPause()호출인 경우 사용
    private void save() {
        MyUtils.saveAddingTodo(getFields());
    }

    private String[] getFields() {
        return new String[] {
                name.getText().toString(),
                String.valueOf(category.getSelectedItemPosition()),
                explain.getText().toString(),
                date.getText().toString().equals("") ? null : MyUtils.getStringFromDate(calendar.getTime()),
                location.getText().toString(),
                latLng == null ? null : String.valueOf(latLng.latitude),
                latLng == null ? null : String.valueOf(latLng.longitude)
        };
    }

    @Override
    public void onMapReady(@NonNull @NotNull NaverMap naverMap) {
        this.naverMap = naverMap;
    }

    public void mark() {
        // 맵 핀 찍기
        if(marker == null) {
            marker = new Marker();
        }
        marker.setPosition(latLng);
        if(naverMap == null){
            map.getMapAsync(this);

        }
        if(naverMap != null){
            marker.setMap(naverMap);
            CameraPosition position = new CameraPosition(latLng, 15);
            naverMap.setCameraPosition(position);
        }
    }
}