package com.example.techbgi.activity;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.techbgi.adapter.CategoryAdapter;
import com.example.techbgi.R;
import com.example.techbgi.adapter.FacultyCategoryAdapter;
import com.example.techbgi.databinding.FragmentFacultyHomeBinding;
import com.example.techbgi.databinding.FragmentHomeBinding;
import com.example.techbgi.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;


public class FacultyHomeFragment extends Fragment {

    public FacultyHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentFacultyHomeBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFacultyHomeBinding.inflate(inflater,container,false);

        ArrayList<CategoryModel> categories = new ArrayList<>();
        categories.add(new CategoryModel(1,"Faculties","https://cdn-icons-png.flaticon.com/512/906/906175.png"));
        categories.add(new CategoryModel(2,"Add Notice","https://cdn-icons-png.flaticon.com/512/8028/8028210.png"));
        categories.add(new CategoryModel(3,"Attendance","https://cdn-icons-png.flaticon.com/512/4388/4388285.png"));
        categories.add(new CategoryModel(4,"Set TimeTable","https://cdn-icons-png.flaticon.com/512/1048/1048953.png"));
        categories.add(new CategoryModel(5,"Upload Marks","https://cdn-icons-png.flaticon.com/512/9913/9913544.png"));
        categories.add(new CategoryModel(6,"Add Companies","https://cdn-icons-png.flaticon.com/512/3061/3061341.png"));
        categories.add(new CategoryModel(7,"KeepNotes","https://cdn-icons-png.flaticon.com/512/3699/3699808.png"));
        categories.add(new CategoryModel(8,"Upload Notes","https://cdn-icons-png.flaticon.com/512/1302/1302002.png"));
        categories.add(new CategoryModel(9,"Add Questions","https://cdn-icons-png.flaticon.com/512/7688/7688697.png"));
        categories.add(new CategoryModel(10,"Coming Soon","https://cdn-icons-png.flaticon.com/512/5167/5167425.png"));


        FacultyCategoryAdapter adapter = new FacultyCategoryAdapter(getContext(),categories);
        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);

        final List<SlideModel> arrayList = new ArrayList<>();

        arrayList.add(new SlideModel(R.drawable.bansal1, ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.bansal2, ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.bansal3, ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.bansal4, ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.bansal5, ScaleTypes.FIT));
        arrayList.add(new SlideModel(R.drawable.bansal6, ScaleTypes.FIT));

        binding.imageSlider.setImageList(arrayList);


        binding.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = binding.searchEdt.getText().toString();
                searchNet(term);
            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    private void searchNet(String words)
    {
        try {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY,words);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
            searchNetCompact(words);
        }
    }
    //search internet with the browser if there's no search app
    private void searchNetCompact(String words){
        try{
            Uri uri = Uri.parse("https://www.google.com/#q=" + words);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        }catch (ActivityNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
        }
    }
}