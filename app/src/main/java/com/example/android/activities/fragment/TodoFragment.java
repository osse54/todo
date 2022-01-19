package com.example.android.activities.fragment;

import androidx.fragment.app.Fragment;
import com.example.android.activities.adapter.TodoViewHolder;

import java.util.ArrayList;

public abstract class TodoFragment extends Fragment {
    public abstract ArrayList<TodoViewHolder> getViewHolderList();
    public abstract void refresh();
}
