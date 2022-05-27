package com.example.cebuapp.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cebuapp.R;
import com.example.cebuapp.model.DatabaseHelper;

public class SpotsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Intent intent;
    private Button btnAll, btn1, btn2, btn3, btn4, btn5;
    private ProgressDialog dialog;
    private ListView spotsList;
    private TextView noSpotsMsg;
    private int userID, spotID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots);

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);

        userID = userID();
        castComponents();
        noSpotsMsg.setVisibility(View.INVISIBLE);

        // get jobs
        setBtnOnClicks();

        dialog.setTitle("Fetchiing Cebu's tourist spots.");
        getSpotsList(0);
    }

    private void castComponents() {
        btnAll = findViewById(R.id.btnAll);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        spotsList = findViewById(R.id.spotsList);
        noSpotsMsg = findViewById(R.id.noSpotsMsg);
    }

    private void setBtnOnClicks() {
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing All Tourist Spots.");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSpotsList(0);

                    }
                }, 2000);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing BOGO's spots");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSpotsList(1);

                    }
                }, 2000);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing CARMEN's spots");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSpotsList(2);

                    }
                }, 2000);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing DANAO's spots");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSpotsList(3);

                    }
                }, 2000);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing TABOGON's spots");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSpotsList(4);

                    }
                }, 2000);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing TABUELAN's spots");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSpotsList(5);

                    }
                }, 2000);
            }
        });


    }

    private void getSpotsList(int spotCatID) {
        try{
            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            cursor = DatabaseHelper.selectSpotCategoryById(db, spotCatID);

            if (cursor != null && cursor.getCount() > 0) {
                CursorAdapter listAdapter = new SimpleCursorAdapter(getApplicationContext(),
                        R.layout.custom_list_item,
                        cursor,
                        new String[]{"SPOTTITLE", "SPOTLOCATION", "SPOTLOCATION"},
                        new int[]{R.id.listTitle, R.id.listDesc1, R.id.listDesc2}, 0);
                noSpotsMsg.setVisibility(View.GONE);
                spotsList.setVisibility(View.VISIBLE);
                spotsList.setAdapter(listAdapter);
            } else {
                spotsList.setVisibility(View.GONE);
                noSpotsMsg.setVisibility(View.VISIBLE);
            }
            dialog.dismiss();

            // onclick of list
            spotsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    spotID = (int) id;
                    Toast.makeText(getApplicationContext(), "Viewing spot ID " + String.valueOf(spotID) , Toast.LENGTH_SHORT).show();

                    intent = new Intent(getApplicationContext(), SpotsDetailsActivity.class);
                    intent.putExtra("SPOTID", spotID);

                    startActivity(intent);
                }
            });

        } catch (SQLiteException e){
            Toast.makeText(getApplicationContext(), "Database unavailable for jobs", Toast.LENGTH_SHORT).show();
        }
    }

    public int userID() {
        LoginActivity.sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        userID = LoginActivity.sharedPreferences.getInt(LoginActivity.USER_ID, 0);
        return userID;
    }
}