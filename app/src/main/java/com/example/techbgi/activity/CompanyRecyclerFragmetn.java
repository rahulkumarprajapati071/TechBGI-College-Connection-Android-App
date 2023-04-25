package com.example.techbgi.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techbgi.R;
import com.example.techbgi.adapter.CompanyAdapter;
import com.example.techbgi.model.CompanyModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CompanyRecyclerFragmetn extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    CompanyAdapter adapter;

    RecyclerView recyclerCompany;

    public CompanyRecyclerFragmetn() {
    }

    // TODO: Rename and change types and number of parameters
    public static CompanyRecyclerFragmetn newInstance(String param1, String param2) {
        CompanyRecyclerFragmetn fragment = new CompanyRecyclerFragmetn();
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
        View view = inflater.inflate(R.layout.fragment_company_recycler_fragmetn, container, false);

        recyclerCompany = (RecyclerView) view.findViewById(R.id.recyclerCompany);
        recyclerCompany.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<CompanyModel> options =
                new FirebaseRecyclerOptions.Builder<CompanyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("company"),CompanyModel.class)
                        .build();

        adapter = new CompanyAdapter(options);
        recyclerCompany.setAdapter(adapter);

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