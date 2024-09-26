package com.example.dogsnutrition;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText addressEditText;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        registerButton = findViewById(R.id.registerButton);

        dbHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(false);  // Change to true if you want to create an admin user
            }
        });
    }

    private void registerUser(boolean isAdmin) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || address.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Check if email already exists
            if (isEmailExists(email)) {
                Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save user data to database
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
            values.put(DatabaseHelper.COLUMN_USER_PASSWORD, password);
            values.put(DatabaseHelper.COLUMN_USER_NAME, name);
            values.put(DatabaseHelper.COLUMN_USER_ADDRESS, address);
            values.put(DatabaseHelper.COLUMN_USER_IS_ADMIN, 0);  // Set isAdmin flag

            long newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values);

            if (newRowId != -1) {
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                // Navigate to LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = { DatabaseHelper.COLUMN_USER_EMAIL };
        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}