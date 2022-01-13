package com.example.android.activities.adapter;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.activities.OnExpandButtonClickListener;
import com.example.android.util.MyUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categories;

    public CategoryAdapter() {
        categories = new ArrayList<>(Arrays.asList(MyUtils.getMainActivity().getResources().getStringArray(R.array.category)));
        categories.remove(0);
    }

    @NonNull
    @NotNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_recycler_view, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryViewHolder holder, int position) {
        holder.view.setText(categories.get(position));
        holder.categoryTodoAdapter = new CategoryTodoAdapter(categories.get(position));


        holder.categoryTodoRecyclerView = holder.itemView.findViewById(R.id.todoRecyclerView);
        holder.categoryTodoRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.categoryTodoRecyclerView.setAdapter(holder.categoryTodoAdapter);

        holder.categoryTodoAdapter.notifyDataSetChanged();

        holder.expandButton.setOnClickListener(new OnExpandButtonClickListener(holder.categoryTodoRecyclerView, holder.expandButton));
    }

    @Override
    public int getItemCount() {
        Log.i(this.getClass().toString(), String.valueOf(categories.size()));
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView view;
        private ImageView expandButton;

        private RecyclerView categoryTodoRecyclerView;
        private CategoryTodoAdapter categoryTodoAdapter;

        public CategoryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.categoryName);
            expandButton = itemView.findViewById(R.id.categoryExpandBtn);
        }
    }
}
