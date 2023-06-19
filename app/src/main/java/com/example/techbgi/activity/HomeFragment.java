package com.example.techbgi.activity;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.techbgi.adapter.CategoryAdapter;
import com.example.techbgi.R;
import com.example.techbgi.databinding.FragmentHomeBinding;
import com.example.techbgi.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentHomeBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);

        ArrayList<CategoryModel> categories = new ArrayList<>();
        categories.add(new CategoryModel(1,"Web","https://cdn-icons-png.flaticon.com/512/870/870169.png"));
        categories.add(new CategoryModel(2,"Notes","https://cdn-icons-png.flaticon.com/512/1302/1302002.png"));
        categories.add(new CategoryModel(3,"Attendence","https://cdn-icons-png.flaticon.com/512/4388/4388285.png"));
        categories.add(new CategoryModel(4,"Time Table","https://cdn-icons-png.flaticon.com/512/1048/1048953.png"));
        categories.add(new CategoryModel(5,"KeepNotes","https://cdn-icons-png.flaticon.com/512/3699/3699808.png"));
        categories.add(new CategoryModel(6,"Top Zone","https://cdn-icons-png.flaticon.com/512/8901/8901532.png"));
        categories.add(new CategoryModel(7,"RGPV","https://cdn-icons-png.flaticon.com/512/8074/8074800.png"));
        categories.add(new CategoryModel(8,"Fees","https://cdn-icons-png.flaticon.com/512/2620/2620197.png"));
        categories.add(new CategoryModel(9,"Faculty","https://cdn-icons-png.flaticon.com/512/906/906175.png"));
        categories.add(new CategoryModel(10,"Companies","https://cdn-icons-png.flaticon.com/512/3061/3061341.png"));
        categories.add(new CategoryModel(11,"Test Room","https://cdn-icons-png.flaticon.com/128/3403/3403504.png"));
        categories.add(new CategoryModel(12,"Map","https://cdn-icons-png.flaticon.com/512/854/854894.png"));
        categories.add(new CategoryModel(13,"Campus","https://cdn-icons-png.flaticon.com/512/5205/5205264.png"));
        categories.add(new CategoryModel(14,"Previous Paper","https://cdn-icons-png.flaticon.com/512/3908/3908574.png"));
        categories.add(new CategoryModel(15,"Coming Soon","https://cdn-icons-png.flaticon.com/512/5167/5167425.png"));


        CategoryAdapter adapter = new CategoryAdapter(getContext(),categories);
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