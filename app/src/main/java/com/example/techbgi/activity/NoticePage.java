package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.techbgi.R;
import com.example.techbgi.adapter.NoticeAdapter;
import com.example.techbgi.model.NoticeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
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
        reference.child("Notice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeModelList.clear();
                List<NoticeModel> validNotices = new ArrayList<>();

                // Iterate through the dataSnapshot to filter out expired notices
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NoticeModel noticeModel = dataSnapshot.getValue(NoticeModel.class);
                    if (noticeModel != null) {
                        long currentTime = System.currentTimeMillis();
                        long noticeTime = noticeModel.getTimeStamp();
                        long twentyFourHoursInMillis = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

                        // Check if notice is within the last 24 hours
                        if (currentTime - noticeTime <= twentyFourHoursInMillis) {
                            validNotices.add(noticeModel);
                        } else {
                            // Notice has expired, remove it from Firebase
                            dataSnapshot.getRef().removeValue();
                        }
                    }
                }

                // Reverse the valid notices list to display the latest on top
                Collections.reverse(validNotices);

                // Add the valid notices to the noticeModelList
                noticeModelList.addAll(validNotices);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("loadPost:onCancelled", error.toString());
            }
        });
    }


}