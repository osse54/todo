package com.example.android.activities;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.*;
import android.content.Intent;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.R;
import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

public class AddTodo extends AppCompatActivity {

    private boolean normalPause;

    private EditText name;
    private Spinner category;
    private EditText explain;
    private TextView date;
    private ImageView calendarImage;
    private Calendar calendar;

    private MenuItem add;
    private ArrayAdapter<CharSequence> categoryAdapter;

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
            menu.getItem(1).setVisible(false);
            add = menu.getItem(0);
            add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(name.getText().toString().equals("")) {
                        alertDialog("제목을 입력해주세요.");
                    } else if(category.getSelectedItem().equals(MyUtils.getCategories()[0])) {
                        alertDialog("카테고리를 선택해주세요.");
                    } else {
                        Todo todo = Todo.getTodo(name.getText().toString(), category.getSelectedItem().toString(), explain.getText().toString(), false);
                        if(date.getText().toString() != "") {
                            todo.setEnd(MyUtils.getStringFromDate(calendar.getTime()));
                        }
                        MyUtils.addTodo(todo);
                        onBackPressed();
                    }
                    return false;
                }
            });
        } else {
            menu.getItem(0).setVisible(false);
            MenuItem update = menu.getItem(1);
            update.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(name.getText().toString().equals("")) {
                        alertDialog("제목을 입력해주세요.");
                    } else if(category.getSelectedItem().equals(MyUtils.getCategories()[0])) {
                        alertDialog("카테고리를 선택해주세요.");
                    } else {
                        Todo todo = null;
                        for(int i = 0; i <MyUtils.getMainActivity().getList().size() && todo == null; i++) {
                            if(MyUtils.getMainActivity().getList().get(i).getNo() == getIntent().getLongExtra("no", -1)) {
                                todo = MyUtils.getMainActivity().getList().get(i);
                                todo.setCategory(category.getSelectedItem().toString());
                                todo.setName(name.getText().toString());
                                todo.setExplain(explain.getText().toString());
                                todo.setEnd(MyUtils.getStringFromDate(calendar.getTime()));
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

    private void alertDialog(String str) {
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(AddTodo.this, R.style.AlertDialog_AppCompat))
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setTitle(str)
                .setCancelable(false)
                .create();
        dialog.show();
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
    }

    private void setDate() {
        date.setText(MyUtils.getDateText(calendar) + "  -  일정 기한 설정");
    }

    private void post() {
        normalPause = true;
        Intent i = getIntent();
        String strName = i.getStringExtra("name");
        String strCategory = i.getStringExtra("category");
        String strExplain = i.getStringExtra("explain");
        boolean isComplete = i.getBooleanExtra("complete", false);
        int[] end = i.getIntArrayExtra("end");

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
        if(i.getStringExtra("end") != null) {
            int[] yearMonthDay = MyUtils.getDateToInt(i.getStringExtra("end"));
            calendar.set(yearMonthDay[0], yearMonthDay[1] - 1, yearMonthDay[2]);
            setDate();
        } else if(end != null) {
            calendar.set(end[0], end[1], end[2]);
            setDate();
        }
    }

    @Override
    protected void onResume() {
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
                int[] d = MyUtils.getDateToInt(strings[3]);
                calendar.set(d[0], d[1] - 1, d[2]);
                setDate();
            }
        } catch (Exception e) {}
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
        Log.i("날짜", MyUtils.getStringFromDate(calendar.getTime()));
        return new String[]{
                name.getText().toString(),
                String.valueOf(category.getSelectedItemPosition()),
                explain.getText().toString(),
                MyUtils.getStringFromDate(calendar.getTime())
        };
    }
}