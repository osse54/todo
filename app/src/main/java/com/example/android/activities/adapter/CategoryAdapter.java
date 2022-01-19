package com.example.android.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.listener.OnExpandButtonClickListener;
import com.example.android.util.MyUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categories;
    private List<CategoryViewHolder> list;
    private List<TodoViewHolder> viewHolderList;

    public CategoryAdapter() {
        categories = new ArrayList<>(Arrays.asList(MyUtils.getCategories()));
        categories.remove(0);
        list = new ArrayList<>();
        viewHolderList = new ArrayList<>();
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
        holder.view.setText(categories.get(holder.getAbsoluteAdapterPosition()));
        holder.categoryTodoAdapter = new CategoryTodoAdapter(categories.get(holder.getAbsoluteAdapterPosition()));

        holder.categoryTodoRecyclerView = holder.itemView.findViewById(R.id.todoRecyclerView);
        holder.categoryTodoRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.categoryTodoRecyclerView.setAdapter(holder.categoryTodoAdapter);

        holder.categoryTodoAdapter.notifyDataSetChanged();

        holder.expandButton.setOnClickListener(new OnExpandButtonClickListener(holder.categoryTodoRecyclerView, holder.expandButton));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void refresh() {
        for(CategoryViewHolder temp : list) {
            temp.refresh();
        }
    }

    public List<TodoViewHolder> getViewHolderList() {
        viewHolderList = new ArrayList<>();
        for(CategoryViewHolder temp : list) {
            for(TodoViewHolder viewHolder : temp.categoryTodoAdapter.getViewHolderList()) {
                viewHolderList.add(viewHolder);
            }
        }
        return viewHolderList;
    }

    public List<CategoryViewHolder> getList() {
        return list;
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
            list.add(this);
        }

        public CategoryTodoAdapter getCategoryTodoAdapter() {
            return categoryTodoAdapter;
        }

        public void refresh() {
            categoryTodoAdapter.refresh();
        }
    }
}
