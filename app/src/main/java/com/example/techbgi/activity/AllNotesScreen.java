package com.example.techbgi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.adapter.NotesAdapter;
import com.example.techbgi.model.NotesModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AllNotesScreen extends AppCompatActivity{

    NotesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes_screen);

        Toolbar toolbar = findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("All Notes");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);

        RecyclerView recnoteview = findViewById(R.id.recnoteview);
        recnoteview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String semester = getIntent().getStringExtra("semester");
        String branch = getIntent().getStringExtra("branch");
        Toast.makeText(this, semester+branch, Toast.LENGTH_SHORT).show();
        //for getting all notes from database based on semester and branch
        FirebaseRecyclerOptions<NotesModel> options = new FirebaseRecyclerOptions.Builder<NotesModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Notes").child(semester+branch), NotesModel.class)
                .build();

        adapter = new NotesAdapter(options);
        recnoteview.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}