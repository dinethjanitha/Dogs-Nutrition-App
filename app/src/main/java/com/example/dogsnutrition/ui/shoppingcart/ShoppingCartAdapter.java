package com.example.dogsnutrition.ui.shoppingcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsnutrition.R;
import com.example.dogsnutrition.models.ShoppingCartItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> {

    private Context context;
    private List<ShoppingCartItem> shoppingCartItems;
    private OnShoppingCartItemListener listener;

    public interface OnShoppingCartItemListener {
        void onQuantityChange(ShoppingCartItem item, int newQuantity);
        void onRemoveItem(ShoppingCartItem item);
    }

    public ShoppingCartAdapter(Context context, List<ShoppingCartItem> shoppingCartItems, OnShoppingCartItemListener listener) {
        this.context = context;
        this.shoppingCartItems = shoppingCartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_item, parent, false);
        return new ShoppingCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        ShoppingCartItem item = shoppingCartItems.get(position);

        holder.productNameTextView.setText(item.getProduct().getName());
        holder.productPriceTextView.setText(String.format("$%.2f", Double.parseDouble(item.getProduct().getPrice())));
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
        Picasso.get().load(item.getProduct().getImageUri()).into(holder.productImageView);

        holder.increaseQuantityButton.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            item.setQuantity(newQuantity);
            holder.quantityTextView.setText(String.valueOf(newQuantity));
            listener.onQuantityChange(item, newQuantity);
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
                holder.quantityTextView.setText(String.valueOf(newQuantity));
                listener.onQuantityChange(item, newQuantity);
            }
        });

        holder.removeButton.setOnClickListener(v -> listener.onRemoveItem(item));
    }

    @Override
    public int getItemCount() {
        return shoppingCartItems.size();
    }

    public static class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView, productPriceTextView, quantityTextView;
        ImageView productImageView;
        ImageView increaseQuantityButton, decreaseQuantityButton, removeButton;

        public ShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
