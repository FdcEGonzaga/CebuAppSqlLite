package com.example.cebuapp.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cebuapp.R;
import com.example.cebuapp.model.NewsApiResponse;
import com.example.cebuapp.model.NewsArticles;

import java.util.List;

public class NewsActivity extends AppCompatActivity implements NewsSelectListener, View.OnClickListener{
    RecyclerView recyclerView;
    NewsAdapter adapter;
    ProgressDialog dialog;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetchiing news articles");
        dialog.show();

        castComponents();
        setSearchViewListener();
        setBtnListeners();

        NewsRequestManager manager = new NewsRequestManager(this);
        manager.getNewsHeadlines(listener, "general", null);

    }

    private void setSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Searching for " + query + "...");
                dialog.show();

                NewsRequestManager manager = new NewsRequestManager(NewsActivity.this);
                manager.getNewsHeadlines(listener, "general", query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void castComponents() {
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);
        searchView = findViewById(R.id.search_view);
    }

    private void setBtnListeners() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
    }


    private final OnFetchNewsDataListener<NewsApiResponse> listener = new OnFetchNewsDataListener<NewsApiResponse>() {
        @Override
        public void onfetchData(List<NewsArticles> list, String message) {
            if (list.isEmpty()) {
                Toast.makeText(NewsActivity.this, "Error: No data found." , Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                showNews(list);
                dialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(NewsActivity.this, "Error: [x00" +message+ "] occured.", Toast.LENGTH_LONG).show();
        }
    };

    private void showNews(List<NewsArticles> list) {
        recyclerView = findViewById(R.id.newslist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new NewsAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnNewsClicked(NewsArticles articles) {
        startActivity(new Intent(NewsActivity.this, NewsShowActivity.class).putExtra("data", articles));
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String category = button.getText().toString();
        dialog.setTitle("Fetching " + category + " news articles.");
        dialog.show();

        // call api
        NewsRequestManager manager = new NewsRequestManager(this);
        manager.getNewsHeadlines(listener, category, null);
    }
}