package com.example.android.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;
import com.example.android.util.Refreshable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryTodoAdapter extends RecyclerView.Adapter<CategoryTodoAdapter.CategoryTodoViewHolder> implements Refreshable {

    private List<Todo> myList;
    private String cateogory;

    public CategoryTodoAdapter(String category) {
        this.cateogory = category;
        myList = new ArrayList<>();
        refresh();
        sort();
    }

    @NonNull
    @NotNull
    @Override
    public CategoryTodoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_recycler_view, parent, false);
        return new CategoryTodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryTodoViewHolder holder, int position) {
        holder.checkBox.setText(myList.get(position).getName());
        holder.checkBox.setChecked(myList.get(position).isComplete());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.getMainActivity().showTodo(myList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    @Override
    public void refresh() {
        myList.clear();
        for(Todo temp : MyUtils.getMainActivity().getList()) {
            if(temp.getCategory().equals(cateogory)) {
                myList.add(temp);
            }
        }
    }

    private void sort() {

    }

    public class CategoryTodoViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private Button button;

        public CategoryTodoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todoChk);
            button = itemView.findViewById(R.id.todoBtn);
        }
    }
}
