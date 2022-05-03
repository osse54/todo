package com.example.android.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.dto.Todo;
import com.example.android.listener.CheckBoxCheckedChangeListener;
import com.example.android.util.MyUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private ArrayList<Todo> myList;
    private ArrayList<TodoViewHolder> viewHolderList;
    private boolean check;

    public DayAdapter(boolean check) {
        myList = new ArrayList<>();
        viewHolderList = new ArrayList<>();
        this.check = check;
        refresh();
    }

    @NonNull
    @NotNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_recycler_view, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TodoViewHolder holder, int position) {
        Todo todo = myList.get(holder.getAbsoluteAdapterPosition());
        holder.no = todo.no;
        holder.complete = todo.isComplete();
        String date = "   ";
        if(todo.getEnd() != null) {
            int[] ymd = MyUtils.getDateToInt(todo.getEnd());
            date += MyUtils.getDateText(ymd[0], ymd[1], ymd[2]);
        }
        holder.checkBox.setText(todo.getName() + date);
        holder.checkBox.setChecked(todo.isComplete());
        holder.setCheckBoxListener(new CheckBoxCheckedChangeListener(holder, myList, this, check));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.getMainActivity().showTodo(todo);
            }
        });
        viewHolderList.add(holder);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public void refresh() {
        myList.clear();
        for(Todo temp : MyUtils.getMainActivity().getList()) {
            if (temp.isComplete() == check) {
                myList.add(temp);
            }
        }
        notifyDataSetChanged();
    }

    public void insert(Todo todo) {
        refresh();
        notifyItemInserted(myList.indexOf(todo));
        notifyDataSetChanged();
    }

    public ArrayList<TodoViewHolder> getViewHolderList() {
        return viewHolderList;
    }

    public ArrayList<Todo> getMyList() {
        return myList;
    }

    public boolean isCheck() {
        return check;
    }
}
