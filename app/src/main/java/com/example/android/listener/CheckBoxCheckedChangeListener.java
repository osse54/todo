package com.example.android.listener;

import android.widget.CompoundButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.activities.adapter.DayAdapter;
import com.example.android.activities.adapter.TodoViewHolder;
import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;

import java.util.List;

public class CheckBoxCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
    private TodoViewHolder holder;
    private List<Todo> myList;
    private RecyclerView.Adapter adapter;
    private boolean check;

    public CheckBoxCheckedChangeListener(TodoViewHolder holder, List<Todo> myList, RecyclerView.Adapter adapter, boolean check) {
        this.holder = holder;
        this.myList = myList;
        this.adapter = adapter;
        this.check = check;
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int position = holder.getAbsoluteAdapterPosition();
        Todo todo = myList.get(position);
        todo.setComplete(!check);
        myList.remove(position);
        adapter.notifyDataSetChanged();
        // 이거 없으면 뻑남. recyclerView라서 그런지 checkBox를 재사용되어 문제가 생기는 듯 함.
        holder.getCheckBox().setOnCheckedChangeListener(null);
        DayAdapter otherAdapter = (DayAdapter) MyUtils.getOtherAdapter(adapter)[0];
        otherAdapter.insert(todo);
        MyUtils.saveList();
        MyUtils.loadList();
    }
}
