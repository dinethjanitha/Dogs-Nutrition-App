package com.example.dogsnutrition.ui.adminsection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsnutrition.DatabaseHelper;
import com.example.dogsnutrition.R;
import com.example.dogsnutrition.models.User;

import java.util.ArrayList;

public class ManageUsersFragment extends Fragment {

    private RecyclerView usersRecyclerView;
    private SearchView userSearchView;
    private DatabaseHelper dbHelper;
    private ArrayList<User> userList;
    private UsersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_users, container, false);

        dbHelper = new DatabaseHelper(getContext());
        usersRecyclerView = view.findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userSearchView = view.findViewById(R.id.userSearchView);
        userSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        loadUsers();

        return view;
    }

    private void loadUsers() {
        userList = dbHelper.getAllUsers();
        adapter = new UsersAdapter(getContext(), userList, new UsersAdapter.OnUserActionListener() {
            @Override
            public void onUpdate(User user) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_manage_users_to_update_user, bundle);
            }

            @Override
            public void onDelete(final User user) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dbHelper.deleteUser(user.getId())) {
                                    Toast.makeText(getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                                    loadUsers();
                                } else {
                                    Toast.makeText(getContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        usersRecyclerView.setAdapter(adapter);
    }
}
