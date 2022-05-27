package com.example.cebuapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cebuapp.R;
import com.example.cebuapp.model.DatabaseHelper;

public class FoodDetailsActivity extends AppCompatActivity {

    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Intent intent;
    private int foodID, userID;
    private TextView detailTitle, detailLocation, detailDate, detailContact, detailDescription;
    private Button contactFoodBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        castComponents();
        intent = getIntent();
        foodID = intent.getIntExtra("FOODID",0);
        userID = userID();

        displaySelectedFood(foodID);
    }

    private void castComponents() {
        detailTitle = findViewById(R.id.detailTitle);
        detailLocation = findViewById(R.id.detailLocation);
        detailDate = findViewById(R.id.detailDate);
        detailContact = findViewById(R.id.detailContact);
        detailDescription = findViewById(R.id.detailDescription);
        contactFoodBtn = findViewById(R.id.contactFoodBtn);
    }


    private void displaySelectedFood(int foodID) {
        try {
            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            cursor = DatabaseHelper.selectFoodById(db, foodID);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                detailTitle.setText(""+ cursor.getString(1));
                detailDescription.setText(""+ cursor.getString(2));
                detailLocation.setText(""+ cursor.getString(3));
                detailDate.setText(""+ cursor.getString(4));
                detailContact.setText(""+ cursor.getString(5));


                contactFoodBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getApplicationContext(), DialActivity.class);
                        intent.putExtra("CONTACTNUMBER",  detailContact.getText());
                        startActivity(intent);
                    }
                });
            }

        } catch (SQLiteException ex) {
            Toast.makeText(getApplicationContext(), "Error displaying of job ID " + ex , Toast.LENGTH_LONG).show();
        }
    }

    public int userID() {
        LoginActivity.sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        userID = LoginActivity.sharedPreferences.getInt(LoginActivity.USER_ID, 0);
        return userID;
    }
}