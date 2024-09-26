package com.example.dogsnutrition.ui.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsnutrition.DatabaseHelper;
import com.example.dogsnutrition.R;
import com.example.dogsnutrition.models.Product;
import com.example.dogsnutrition.models.ShoppingCartItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartFragment extends Fragment {

    private RecyclerView shoppingCartRecyclerView;
    private ShoppingCartAdapter adapter;
    private List<ShoppingCartItem> shoppingCartItems;
    private TextView totalAmountTextView;
    private Button checkoutButton;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        shoppingCartRecyclerView = view.findViewById(R.id.shoppingCartRecyclerView);
        totalAmountTextView = view.findViewById(R.id.totalAmountTextView);
        checkoutButton = view.findViewById(R.id.checkoutButton);
        dbHelper = new DatabaseHelper(getContext());

        // Load cart items from database
        loadCartItems();

        adapter = new ShoppingCartAdapter(getContext(), shoppingCartItems, new ShoppingCartAdapter.OnShoppingCartItemListener() {
            @Override
            public void onQuantityChange(ShoppingCartItem item, int newQuantity) {
                dbHelper.updateCartItemQuantity(item.getProduct().getId(), newQuantity);
                updateTotalAmount();
            }

            @Override
            public void onRemoveItem(ShoppingCartItem item) {
                dbHelper.removeCartItem(item.getProduct().getId());
                shoppingCartItems.remove(item);
                adapter.notifyDataSetChanged();
                updateTotalAmount();
            }
        });

        shoppingCartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shoppingCartRecyclerView.setAdapter(adapter);

        updateTotalAmount();

        checkoutButton.setOnClickListener(v -> {
            // Implement checkout functionality here
        });

        return view;
    }

    private void loadCartItems() {
        shoppingCartItems = new ArrayList<>();
        List<Product> products = dbHelper.getAllProducts();
        for (Product product : products) {
            int quantity = dbHelper.getCartItemQuantity(product.getId());
            if (quantity > 0) {
                shoppingCartItems.add(new ShoppingCartItem(product, quantity));
            }
        }
    }

    private void updateTotalAmount() {
        double total = 0;
//        double price = ;
        for (ShoppingCartItem item : shoppingCartItems) {
            total += Double.parseDouble(item.getProduct().getPrice()) * item.getQuantity();
        }
        totalAmountTextView.setText(String.format("$%.2f", total));
    }
}
