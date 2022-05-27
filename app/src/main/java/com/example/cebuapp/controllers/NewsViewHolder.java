package com.example.cebuapp.controllers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cebuapp.R;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    TextView text_title, text_source;
    ImageView img_headline;
    CardView cardView;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);

        castValues();
    }

    private void castValues() {
        text_title = itemView.findViewById(R.id.newstitle);
        text_source = itemView.findViewById(R.id.newsdescription);
        img_headline = itemView.findViewById(R.id.newsimg);
        cardView = itemView.findViewById(R.id.newscardview);
    }
}
