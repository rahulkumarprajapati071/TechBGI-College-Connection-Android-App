package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;
import com.example.techbgi.adapter.ClassAdapter;
import com.example.techbgi.adapter.NotesAdapter;
import com.example.techbgi.adapter.NoticeAdapter;
import com.example.techbgi.model.ClassItem;
import com.example.techbgi.model.NotesModel;
import com.example.techbgi.model.NoticeModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticePage extends AppCompatActivity {

    NoticeAdapter adapter;
    private final List<NoticeModel> noticeModelList = new ArrayList<>();
    private RecyclerView noticeview;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_page);

        loadClassData();

        noticeview = findViewById(R.id.noticeview);
        noticeview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        noticeview.setLayoutManager(layoutManager);
        adapter = new NoticeAdapter(noticeModelList,this);
        noticeview.setAdapter(adapter);
    }
    public void loadClassData() {

        reference.child("Notice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeModelList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    NoticeModel noticeModel = dataSnapshot.getValue(NoticeModel.class);
                    assert noticeModel != null;
                    noticeModelList.add(noticeModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d( "loadPost:onCancelled", error.toString());
            }
        });
    }
}