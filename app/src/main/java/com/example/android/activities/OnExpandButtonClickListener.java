package com.example.android.activities;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

public class OnExpandButtonClickListener implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ImageView button;

    public OnExpandButtonClickListener(RecyclerView recyclerView, ImageView button) {
        this.recyclerView = recyclerView;
        this.button = button;
    }

    @Override
    public void onClick(View v) {
        if(recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            button.animate().setDuration(200).rotation(0f);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            button.animate().setDuration(200).rotation(180f);
        }
    }
}
