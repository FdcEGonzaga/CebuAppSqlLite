package com.example.cebuapp.controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cebuapp.R;
import com.example.cebuapp.model.DatabaseHelper;
import com.example.cebuapp.model.HelperUtilities;

public class RegistrationActivity extends AppCompatActivity {

    private int clientID;
    private EditText inputFullname;
    private EditText inputEmail;
    private EditText inputCreditCard;
    private EditText inputPhone;
    private EditText inputConfirmPassword;
    private EditText inputPassword;
    private boolean isValid;
    private SQLiteOpenHelper hospitalDatabaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button register = (Button) findViewById(R.id.btnRegister);
        TextView linkLogin = (TextView) findViewById(R.id.linklogin);


        inputFullname = (EditText) findViewById(R.id.txtfullname);
        inputEmail = (EditText) findViewById(R.id.txtEmail);
        inputPhone = (EditText) findViewById(R.id.txtPhone);
        inputCreditCard = (EditText) findViewById(R.id.txtCreditCard);
        inputPassword = (EditText) findViewById(R.id.txtPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerEmployee();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerEmployee() {

        try {


            hospitalDatabaseHelper = new DatabaseHelper(getApplicationContext());
            db = hospitalDatabaseHelper.getWritableDatabase();

            cursor = DatabaseHelper.selectUser(db, HelperUtilities.filter(inputEmail.getText().toString()));

            isValid = isValidUserInput();


            if (isValid) {

                if (cursor != null && cursor.getCount() > 0) {

                    accountExistsAlert().show();

                } else {

                    DatabaseHelper.insertUsers(db,
                            inputFullname.getText().toString(),
                            inputEmail.getText().toString(),
                            inputPhone.getText().toString(),
                            inputCreditCard.getText().toString(),
                            inputPassword.getText().toString());

                    cursor = DatabaseHelper.selectUserID(db,
                            inputFullname.getText().toString(),
                            inputPhone.getText().toString(),
                            inputCreditCard.getText().toString());

                    // if user is already saved to DB
                    if (cursor != null && cursor.getCount() == 1) {
                        registrationSuccessDialog().show();
                    }

                }

            }


        } catch (SQLiteException ex) {
            Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    private Dialog accountExistsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("An account with this email already exists. Please try again. ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    private Dialog registrationSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your profile created successfully! ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        return builder.create();
    }

    private boolean isValidUserInput() {
        if (HelperUtilities.isEmptyOrNull(inputFullname.getText().toString())) {
            inputFullname.setError("Please enter your first name");
            return false;
        } else if (!HelperUtilities.isString(inputFullname.getText().toString())) {
            inputFullname.setError("Please enter a valid first name");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputEmail.getText().toString())) {
            inputEmail.setError("Please enter your email");
            return false;
        } else if (!HelperUtilities.isValidEmail(inputEmail.getText().toString())) {
            inputEmail.setError("Please enter a valid email");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputPhone.getText().toString())) {
            inputPhone.setError("Please enter your phone");
            return false;
        } else if (!HelperUtilities.isValidPhone(inputPhone.getText().toString())) {
            inputPhone.setError("Please enter a valid phone");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputCreditCard.getText().toString())) {
            inputCreditCard.setError("Please enter your credit card number");
            return false;
        } else if (!HelperUtilities.isValidCreditCard(inputCreditCard.getText().toString())) {
            inputCreditCard.setError("Please enter a valid credit card number");
            return false;
        }
        if (HelperUtilities.isEmptyOrNull(inputPassword.getText().toString())) {
            inputPassword.setError("Please enter your password");
            return false;
        } else if (HelperUtilities.isShortPassword(inputPassword.getText().toString())) {
            inputPassword.setError("Your password must have at least 6 characters.");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(inputConfirmPassword.getText().toString())) {
            inputConfirmPassword.setError("Please confirm password");
            return false;
        }

        if (!(inputConfirmPassword.getText().toString().equals(inputPassword.getText().toString()))) {

            inputConfirmPassword.setError("The password doesn't match.");
            return false;
        }


        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            cursor.close();
            db.close();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error closing database or cursor", Toast.LENGTH_SHORT).show();
        }
    }
}