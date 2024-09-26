package com.example.dogsnutrition.ui.productcatalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dogsnutrition.DatabaseHelper;
import com.example.dogsnutrition.R;
import com.example.dogsnutrition.models.Product;
import com.squareup.picasso.Picasso;

public class ProductDetailFragment extends Fragment {

    private TextView nameTextView, priceTextView, descriptionTextView, brandTextView, typeTextView, ageTextView;
    private ImageView productImageView;
    private Button addToCartButton;
    private DatabaseHelper dbHelper;
    private Product product;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        nameTextView = view.findViewById(R.id.nameTextView);
        priceTextView = view.findViewById(R.id.priceTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        brandTextView = view.findViewById(R.id.brandTextView);
        typeTextView = view.findViewById(R.id.typeTextView);
        ageTextView = view.findViewById(R.id.ageTextView);
        productImageView = view.findViewById(R.id.productImageView);
        addToCartButton = view.findViewById(R.id.addToCartButton);

        dbHelper = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable("product");
            loadProductDetails();
        }

        addToCartButton.setOnClickListener(v -> {
            if (product != null) {
                int quantity = dbHelper.getCartItemQuantity(product.getId());
                if (quantity == 0) {
                    dbHelper.addCartItem(product.getId(), 1);
                    Toast.makeText(getContext(), "Done add to cart", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.updateCartItemQuantity(product.getId(), quantity + 1);
                    Toast.makeText(getContext(), "Item Already Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void loadProductDetails() {
        if (product != null) {
            nameTextView.setText(product.getName());
            priceTextView.setText(String.format("$%.2f", Double.parseDouble(product.getPrice())));
            descriptionTextView.setText(product.getDescription());
            brandTextView.setText(product.getBrand());
            typeTextView.setText(product.getType());
            ageTextView.setText(product.getAge());

            // Load the image using Picasso
            Picasso.get().load(product.getImageUri()).into(productImageView);
        }
    }
}
