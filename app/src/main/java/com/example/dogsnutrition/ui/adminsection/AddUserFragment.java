package com.example.dogsnutrition.ui.adminsection;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dogsnutrition.DatabaseHelper;
import com.example.dogsnutrition.R;

public class AddUserFragment extends Fragment {

    private EditText emailEditText, passwordEditText, nameEditText, addressEditText;
    private Switch isAdminSwitch;
    private Button addButton;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        dbHelper = new DatabaseHelper(getContext());

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        isAdminSwitch = view.findViewById(R.id.isAdminSwitch);
        addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        return view;
    }

    private void addUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        boolean isAdmin = isAdminSwitch.isChecked();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            if (dbHelper.insertUser(email, password, name, address, isAdmin)) {
                Toast.makeText(getContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                emailEditText.setText("");
                passwordEditText.setText("");
                nameEditText.setText("");
                addressEditText.setText("");
                isAdminSwitch.setChecked(false);
            } else {
                Toast.makeText(getContext(), "Failed to add user", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
