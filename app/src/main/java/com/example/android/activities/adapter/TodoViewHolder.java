package com.example.android.activities.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.listener.CheckBoxCheckedChangeListener;
import org.jetbrains.annotations.NotNull;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    long no;
    boolean complete;
    CheckBox checkBox;
    Button button;
    CheckBoxCheckedChangeListener listener;

    public TodoViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.todoChk);
        button = itemView.findViewById(R.id.todoBtn);
        button.setText("자세히");
    }

    public void prepareDelete() {
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(false);
        button.setVisibility(Button.INVISIBLE);
    }

    public void returnToNormal() {
        checkBox.setChecked(complete);
        checkBox.setOnCheckedChangeListener(listener);
        button.setVisibility(Button.VISIBLE);
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public long getNo() {
        return no;
    }

    public void setCheckBoxListener(CheckBoxCheckedChangeListener checkBoxCheckedChangeListener) {
        listener = checkBoxCheckedChangeListener;
        checkBox.setOnCheckedChangeListener(listener);
    }
}
