package com.example.dogsnutrition.ui.adminsection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsnutrition.R;
import com.example.dogsnutrition.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private Context context;
    private List<User> userList;
    private List<User> userListFull;
    private OnUserActionListener listener;

    public interface OnUserActionListener {
        void onUpdate(User user);
        void onDelete(User user);
    }

    public UsersAdapter(Context context, List<User> userList, OnUserActionListener listener) {
        this.context = context;
        this.userList = userList;
        this.userListFull = new ArrayList<>(userList); // copy original list for filtering
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.emailTextView.setText(user.getEmail());
        holder.nameTextView.setText(user.getName());
        holder.addressTextView.setText(user.getAddress());
        holder.isAdminTextView.setText(user.isAdmin() ? "Admin" : "User");

        holder.updateButton.setOnClickListener(v -> listener.onUpdate(user));
        holder.deleteButton.setOnClickListener(v -> listener.onDelete(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void filter(String text) {
        userList.clear();
        if (text.isEmpty()) {
            userList.addAll(userListFull);
        } else {
            text = text.toLowerCase();
            for (User user : userListFull) {
                if (user.getName().toLowerCase().contains(text) || user.getEmail().toLowerCase().contains(text)) {
                    userList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView, nameTextView, addressTextView, isAdminTextView;
        Button updateButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            isAdminTextView = itemView.findViewById(R.id.isAdminTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
