package com.example.dogsnutrition.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class ProfileFragment extends Fragment {

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private Button updateButton;
    private DatabaseHelper dbHelper;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        updateButton = view.findViewById(R.id.updateButton);
        dbHelper = new DatabaseHelper(getContext());

        loadUserProfile();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        return view;
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId != -1) {
            currentUser = dbHelper.getUserById(userId);
            if (currentUser != null) {
                nameEditText.setText(currentUser.getName());
                addressEditText.setText(currentUser.getAddress());
                emailEditText.setText(currentUser.getEmail());
            } else {
                Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserProfile() {
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (name.isEmpty() || address.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            currentUser.setName(name);
            currentUser.setAddress(address);
            currentUser.setEmail(email);
            boolean isUpdated = dbHelper.updateUser(currentUser);
            if (isUpdated) {
                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Profile update failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
