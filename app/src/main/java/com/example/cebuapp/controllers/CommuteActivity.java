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
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cebuapp.R;
import com.example.cebuapp.model.DatabaseHelper;

public class CommuteActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Intent intent;
    private ProgressDialog dialog;
    private ListView commutelist;
    private TextView noCommuteMsg;
    private int userID, spotID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commute);

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);

        userID = userID();
        castComponents();
        noCommuteMsg.setVisibility(View.INVISIBLE);

        dialog.setTitle("Fetchiing Cebu's commuting spots.");
        getCommmutesList();
    }

    private void castComponents() {
        commutelist = findViewById(R.id.commutelist);
        noCommuteMsg = findViewById(R.id.noCommuteMsg);
    }


    private void getCommmutesList() {
        try{
            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            cursor = DatabaseHelper.selectCommuteById(db);

            if (cursor != null && cursor.getCount() > 0) {
                CursorAdapter listAdapter = new SimpleCursorAdapter(getApplicationContext(),
                        R.layout.custom_commute_item,
                        cursor,
                        new String[]{"COMTITLE", "COMDESCRIPTION"},
                        new int[]{R.id.listTitle, R.id.listDesc}, 0);
                noCommuteMsg.setVisibility(View.GONE);
                commutelist.setVisibility(View.VISIBLE);
                commutelist.setAdapter(listAdapter);
            } else {
                commutelist.setVisibility(View.GONE);
                noCommuteMsg.setVisibility(View.VISIBLE);
            }
            dialog.dismiss();


        } catch (SQLiteException e){
            Toast.makeText(getApplicationContext(), "Database unavailable for commutes", Toast.LENGTH_SHORT).show();
        }
    }

    public int userID() {
        LoginActivity.sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        userID = LoginActivity.sharedPreferences.getInt(LoginActivity.USER_ID, 0);
        return userID;
    }
}