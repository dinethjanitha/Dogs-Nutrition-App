package com.example.dogsnutrition.ui.productcatalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductCatalogFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private SearchView productSearchView;
    private Spinner filterSpinner, sortSpinner;
    private DatabaseHelper dbHelper;
    private List<Product> productList;
    private ProductsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_catalog, container, false);

        dbHelper = new DatabaseHelper(getContext());
        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productSearchView = view.findViewById(R.id.productSearchView);
        filterSpinner = view.findViewById(R.id.filterSpinner);
        sortSpinner = view.findViewById(R.id.sortSpinner);

        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.filter_options, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_options, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        productSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getItemAtPosition(position).toString();
                applyFilter(filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sort = parent.getItemAtPosition(position).toString();
                applySort(sort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        loadProducts();

        return view;
    }

    private void loadProducts() {
        productList = dbHelper.getAllProducts();
        adapter = new ProductsAdapter(getContext(), productList, new ProductsAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", product);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_product_catalog_to_product_detail, bundle);
            }
        });
        productsRecyclerView.setAdapter(adapter);
    }

    private void applyFilter(String filter) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (filter.equals("All") || product.getType().equalsIgnoreCase(filter)) {
                filteredList.add(product);
            }
        }
        adapter = new ProductsAdapter(getContext(), filteredList, new ProductsAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", product);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_product_catalog_to_product_detail, bundle);
            }
        });
        productsRecyclerView.setAdapter(adapter);
    }

    private void applySort(String sort) {
        if (sort.equals("Price: Low to High")) {
            Collections.sort(productList, new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return Double.compare(Double.parseDouble(o1.getPrice()), Double.parseDouble(o2.getPrice()));
                }
            });
        } else if (sort.equals("Price: High to Low")) {
            Collections.sort(productList, new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return Double.compare(Double.parseDouble(o2.getPrice()), Double.parseDouble(o1.getPrice()));
                }
            });
        }
        adapter.notifyDataSetChanged();
    }

}
