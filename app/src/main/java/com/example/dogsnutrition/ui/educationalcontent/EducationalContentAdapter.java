package com.example.dogsnutrition.ui.educationalcontent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsnutrition.R;

import java.util.List;

public class EducationalContentAdapter extends RecyclerView.Adapter<EducationalContentAdapter.EducationalContentViewHolder> {

    private List<EducationalContent> educationalContentList;

    public EducationalContentAdapter(List<EducationalContent> educationalContentList) {
        this.educationalContentList = educationalContentList;
    }

    @NonNull
    @Override
    public EducationalContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_educational_content, parent, false);
        return new EducationalContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationalContentViewHolder holder, int position) {
        EducationalContent content = educationalContentList.get(position);
        holder.titleTextView.setText(content.getTitle());
        holder.descriptionTextView.setText(content.getDescription());
        holder.imageView.setImageResource(content.getImageResId());
    }

    @Override
    public int getItemCount() {
        return educationalContentList.size();
    }

    public static class EducationalContentViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        ImageView imageView;

        public EducationalContentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
