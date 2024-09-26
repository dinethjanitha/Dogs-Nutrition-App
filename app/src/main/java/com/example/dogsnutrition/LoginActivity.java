package com.example.dogsnutrition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    if (email.equals("AdminRoot") && password.equals("1234")) {
                        Toast.makeText(LoginActivity.this, "Admin login successful", Toast.LENGTH_SHORT).show();
                        saveUserInfo(email, "Admin", true,10000001);
                        navigateToHome();
                    } else {
                        loginUser(email, password);

                    }
                }
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        SQLiteOpenHelper db = dbHelper;

        String[] projection = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_PASSWORD,
                DatabaseHelper.COLUMN_USER_NAME,
                DatabaseHelper.COLUMN_USER_IS_ADMIN
        };

        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.getReadableDatabase().query(
                DatabaseHelper.TABLE_USERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") boolean isAdmin = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_IS_ADMIN)) > 0;
            @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_NAME));
            @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID));
            if (isAdmin) {
                Toast.makeText(LoginActivity.this, "Admin login successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            }
            saveUserInfo(email, userName, isAdmin, userId);
            navigateToHome();
        } else {
            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void saveUserInfo(String email, String name, boolean isAdmin, int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userEmail", email);
        editor.putString("userName", name);
        editor.putBoolean("isAdmin", isAdmin);
        editor.putInt("userId", userId);
        editor.apply();
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
