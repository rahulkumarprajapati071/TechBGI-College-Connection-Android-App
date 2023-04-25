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
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;
import com.example.techbgi.adapter.AttendanceViewAdapter;
import com.example.techbgi.adapter.ClassAdapter;
import com.example.techbgi.adapter.NotesAdapter;
import com.example.techbgi.adapter.NoticeAdapter;
import com.example.techbgi.model.AttendanceViewModel;
import com.example.techbgi.model.ClassItem;
import com.example.techbgi.model.NotesModel;
import com.example.techbgi.model.NoticeModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

        System.out.println(">>>>>>>>>>>sutdnet name "+studentName);
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
//    }
//    public void loadData() {
//
//        reference.child("Notice").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                noticeModelList.clear();
//                for(DataSnapshot dataSnapshot: snapshot.getChildren())
//                {
//                    NoticeModel noticeModel = dataSnapshot.getValue(NoticeModel.class);
//                    assert noticeModel != null;
//                    noticeModelList.add(noticeModel);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d( "loadPost:onCancelled", error.toString());
//            }
//        });
    }

    private void fetchAttendance(String rollNumber,String semester,String branch,String subject) {

        // Calculate the college start date (15th January of the current year)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        Date collegeStartDate = calendar.getTime();

        DatabaseReference reference = database.getReference().child("AttendanceOfStudent").child(rollNumber+semester+branch+subject);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String collegeStartDateString = dateFormat.format(collegeStartDate);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendanceViewModelList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                        String date = subjectSnapshot.getKey();
                        if (date.compareTo(collegeStartDateString) >= 0 && date.compareTo(dateFormat.format(new Date())) <= 0) {
                            AttendanceViewModel attendanceViewModel = subjectSnapshot.getValue(AttendanceViewModel.class);
                            assert attendanceViewModel != null;
                            attendanceViewModelList.add(attendanceViewModel);
                            adapter.notifyDataSetChanged();
                            total++;
                            if (Objects.equals(attendanceViewModel.getStatus(), "P")) {
                                totalPresent++;
                            } else {
                                totalAbsent++;
                            }
                            name.setText(attendanceViewModel.getStudentName());
                        }
                    }
                    totalday.setText(String.valueOf(total));
                    present.setText(String.valueOf(totalPresent));
                    absent.setText(String.valueOf(totalAbsent));

                    //percentage kese nikalega kya shi nikal ahai
                    int per = (totalPresent * 100)/total;
                    percentage.setText(String.valueOf(per)+"%");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d( "loadPost:onCancelled", error.toString());
            }
        });
    }

//    private void fetchAttendance(String rollNumber,String semester,String branch,String subject) {
//
//        // Calculate the college start date (15th January of the current year)
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, Calendar.JANUARY);
//        calendar.set(Calendar.DAY_OF_MONTH, 15);
//        Date collegeStartDate = calendar.getTime();
//
//        DatabaseReference reference = database.getReference().child("AttendanceOfStudent").child(rollNumber+semester+branch+subject);
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String collegeStartDateString = dateFormat.format(collegeStartDate);
//
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()) {
//                    for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
//                        String date = subjectSnapshot.getKey();
//                        if (date.compareTo(collegeStartDateString) >= 0 && date.compareTo(dateFormat.format(new Date())) <= 0) {
//                            AttendanceViewModel attendanceViewModel = null;
//                            for (DataSnapshot attendanceSnapshot : subjectSnapshot.getChildren()) {
//                                String status = attendanceSnapshot.getValue().toString();
//                                String dates = attendanceSnapshot.getValue().toString();
//                                String studentname = attendanceSnapshot.getValue().toString();
//                                attendanceViewModel = new AttendanceViewModel(dates, status, studentname);
//                                assert attendanceViewModel != null;
//                                attendanceViewModelList.add(attendanceViewModel);
//                            }
//                            total++;
//                            if (Objects.equals(attendanceViewModel.getStatus(), "P")) {
//                                totalPresent++;
//                            } else {
//                                totalAbsent++;
//                            }
//                            name.setText(attendanceViewModel.getStudentName());
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//                    totalday.setText(String.valueOf(total));
//                    present.setText(String.valueOf(totalPresent));
//                    absent.setText(String.valueOf(totalAbsent));
//
//                    //percentage kese nikalega kya shi nikal ahai
//                    int per = (totalPresent * 100)/total;
//                    percentage.setText(String.valueOf(per));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d( "loadPost:onCancelled", error.toString());
//            }
//        });
//    }
}