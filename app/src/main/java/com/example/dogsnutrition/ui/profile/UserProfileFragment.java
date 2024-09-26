package com.example.dogsnutrition.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dogsnutrition.DatabaseHelper;
import com.example.dogsnutrition.R;
import com.example.dogsnutrition.models.User;

public class UserProfileFragment extends Fragment {

    private EditText nameEditText, addressEditText, emailEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        saveButton = view.findViewById(R.id.saveButton);
        dbHelper = new DatabaseHelper(getContext());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

        if (userEmail != null) {
            user = dbHelper.getUserByEmail(userEmail);
            if (user != null) {
                loadUserDetails();
            }
        }

        saveButton.setOnClickListener(v -> saveUserDetails());

        return view;
    }

    private void loadUserDetails() {
        if (user != null) {
            nameEditText.setText(user.getName());
            addressEditText.setText(user.getAddress());
            emailEditText.setText(user.getEmail());
        }
    }

    private void saveUserDetails() {
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.equals(user.getEmail()) && dbHelper.isEmailExists(email)) {
            Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setName(name);
        user.setAddress(address);
        user.setEmail(email);

        boolean isUpdated = dbHelper.updateUser(user);
        if (isUpdated) {
            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}
