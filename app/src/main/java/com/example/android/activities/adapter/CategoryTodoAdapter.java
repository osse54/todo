package com.example.android.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CategoryTodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private List<Todo> myList;
    private String cateogory;
    private List<TodoViewHolder> viewHolderList;

    public CategoryTodoAdapter(String category) {
        this.cateogory = category;
        myList = new ArrayList<>();
        refresh();
        sort();
        viewHolderList = new ArrayList<>();
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
        viewHolderList.add(holder);
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
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                holder.checkBox.setOnCheckedChangeListener(null);
                if(findRelativeAdapterPositionIn(CategoryTodoAdapter.this, holder, holder.getAbsoluteAdapterPosition()) != RecyclerView.NO_POSITION) {
                    todo.setComplete(!todo.isComplete());
                    refresh();
                }
                holder.checkBox.setOnCheckedChangeListener(this::onCheckedChanged);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.getMainActivity().showTodo(myList.get(holder.getAbsoluteAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public void refresh() {
        myList.clear();
        myList = new ArrayList<>();
        for(Todo temp : MyUtils.getMainActivity().getList()) {
            if(temp.getCategory().equals(cateogory)) {
                myList.add(temp);
            }
        }
        sort();
        notifyDataSetChanged();
    }

    private void sort() {
        myList.sort(new Comparator<Todo>() {
            @Override
            public int compare(Todo o1, Todo o2) {
                int result = 0;
                if(o1.isComplete() == false && o2.isComplete() == true) {
                    result = -1;
                }
                if(o1.isComplete() == o2.isComplete()) {
                    result = 0;
                    if(o1.no < o2.no) {
                        result = -1;
                    }
                }
                return result;
            }
        });
    }

    public List<TodoViewHolder> getViewHolderList() {
        return viewHolderList;
    }
}
