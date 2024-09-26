package com.example.dogsnutrition.ui.educationalcontent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsnutrition.R;

import java.util.ArrayList;
import java.util.List;

public class EducationalContentFragment extends Fragment {

    private RecyclerView educationalContentRecyclerView;
    private EducationalContentAdapter adapter;
    private List<EducationalContent> educationalContentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_educational_content, container, false);

        educationalContentRecyclerView = view.findViewById(R.id.educationalContentRecyclerView);
        educationalContentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample static data with drawable resources
        educationalContentList = new ArrayList<>();
        educationalContentList.add(new EducationalContent("Healthy Dog Diets", "Learn about balanced diets for dogs.", R.drawable.edu1));
        educationalContentList.add(new EducationalContent("Puppy Nutrition Tips", "Important tips for feeding puppies.", R.drawable.edu2));
        educationalContentList.add(new EducationalContent("Senior Dog Health", "Nutritional needs of senior dogs.", R.drawable.edu3));
        educationalContentList.add(new EducationalContent("Dog Food Brands", "Best brands for dog food.", R.drawable.edu4));

        adapter = new EducationalContentAdapter(educationalContentList);
        educationalContentRecyclerView.setAdapter(adapter);

        return view;
    }
}
