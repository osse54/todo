package com.example.android.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.activities.MainActivity;
import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;
import com.example.android.util.Refreshable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> implements Refreshable {

    private List<Todo> myList;
    private boolean check;

    public DayAdapter(boolean check) {
        this.myList = new ArrayList<>();
        this.check = check;
        refresh();
    }

    @NonNull
    @NotNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_recycler_view, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DayViewHolder holder, int position) {
        Todo todo = myList.get(position);
        holder.checkBox.setText(todo.getName());
        holder.checkBox.setChecked(todo.isComplete());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myList.get(holder.getAdapterPosition()).setComplete(!check);
                MyUtils.refresh();
                notifyItemRemoved(holder.getAdapterPosition());
//                notifyDataSetChanged(); 에러
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.getMainActivity().showTodo(todo);
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
            if (temp.isComplete() == check) {
                myList.add(temp);
            }
        }
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private Button button;

        public DayViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todoChk);
            button = itemView.findViewById(R.id.todoBtn);
            button.setText("자세히");
        }
    }
}
