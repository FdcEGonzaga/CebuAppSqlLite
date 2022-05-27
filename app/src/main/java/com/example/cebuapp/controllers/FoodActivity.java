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

public class FoodActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Intent intent;
    private Button btnAll, btn1, btn2, btn3, btn4, btn5, btn6, btn7;
    private ProgressDialog dialog;
    private ListView foodList;
    private TextView noFoodMsg;
    private int userID, foodID;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, DatabaseHelper.JOBFIELDS);

        userID = userID();
        castComponents();
        handler = new Handler();

        sharedPreferences = getSharedPreferences("MY_PREFS", 0);

        // get jobs
        setBtnOnClicks();

        dialog.setTitle("Fetchiing all Cebu food areas.");

        getFoodsList(0);
    }

    private void castComponents() {
        btnAll = findViewById(R.id.btnAll);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        foodList = findViewById(R.id.foodList);
        noFoodMsg = findViewById(R.id.noFoodMsg);
    }

    private void setBtnOnClicks() {
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing ALCOY's food areas.");
                dialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodsList(0);

                    }
                }, 2000);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing all food areas.");
                dialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodsList(1);

                    }
                }, 2000);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing ALEGRIA's food areas.");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodsList(2);

                    }
                }, 2000);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing ALCANTARA's food areas.");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodsList(3);

                    }
                }, 2000);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing BARILI's food areas.");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodsList(4);

                    }
                }, 2000);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing BADIAN's food areas.");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodsList(5);

                    }
                }, 2000);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing BOGO's food areas.");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodsList(6);

                    }
                }, 2000);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing CARMEN's food areas.");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodsList(7);

                    }
                }, 2000);
            }
        });
    }

    private void getFoodsList(int foodLocationID) {
        try{
            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            cursor = DatabaseHelper.selectFoodLocById(db, foodLocationID);

            if (cursor != null && cursor.getCount() > 0) {
                CursorAdapter listAdapter = new SimpleCursorAdapter(getApplicationContext(),
                        R.layout.custom_list_item,
                        cursor,
                        new String[]{"FOODTITLE", "FOODLOCATION", "FOODPOSTED"},
                        new int[]{R.id.listTitle, R.id.listDesc1, R.id.listDesc2}, 0);
                noFoodMsg.setVisibility(View.GONE);
                foodList.setVisibility(View.VISIBLE);
                foodList.setAdapter(listAdapter);
            } else {
                foodList.setVisibility(View.GONE);
                noFoodMsg.setVisibility(View.VISIBLE);
            }
            dialog.dismiss();

            // onclick of list
            foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    foodID = (int) id;
                    Toast.makeText(getApplicationContext(), "Viewing job ID " + String.valueOf(foodID) , Toast.LENGTH_SHORT).show();

                    intent = new Intent(getApplicationContext(), FoodDetailsActivity.class);
                    intent.putExtra("FOODID", foodID);

                    startActivity(intent);
                }
            });

        } catch (SQLiteException e){
            Toast.makeText(getApplicationContext(), "Database unavailable for foods", Toast.LENGTH_SHORT).show();
        }
    }

    public int userID() {
        LoginActivity.sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        userID = LoginActivity.sharedPreferences.getInt(LoginActivity.USER_ID, 0);
        return userID;
    }
}