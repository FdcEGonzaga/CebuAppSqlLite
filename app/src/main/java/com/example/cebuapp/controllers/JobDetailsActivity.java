package com.example.cebuapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cebuapp.R;
import com.example.cebuapp.model.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JobDetailsActivity extends AppCompatActivity {
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Intent intent;
    private int jobID, userID;
    private ImageView jobDetailImg;
    private TextView jobDetailTitle, jobDetailCompany, jobDetailYears, jobDetailField, jobDetailDate, jobDetailSalary, jobDetailDesc;
    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        castComponents();
        getCurrentDate();
        intent = getIntent();
        jobID = intent.getIntExtra("JOBID",0);
        userID = userID();

        displaySelectedJob(jobID);
    }

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy H:m", Locale.getDefault());
        formattedDate = df.format(c);
    }

    private void castComponents() {
        jobDetailImg = findViewById(R.id.jobDetailImg);
        jobDetailTitle = findViewById(R.id.jobDetailTitle);
        jobDetailYears = findViewById(R.id.jobDetailYears);
        jobDetailDesc = findViewById(R.id.jobDetailDesc);
        jobDetailSalary = findViewById(R.id.jobDetailSalary);
        jobDetailField = findViewById(R.id.jobDetailField);
        jobDetailCompany = findViewById(R.id.jobDetailCompany);
        jobDetailDate = findViewById(R.id.jobDetailDate);
    }


    private void displaySelectedJob(int jobID) {
        try {
            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            cursor = DatabaseHelper.selectJobById(db, jobID);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                jobDetailTitle.setText(""+ cursor.getString(1));
                jobDetailYears.setText("At least "+ cursor.getString(2) + " experience");
                jobDetailDesc.setText(""+ cursor.getString(3));
                jobDetailSalary.setText("Salary offer is up to "+ cursor.getString(4));
                jobDetailCompany.setText(""+ cursor.getString(5));
                jobDetailField.setText(""+ cursor.getString(6));
                jobDetailDate.setText(formattedDate);
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