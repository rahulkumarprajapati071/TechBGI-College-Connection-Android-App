package com.example.techbgi.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.techbgi.R;
import com.example.techbgi.adapter.ClassMarksAdapter;
import com.example.techbgi.adapter.CompanyAdapter;
import com.example.techbgi.model.ClassItem;
import com.example.techbgi.model.CompanyModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ClassMarksFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ClassMarksAdapter adapter;

    RecyclerView recyclerclassmarks;

    public ClassMarksFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static ClassMarksFragment newInstance(String param1, String param2) {
        ClassMarksFragment fragment = new ClassMarksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_marks, container, false);

        recyclerclassmarks = (RecyclerView) view.findViewById(R.id.recyclerclassmarks);
        recyclerclassmarks.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<ClassItem> options =
                new FirebaseRecyclerOptions.Builder<ClassItem>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("classOfMarks").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())),ClassItem.class)
                        .build();

        adapter = new ClassMarksAdapter(options);
        recyclerclassmarks.setAdapter(adapter);

        return view;

    }
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}