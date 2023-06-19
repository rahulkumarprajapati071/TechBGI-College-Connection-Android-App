package com.example.techbgi.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techbgi.R;
import com.example.techbgi.adapter.QuestionsCardAdapter;
import com.example.techbgi.model.QuestionsDetailsModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionsCardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    QuestionsCardAdapter adapter;

    RecyclerView recyclerQuestionsCard;

    public QuestionsCardFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static QuestionsCardFragment newInstance(String param1, String param2) {
        QuestionsCardFragment fragment = new QuestionsCardFragment();
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
        View view = inflater.inflate(R.layout.fragment_questions_card, container, false);

        recyclerQuestionsCard = (RecyclerView) view.findViewById(R.id.recyclerQuestionsCard);
        recyclerQuestionsCard.setLayoutManager(new LinearLayoutManager(getContext()));

//        recyclerQuestionsCard = (RecyclerView) view.findViewById(R.id.recyclerQuestionsCard);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setReverseLayout(true); // Set reverse layout
//        recyclerQuestionsCard.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<QuestionsDetailsModel> options =
                new FirebaseRecyclerOptions.Builder<QuestionsDetailsModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Questions").orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid()),QuestionsDetailsModel.class)
                        .build();

        adapter = new QuestionsCardAdapter(options);
        recyclerQuestionsCard.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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