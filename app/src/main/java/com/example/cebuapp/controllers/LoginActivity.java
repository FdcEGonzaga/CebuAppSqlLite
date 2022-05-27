package com.example.cebuapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cebuapp.R;
import com.example.cebuapp.model.DatabaseHelper;
import com.example.cebuapp.model.HelperUtilities;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MY_PREFS";
    public static final String EMAIL = "EMAIL";
    public static final String USER_ID = "ID";
    public static final String LOGIN_STATUS = "LOGGED_IN";
    public static SharedPreferences sharedPreferences;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView txtLoginError;
    private boolean isValid;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private int userID;
    private int accountID;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, 0);
        Boolean loggedIn = sharedPreferences.getBoolean(LOGIN_STATUS, false);//login status

        //checks the login status and redirects to the main activity
        if (loggedIn) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        TextView linkRegister = (TextView) findViewById(R.id.linkregister);

        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        txtLoginError = (TextView) findViewById(R.id.txtloginerror);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    private void attemptLogin() {
        try {

            databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            isValid = isValidUserInput();

            String filteredEmail = HelperUtilities.filter(inputEmail.getText().toString());
            String filteredPassword = HelperUtilities.filter(inputPassword.getText().toString());

            if (isValid) {

                cursor = DatabaseHelper.login(db, filteredEmail, filteredPassword);

                if (cursor != null && cursor.getCount() == 1) {
                    cursor.moveToFirst();

                    String email = cursor.getString(2);
                    userID = cursor.getInt(0);

                    //Toast.makeText(getApplicationContext(), "client id " + String.valueOf(clientID), Toast.LENGTH_SHORT).show();

                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(EMAIL, email);
                    editor.putInt(USER_ID, userID);
                    editor.putBoolean(LOGIN_STATUS, true);

                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    txtLoginError.setText("Invalid email or password");
                }
            }

        } catch (SQLiteException ex) {
            Log.d("LOGIN", "MY ERROR: " + ex);
            Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isValidUserInput() {
        if (HelperUtilities.isEmptyOrNull(inputEmail.getText().toString())) {
            txtLoginError.setText("Invalid email or password");
            return false;
        }
        if (HelperUtilities.isEmptyOrNull(inputPassword.getText().toString())) {
            txtLoginError.setText("Invalid email or password");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (cursor != null) {
                cursor.close();
            }

            if (db != null) {
                db.close();
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error closing database or cursor", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
    }

}