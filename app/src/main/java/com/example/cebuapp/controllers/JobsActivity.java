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

public class JobsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Intent intent;
    private Button btnAll, btn1, btn2, btn3, btn4;
    private ProgressDialog dialog;
    private ListView jobsList;
    private TextView noJobsMsg;
    private int userID, jobID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, DatabaseHelper.JOBFIELDS);

        userID = userID();
        castComponents();
        noJobsMsg.setVisibility(View.INVISIBLE);

        // get jobs
        setBtnOnClicks();

        dialog.setTitle("Fetchiing all Cebu job posts.");
        getJobsList(0);
    }

    private void castComponents() {
        btnAll = findViewById(R.id.btnAll);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        jobsList = findViewById(R.id.jobslist);
        noJobsMsg = findViewById(R.id.noJobsMsg);
    }

    private void setBtnOnClicks() {
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing All jobs");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJobsList(0);

                    }
                }, 2000);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing IT related jobs");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJobsList(1);

                    }
                }, 2000);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing Engineering related jobs");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJobsList(2);

                    }
                }, 2000);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing Teaching related jobs");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJobsList(3);

                    }
                }, 2000);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Fetchiing Contractual related jobs");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJobsList(4);

                    }
                }, 2000);
            }
        });
    }

    private void getJobsList(int catID) {
        try{
            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            cursor = DatabaseHelper.selectJobCategoryById(db, catID);

            if (cursor != null && cursor.getCount() > 0) {
                CursorAdapter listAdapter = new SimpleCursorAdapter(getApplicationContext(),
                        R.layout.custom_list_item,
                        cursor,
                        new String[]{"JOBTITLE", "JOBYEAREXP", "JOBSALARY"},
                        new int[]{R.id.listTitle, R.id.listDesc1, R.id.listDesc2}, 0);
                noJobsMsg.setVisibility(View.GONE);
                jobsList.setVisibility(View.VISIBLE);
                jobsList.setAdapter(listAdapter);
            } else {
                jobsList.setVisibility(View.GONE);
                noJobsMsg.setVisibility(View.VISIBLE);
            }
            dialog.dismiss();

            // onclick of list
            jobsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    jobID = (int) id;
                    Toast.makeText(getApplicationContext(), "Viewing job ID " + String.valueOf(jobID) , Toast.LENGTH_SHORT).show();

                    intent = new Intent(getApplicationContext(), JobDetailsActivity.class);
                    intent.putExtra("JOBID",jobID);

                    startActivity(intent);
                }
            });

        } catch (SQLiteException e) {
            Toast.makeText(getApplicationContext(), "Database unavailable for jobs", Toast.LENGTH_SHORT).show();
        }
    }

    public int userID() {
        LoginActivity.sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        userID = LoginActivity.sharedPreferences.getInt(LoginActivity.USER_ID, 0);
        return userID;
    }
}