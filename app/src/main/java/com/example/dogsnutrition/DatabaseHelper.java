package com.example.dogsnutrition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.dogsnutrition.models.Product;
import com.example.dogsnutrition.models.ShoppingCartItem;
import com.example.dogsnutrition.models.User;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dogNutrition.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TAG = "DatabaseHelper";

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_ADDRESS = "address";
    public static final String COLUMN_USER_IS_ADMIN = "isAdmin";

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_BRAND = "brand";
    public static final String COLUMN_PRODUCT_TYPE = "type";
    public static final String COLUMN_PRODUCT_AGE = "age";
    public static final String COLUMN_PRODUCT_IMAGE_URI = "imageUri";

    public static final String TABLE_CART = "cart";
    public static final String COLUMN_CART_ID = "id";
    public static final String COLUMN_CART_PRODUCT_ID = "product_id";
    public static final String COLUMN_CART_QUANTITY = "quantity";

    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_EMAIL + " TEXT, " +
                    COLUMN_USER_PASSWORD + " TEXT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_USER_ADDRESS + " TEXT, " +
                    COLUMN_USER_IS_ADMIN + " INTEGER DEFAULT 0);";

    private static final String TABLE_PRODUCTS_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT, " +
                    COLUMN_PRODUCT_DESCRIPTION + " TEXT, " +
                    COLUMN_PRODUCT_PRICE + " TEXT, " +
                    COLUMN_PRODUCT_BRAND + " TEXT, " +
                    COLUMN_PRODUCT_TYPE + " TEXT, " +
                    COLUMN_PRODUCT_AGE + " TEXT, " +
                    COLUMN_PRODUCT_IMAGE_URI + " TEXT);";

    private static final String TABLE_CART_CREATE =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CART_PRODUCT_ID + " INTEGER, " +
                    COLUMN_CART_QUANTITY + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USERS_CREATE);
        db.execSQL(TABLE_PRODUCTS_CREATE);
        db.execSQL(TABLE_CART_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public boolean insertUser(String email, String password, String name, String address, boolean isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_EMAIL, email);
        contentValues.put(COLUMN_USER_PASSWORD, password);
        contentValues.put(COLUMN_USER_NAME, name);
        contentValues.put(COLUMN_USER_ADDRESS, address);
        contentValues.put(COLUMN_USER_IS_ADMIN, isAdmin ? 1 : 0);
        long result = db.insert(TABLE_USERS, null, contentValues);
        Log.d(TAG, "Inserted User: " + name + ", Result: " + result);
        return result != -1;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_EMAIL, user.getEmail());
        contentValues.put(COLUMN_USER_NAME, user.getName());
        contentValues.put(COLUMN_USER_ADDRESS, user.getAddress());
        contentValues.put(COLUMN_USER_IS_ADMIN, user.isAdmin() ? 1 : 0);
        int result = db.update(TABLE_USERS, contentValues, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
        Log.d(TAG, "Updated User: " + user.getName() + ", Result: " + result);
        return result > 0;
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USERS, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "Deleted User ID: " + id + ", Result: " + result);
        return result > 0;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ADDRESS));
                int isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_IS_ADMIN));

                User user = new User(id, email, name, address, isAdmin == 1);
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }


    public boolean insertProduct(String name, String description, String price, String brand, String type, String age, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_NAME, name);
        contentValues.put(COLUMN_PRODUCT_DESCRIPTION, description);
        contentValues.put(COLUMN_PRODUCT_PRICE, price);
        contentValues.put(COLUMN_PRODUCT_BRAND, brand);
        contentValues.put(COLUMN_PRODUCT_TYPE, type);
        contentValues.put(COLUMN_PRODUCT_AGE, age);
        contentValues.put(COLUMN_PRODUCT_IMAGE_URI, imageUri);
        long result = db.insert(TABLE_PRODUCTS, null, contentValues);
        Log.d(TAG, "Inserted Product: " + name + ", Result: " + result);
        return result != -1;
    }

    public boolean updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_NAME, product.getName());
        contentValues.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        contentValues.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        contentValues.put(COLUMN_PRODUCT_BRAND, product.getBrand());
        contentValues.put(COLUMN_PRODUCT_TYPE, product.getType());
        contentValues.put(COLUMN_PRODUCT_AGE, product.getAge());
        contentValues.put(COLUMN_PRODUCT_IMAGE_URI, product.getImageUri());
        int result = db.update(TABLE_PRODUCTS, contentValues, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getId())});
        Log.d(TAG, "Updated Product: " + product.getName() + ", Result: " + result);
        return result > 0;
    }

    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "Deleted Product ID: " + id + ", Result: " + result);
        return result > 0;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_BRAND));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_TYPE));
                String age = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_AGE));
                String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE_URI));

                Product product = new Product(id, name, description, price, brand, type, age, imageUri);
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }


    public boolean updateCartItemQuantity(int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CART_QUANTITY, quantity);
        int result = db.update(TABLE_CART, contentValues, COLUMN_CART_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        Log.d(TAG, "Updated Cart Item Quantity for Product ID: " + productId + ", Result: " + result);
        return result > 0;
    }

    public boolean removeCartItem(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CART, COLUMN_CART_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        Log.d(TAG, "Removed Cart Item for Product ID: " + productId + ", Result: " + result);
        return result > 0;
    }

    public ArrayList<ShoppingCartItem> getAllCartItems() {
        ArrayList<ShoppingCartItem> cartItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_PRODUCT_ID));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));

                // Assuming you have a method getProductById to fetch a product by its ID
                Product product = getProductById(productId);

                ShoppingCartItem cartItem = new ShoppingCartItem(product, quantity);
                cartItemList.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItemList;
    }

    public Product getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
            String brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_BRAND));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_TYPE));
            String age = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_AGE));
            String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE_URI));

            cursor.close();
            return new Product(id, name, description, price, brand, type, age, imageUri);
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public int getCartItemQuantity(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CART_QUANTITY + " FROM " + TABLE_CART + " WHERE " + COLUMN_CART_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
            cursor.close();
            return quantity;
        }
        if (cursor != null) {
            cursor.close();
        }
        return 0;
    }

    public boolean addCartItem(int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CART_PRODUCT_ID, productId);
        contentValues.put(COLUMN_CART_QUANTITY, quantity);
        long result = db.insert(TABLE_CART, null, contentValues);
        Log.d(TAG, "Added Cart Item: Product ID: " + productId + ", Quantity: " + quantity + ", Result: " + result);
        return result != -1;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Log.d(TAG, "Email Exists: " + email + ", Exists: " + exists);
        return exists;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ADDRESS));
            int isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_IS_ADMIN));

            cursor.close();
            Log.d(TAG, "Retrieved User by Email: " + email + ", Name: " + name);
            return new User(id, email, name, address, isAdmin == 1);
        }
        if (cursor != null) {
            cursor.close();
        }
        Log.d(TAG, "User not found by Email: " + email);
        return null;
    }

    // Get user by ID
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ADDRESS));
            boolean isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_IS_ADMIN)) > 0;

            cursor.close();
            Log.d(TAG, "Retrieved User by ID: " + userId + ", Name: " + name);
            return new User(id, email, name, address, isAdmin);
        }
        if (cursor != null) {
            cursor.close();
        }
        Log.d(TAG, "User not found by ID: " + userId);
        return null;
    }
}
