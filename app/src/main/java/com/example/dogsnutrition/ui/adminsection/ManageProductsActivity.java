package com.example.dogsnutrition.ui.adminsection;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsnutrition.DatabaseHelper;
import com.example.dogsnutrition.R;
import com.example.dogsnutrition.models.Product;

import java.util.ArrayList;

public class ManageProductsActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private DatabaseHelper dbHelper;
    private ArrayList<Product> productList;
    private ProductsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        dbHelper = new DatabaseHelper(this);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();
    }

    private void loadProducts() {
        productList = dbHelper.getAllProducts();
        adapter = new ProductsAdapter(this, productList, new ProductsAdapter.OnProductActionListener() {
            @Override
            public void onUpdate(Product product) {
                // Implement your update functionality here if needed
                Toast.makeText(ManageProductsActivity.this, "Update clicked for " + product.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(final Product product) {
                new AlertDialog.Builder(ManageProductsActivity.this)
                        .setTitle("Delete Product")
                        .setMessage("Are you sure you want to delete this product?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dbHelper.deleteProduct(product.getId())) {
                                    Toast.makeText(ManageProductsActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                                    loadProducts();
                                } else {
                                    Toast.makeText(ManageProductsActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        productsRecyclerView.setAdapter(adapter);
    }
}
