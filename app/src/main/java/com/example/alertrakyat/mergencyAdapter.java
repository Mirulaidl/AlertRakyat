package com.example.alertrakyat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class mergencyAdapter extends ArrayAdapter<mergency> {
    public mergencyAdapter(@NonNull Context context, ArrayList<mergency> mergencyArrayList) {
        super(context, R.layout.list_emergency, mergencyArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        mergency mergency = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_emergency,parent,false);
        }

        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        TextView listNumber = view.findViewById(R.id.listNumber);

        listImage.setImageResource(mergency.image);
        listName.setText(mergency.name);
        listNumber.setText(mergency.number);

        return view;
    }
}
