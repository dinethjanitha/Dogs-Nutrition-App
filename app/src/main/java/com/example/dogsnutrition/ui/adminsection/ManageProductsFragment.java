package com.example.dogsnutrition.ui.adminsection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.dogsnutrition.models.Product;

import java.util.ArrayList;

public class ManageProductsFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private DatabaseHelper dbHelper;
    private ArrayList<Product> productList;
    private ProductsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_products, container, false);

        dbHelper = new DatabaseHelper(getContext());
        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadProducts();

        return view;
    }

    private void loadProducts() {
        productList = dbHelper.getAllProducts();
        adapter = new ProductsAdapter(getContext(), productList, new ProductsAdapter.OnProductActionListener() {
            @Override
            public void onUpdate(Product product) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", product);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.updateProductFragment, bundle);
            }

            @Override
            public void onDelete(final Product product) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Product")
                        .setMessage("Are you sure you want to delete this product?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dbHelper.deleteProduct(product.getId())) {
                                    Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                                    loadProducts();
                                } else {
                                    Toast.makeText(getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
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
