package com.example.dogsnutrition.ui.adminsection;

import android.app.Activity;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddProductFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nameEditText, descriptionEditText, priceEditText, brandEditText, typeEditText, ageEditText;
    private Button addButton, selectImageButton;
    private ImageView productImageView;
    private DatabaseHelper dbHelper;
    private Bitmap productImageBitmap;
    private Uri productImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        dbHelper = new DatabaseHelper(getContext());

        nameEditText = view.findViewById(R.id.nameEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        brandEditText = view.findViewById(R.id.brandEditText);
        typeEditText = view.findViewById(R.id.typeEditText);
        ageEditText = view.findViewById(R.id.ageEditText);
        productImageView = view.findViewById(R.id.productImageView);
        addButton = view.findViewById(R.id.addButton);
        selectImageButton = view.findViewById(R.id.selectImageButton);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            productImageUri = data.getData();
            try {
                productImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), productImageUri);
                productImageView.setImageBitmap(productImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Uri saveImageToInternalStorage(Bitmap bitmap) {
        File directory = getContext().getFilesDir();
        File imageFile = new File(directory, "product_image_" + System.currentTimeMillis() + ".png");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addProduct() {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String brand = brandEditText.getText().toString();
        String type = typeEditText.getText().toString();
        String age = ageEditText.getText().toString();

        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || brand.isEmpty() || type.isEmpty() || age.isEmpty() || productImageBitmap == null) {
            Toast.makeText(getContext(), "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
        } else {
            Uri imageUri = saveImageToInternalStorage(productImageBitmap);

            if (imageUri != null) {
                boolean isInserted = dbHelper.insertProduct(name, description, price, brand, type, age, imageUri.toString());

                if (isInserted) {
                    Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                    nameEditText.setText("");
                    descriptionEditText.setText("");
                    priceEditText.setText("");
                    brandEditText.setText("");
                    typeEditText.setText("");
                    ageEditText.setText("");
                    productImageView.setImageResource(R.drawable.ic_imgplc);
                } else {
                    Toast.makeText(getContext(), "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
