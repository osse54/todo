package com.example.android.activities;
import android.widget.Toast;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.R;

public class AddTodo extends AppCompatActivity {

    private boolean normalPause;

    private EditText name;
    private Spinner category;
    private EditText explain;
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
        return true;
    }

    private void assign() {
        name = findViewById(R.id.todoName);
        category = findViewById(R.id.todoCategory);
        explain = findViewById(R.id.todoExplain);
        categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        setListener();
        post();
    }

    private void setListener() {
        category.setAdapter(categoryAdapter);
    }

    private void post() {
        Intent i = getIntent();
        String strName = i.getStringExtra("name");
        String strCategory = i.getStringExtra("category");
        String strExplain = i.getStringExtra("explain");
        boolean isComplete = i.getBooleanExtra("complete", false);

        if(strName != null && strName != "") {
            name.setText(strName);
        }
        if(strCategory != null && strCategory != "") {
            String[] arr = getResources().getStringArray(R.array.category);
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
            // todo 체크버튼 추가되면 체크되는 코드 넣기
        }
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

    // 추가
    private void add() {
        normalPause = true;
    }

    // 해당 화면에서 일정 작성 중에 추가 또는 뒤로가기로 인한 onPause()호출인 경우 사용
    private void save() {
        Toast.makeText(this, "AddTodo, 저장", Toast.LENGTH_SHORT).show();
    }

}