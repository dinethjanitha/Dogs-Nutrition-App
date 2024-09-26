package com.example.dogsnutrition.ui.adminsection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dogsnutrition.R;

public class AdminSectionFragment extends Fragment {

    private Button addProductButton, manageProductsButton, addUserButton, manageUsersButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_admin_section, container, false);

        addProductButton = view.findViewById(R.id.addProductButton);
        manageProductsButton = view.findViewById(R.id.manageProductsButton);
        addUserButton = view.findViewById(R.id.addUserButton);
        manageUsersButton = view.findViewById(R.id.manageUsersButton);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_admin_to_add_product);
            }
        });

        manageProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_admin_to_manage_products);
            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_admin_to_add_user);
            }
        });

        manageUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_admin_to_manage_users);
            }
        });

        return view;
    }
}
