package com.example.techbgi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.techbgi.R;
import com.example.techbgi.adapter.VPAdapter;
import com.example.techbgi.adapter.ViewPaggerItem;

import java.util.ArrayList;

public class CampusActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    ArrayList<ViewPaggerItem> viewPaggerItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager2 = findViewById(R.id.view_pager);
        int[] images = {R.drawable.campus,R.drawable.canteen,R.drawable.workshop,R.drawable.sports,R.drawable.college_bus,R.drawable.college_lib,R.drawable.conference};
        String[] heading = {"Campus","Canteen","Laboratry","Sports","Bus-Facility","Library","Conference-Room"};
        String[] desc = {
                getString(R.string.campus_desc),
                getString(R.string.canteen_desc),
                getString(R.string.workshop_desc),
                getString(R.string.sport_desc),
                getString(R.string.bus_desc),
                getString(R.string.library_desc),
                getString(R.string.conference_desc),
        };

        viewPaggerItemArrayList = new ArrayList<>();
        for(int i =0; i < images.length; i++)
        {
            ViewPaggerItem viewPaggerItem = new ViewPaggerItem(images[i],heading[i],desc[i]);
            viewPaggerItemArrayList.add(viewPaggerItem);

        }
        VPAdapter vpAdapter = new VPAdapter(viewPaggerItemArrayList);

        viewPager2.setAdapter(vpAdapter);

        viewPager2.setClipToPadding(false);

        viewPager2.setClipChildren(false);

        viewPager2.setOffscreenPageLimit(2);

        viewPager2.getChildAt(0).setOverScrollMode(viewPager2.OVER_SCROLL_NEVER);


    }
}