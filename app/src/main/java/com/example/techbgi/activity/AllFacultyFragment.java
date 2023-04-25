package com.example.techbgi.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techbgi.R;
import com.example.techbgi.adapter.AllFacultyAdapter;
import com.example.techbgi.model.FacultyRegistrationModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AllFacultyFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    AllFacultyAdapter adapter;
    RecyclerView recview;

    public AllFacultyFragment() {
        // Required empty public constructor
    }

    public static AllFacultyFragment newInstance(String param1, String param2) {
        AllFacultyFragment fragment = new AllFacultyFragment();
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

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_faculty_all, container, false);


        recview = (RecyclerView)view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<FacultyRegistrationModel> options = new FirebaseRecyclerOptions.Builder<FacultyRegistrationModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("faculty"),FacultyRegistrationModel.class)
                .build();

        adapter = new AllFacultyAdapter(options);
        recview.setAdapter(adapter);

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