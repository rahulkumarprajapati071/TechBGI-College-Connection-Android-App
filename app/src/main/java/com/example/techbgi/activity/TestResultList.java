package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.techbgi.R;
import com.example.techbgi.adapter.TestResultAdapter;
import com.example.techbgi.model.TestResultModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TestResultList extends AppCompatActivity {

    private DatabaseReference testScoreRef;
    private TestResultAdapter testResultAdapter;
    private List<TestResultModel> testResults;
    String subject;
    String branch;
    String semester;
    String date;
    String timeEnd;
    String timeStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result_list);

        testResults = new ArrayList<>();
        testResultAdapter = new TestResultAdapter(testResults);

        TextView subjectNameTextView = findViewById(R.id.subjectNameTextView);
        TextView semesterBranchTextView = findViewById(R.id.semesterBranchTextView);

        // Set subject name and semester/branch

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(testResultAdapter);

        String ref = getIntent().getStringExtra("ref");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);

        testScoreRef = FirebaseDatabase.getInstance().getReference().child("TestScore");

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subject = snapshot.child("subjectName").getValue(String.class);
                branch = snapshot.child("branch").getValue(String.class);
                semester = snapshot.child("semester").getValue(String.class);
                date = snapshot.child("date").getValue(String.class);
                timeEnd = snapshot.child("timeEnd").getValue(String.class);
                timeStart = snapshot.child("timeStart").getValue(String.class);

                subjectNameTextView.setText(subject);
                semesterBranchTextView.setText("semester: " + semester + "  Branch: " + branch);
                testScoreRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        testResults.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String studentName = snapshot.child("studentName").getValue(String.class);
                            String rollNumber = snapshot.child("rollNumber").getValue(String.class);
                            int totalMarks = snapshot.child("total").getValue(Integer.class);
                            int obtainedMarks = snapshot.child("score").getValue(Integer.class);
                            String sem = snapshot.child("semester").getValue(String.class);
                            String bran = snapshot.child("branch").getValue(String.class);
                            String sub = snapshot.child("subjectName").getValue(String.class);
                            String dat = snapshot.child("date").getValue(String.class);
                            String timEnd = snapshot.child("timeEnd").getValue(String.class);
                            String timStart = snapshot.child("timeStart").getValue(String.class);

                            if (sem != null && sem.equals(semester) && bran != null && bran.equals(branch) && sub != null && sub.equals(subject)
                                    && dat != null && dat.equals(date) && timEnd != null && timEnd.equals(timeEnd) && timStart != null && timStart.equals(timeStart)) {
                                TestResultModel testResult = new TestResultModel(studentName, rollNumber, totalMarks, obtainedMarks);
                                testResults.add(0, testResult); // Add at the beginning of the list
                            }

                        }
                        testResultAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
