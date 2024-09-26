package com.example.dogsnutrition.ui.home;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsnutrition.DatabaseHelper;
import com.example.dogsnutrition.R;
import com.example.dogsnutrition.models.Product;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DatabaseHelper(getContext());

        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        // Fetch data from database
        productList = dbHelper.getAllProducts();

        adapter = new ProductAdapter(productList);
        productsRecyclerView.setAdapter(adapter);

        return view;
    }
}
