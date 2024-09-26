package com.example.dogsnutrition.ui.adminsection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dogsnutrition.DatabaseHelper;
import com.example.dogsnutrition.R;
import com.example.dogsnutrition.models.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateProductFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nameEditText, descriptionEditText, priceEditText, brandEditText, typeEditText, ageEditText;
    private ImageView productImageView;
    private Button updateButton, selectImageButton;
    private DatabaseHelper dbHelper;
    private Product product;
    private Bitmap selectedBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_product, container, false);

        dbHelper = new DatabaseHelper(getContext());

        nameEditText = view.findViewById(R.id.nameEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        brandEditText = view.findViewById(R.id.brandEditText);
        typeEditText = view.findViewById(R.id.typeEditText);
        ageEditText = view.findViewById(R.id.ageEditText);
        productImageView = view.findViewById(R.id.productImageView);
        updateButton = view.findViewById(R.id.updateButton);
        selectImageButton = view.findViewById(R.id.selectImageButton);

        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable("product");
            loadProductDetails();
        }

        selectImageButton.setOnClickListener(v -> openImagePicker());

        updateButton.setOnClickListener(v -> updateProduct());

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                productImageView.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadProductDetails() {
        if (product != null) {
            nameEditText.setText(product.getName());
            descriptionEditText.setText(product.getDescription());
            priceEditText.setText(String.valueOf(product.getPrice()));
            brandEditText.setText(product.getBrand());
            typeEditText.setText(product.getType());
            ageEditText.setText(product.getAge());
            if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
                productImageView.setImageURI(Uri.parse(product.getImageUri()));
            }
        }
    }

    private void updateProduct() {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String brand = brandEditText.getText().toString();
        String type = typeEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String imageUri = product.getImageUri();

        if (selectedBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageUri = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), selectedBitmap, "Product Image", null);
        }

        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || brand.isEmpty() || type.isEmpty() || age.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setBrand(brand);
            product.setType(type);
            product.setAge(age);
            product.setImageUri(imageUri);

            boolean isUpdated = dbHelper.updateProduct(product);

            if (isUpdated) {
                Toast.makeText(getContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to update product", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
