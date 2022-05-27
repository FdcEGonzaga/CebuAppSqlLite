package com.example.cebuapp.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cebuapp.R;
import com.example.cebuapp.model.DatabaseHelper;

public class HomeActivity extends AppCompatActivity {
    private ImageButton btnNews, btnJobs, btnSpots, btnFoods, btnCommutes, btnMap, btnDial, btnLogout;
    public static final String MY_PREFERENCES = "MY_PREFS";
    public static final String EMAIL = "EMAIL";
    public static final String USER_ID = "ID";
    public static final String LOGIN_STATUS = "LOGGED_IN";
    public static SharedPreferences sharedPreferences;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userID = userID();
        castValues();
        setOnclickListeners();

        sharedPreferences = getSharedPreferences("MY_PREFS", 0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DatabaseHelper.JOBFIELDS);

    }

    public int userID() {
        LoginActivity.sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        userID = LoginActivity.sharedPreferences.getInt(LoginActivity.USER_ID, 0);
        return userID;
    }

    private void castValues() {
        btnNews = findViewById(R.id.btnnews);
        btnJobs = findViewById(R.id.btnjobs);
        btnSpots = findViewById(R.id.btnspots);
        btnFoods = findViewById(R.id.btnfoods);
        btnCommutes = findViewById(R.id.btncommutes);
        btnMap = findViewById(R.id.btnmap);
        btnDial = findViewById(R.id.btnDial);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setOnclickListeners() {

        btnNews.setOnClickListener(view-> {
            startActivity(new Intent(getApplicationContext(), NewsActivity.class));
        });

        btnJobs.setOnClickListener(view-> {
            startActivity(new Intent(getApplicationContext(), JobsActivity.class));
        });

        btnFoods.setOnClickListener(view-> {
            startActivity(new Intent(getApplicationContext(), FoodActivity.class));
        });

        btnSpots.setOnClickListener(view-> {
            startActivity(new Intent(getApplicationContext(), SpotsActivity.class));
        });

        btnCommutes.setOnClickListener(view-> {
            startActivity(new Intent(getApplicationContext(), CommuteActivity.class));
        });

        btnMap.setOnClickListener(view-> {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        });

        btnDial.setOnClickListener(view-> {
            startActivity(new Intent(getApplicationContext(), DialActivity.class));
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Alert")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
                                //logout user
                                sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.remove("EMAIL");
                                editor.remove("USER_ID");
                                editor.putBoolean(LOGIN_STATUS, false);
                                editor.commit();

                                Toast.makeText(HomeActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                                finish();
                            }
                        }).create().show();
            }
        });
    }

}