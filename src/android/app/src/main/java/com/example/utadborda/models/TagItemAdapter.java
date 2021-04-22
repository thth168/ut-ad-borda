package com.example.utadborda.models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.example.utadborda.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class TagItemAdapter extends ArrayAdapter {

    List<Tag> tagItems = new ArrayList<>();
    private MaterialButtonToggleGroup toggleGroup;
    int id = 0;

    public TagItemAdapter(@NonNull Context context, int resource, List<Tag> objects) {
        super(context, resource, objects);
        tagItems = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.grid_view_items, null);
        Button mTagButton = (Button) v.findViewById(R.id.button1);
        mTagButton.setText(tagItems.get(position).getTagName());
//        mTagButton
        toggleGroup = (MaterialButtonToggleGroup) v.findViewById(R.id.toggle_group);
//        toggleGroup.addView(mTagButton, id);
        id++;
//        addToToggleGroup(v, mTagButton);
//        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
//            @Override
//            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
////                if(isChecked){
//                Log.i("WaitingRoom", String.valueOf(group));
////                }
//            }
//        });
        return v;
    }

    public void addToToggleGroup(View v, Button button){
        toggleGroup = (MaterialButtonToggleGroup) v.findViewById(R.id.toggle_group);
        toggleGroup.addView(button);
    }
}

