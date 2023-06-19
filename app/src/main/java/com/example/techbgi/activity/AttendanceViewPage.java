package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.techbgi.R;
import com.example.techbgi.adapter.AttendanceViewAdapter;
import com.example.techbgi.model.AttendanceViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AttendanceViewPage extends AppCompatActivity {

    AttendanceViewAdapter adapter;
    TextView datanot;
    private final List<AttendanceViewModel> attendanceViewModelList = new ArrayList<>();
    private RecyclerView attendancerec;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference reference = database.getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
    RecyclerView.LayoutManager layoutManager;

    int total = 0, totalPresent = 0, totalAbsent = 0;
    String studentName;
    TextView name,enrollment,branchs,semesters,subjects,totalday,present,absent,percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_view_page);

        String roll = getIntent().getStringExtra("roll");
        String subject = getIntent().getStringExtra("subject");
        String semester = getIntent().getStringExtra("semester");
        String branch = getIntent().getStringExtra("branch");

        fetchAttendance(roll,semester,branch,subject);

        name = findViewById(R.id.name);
        enrollment = findViewById(R.id.enrollment);
        branchs = findViewById(R.id.branch);
        semesters = findViewById(R.id.semester);
        subjects = findViewById(R.id.subject);
        totalday = findViewById(R.id.totalday);
        present = findViewById(R.id.present);
        absent = findViewById(R.id.absent);
        percentage = findViewById(R.id.percentage);
        datanot = findViewById(R.id.datanot);

        name.setText(studentName);
        enrollment.setText(roll);
        branchs.setText(branch);
        semesters.setText(semester);
        subjects.setText(subject);

        attendancerec = findViewById(R.id.attendancerec);
        attendancerec.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        attendancerec.setLayoutManager(layoutManager);
        adapter = new AttendanceViewAdapter(attendanceViewModelList,this);
        attendancerec.setAdapter(adapter);
    }

    private void fetchAttendance(String rollNumber, String semester, String branch, String subject) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("AttendanceOfStudent")
                .child(rollNumber + semester + branch + subject);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalPresent = 0;
                    int totalAbsent = 0;
                    int total = (int) snapshot.getChildrenCount();

                    for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                        String date = dateSnapshot.getKey();

                        AttendanceViewModel attendanceViewModel = dateSnapshot.getValue(AttendanceViewModel.class);
                        if (attendanceViewModel != null) {
                            // Add each item at the top of the list
                            attendanceViewModelList.add(0, attendanceViewModel);

                            if (attendanceViewModel.getStatus().equals("P")) {
                                totalPresent++;
                            } else {
                                totalAbsent++;
                            }
                        }
                        name.setText(attendanceViewModel.getStudentName());
                    }

                    adapter.notifyDataSetChanged();

                    totalday.setText(String.valueOf(total));
                    present.setText(String.valueOf(totalPresent));
                    absent.setText(String.valueOf(totalAbsent));

                    int percentage1 = total == 0 ? 0 : (totalPresent * 100) / total;
                    percentage.setText(String.valueOf(percentage1) + "%");

                    if (attendanceViewModelList.size() != 0) {
                        datanot.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("loadPost:onCancelled", error.toString());
            }
        });
    }

}