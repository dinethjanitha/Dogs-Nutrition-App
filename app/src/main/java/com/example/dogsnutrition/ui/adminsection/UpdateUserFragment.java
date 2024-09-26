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
import com.example.dogsnutrition.models.User;

public class UpdateUserFragment extends Fragment {

    private EditText emailEditText, nameEditText, addressEditText;
    private Switch isAdminSwitch;
    private Button updateButton;
    private DatabaseHelper dbHelper;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_user, container, false);

        dbHelper = new DatabaseHelper(getContext());

        emailEditText = view.findViewById(R.id.emailEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        isAdminSwitch = view.findViewById(R.id.isAdminSwitch);
        updateButton = view.findViewById(R.id.updateButton);

        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
            loadUserDetails();
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        return view;
    }

    private void loadUserDetails() {
        if (user != null) {
            emailEditText.setText(user.getEmail());
            nameEditText.setText(user.getName());
            addressEditText.setText(user.getAddress());
            isAdminSwitch.setChecked(user.isAdmin());
        }
    }

    private void updateUser() {
        String email = emailEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        boolean isAdmin = isAdminSwitch.isChecked();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            user.setEmail(email);
            user.setName(name);
            user.setAddress(address);
            user.setAdmin(isAdmin);

            if (dbHelper.updateUser(user)) {
                Toast.makeText(getContext(), "User updated successfully", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.nav_manage_users);
            } else {
                Toast.makeText(getContext(), "Failed to update user", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
