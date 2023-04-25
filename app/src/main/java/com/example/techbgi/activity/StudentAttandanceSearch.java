package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.techbgi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class StudentAttandanceSearch extends AppCompatActivity {

    EditText rollNo;
    Spinner semester,branch,subjectName;

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attandance_search);

        rollNo = findViewById(R.id.rollNo);
        subjectName = findViewById(R.id.subject);
        semester = findViewById(R.id.semester);
        branch = findViewById(R.id.branch);
        next = findViewById(R.id.next);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");

        String[] valueSem = {"Current Semester","1","2","3","4","5","6","7","8"};
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(valueSem));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter);

        String[] valueBranch = {"Choose Branch","CSE","AIML","CSBS","CSE-IOT","CSIT","CIVIL","ME","EC","EEE",
                "IT","EE","EX","AUTO","CHEMICAL","FT","MINING","TX","OTHERS"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(valueBranch));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(adapter2);

        String[] valueSubject = {"Choose Subject","Data Mining","TOC","DBMS","OS","CN","Computer Graphics","Software Engineering","ADA","Wireless and mobile computing",
                "Compiler Designing","ML","CA","E-Commerce","EEE","Engineering Drawing","Data Structure"};
        ArrayList<String> arrayList3 = new ArrayList<>(Arrays.asList(valueSubject));
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList3);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectName.setAdapter(adapter3);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.show();
                if(rollNo.getText().toString().trim().isEmpty() || subjectName.getSelectedItemId() == 0 || semester.getSelectedItemId() == 0 || branch.getSelectedItemId() == 0 ){
                    Toast.makeText(StudentAttandanceSearch.this, "Please select all requirements", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(),AttendanceViewPage.class);
                    intent.putExtra("roll",rollNo.getText().toString());
                    intent.putExtra("semester",semester.getSelectedItem().toString());
                    intent.putExtra("branch",branch.getSelectedItem().toString());
                    intent.putExtra("subject",subjectName.getSelectedItem().toString());
                    startActivity(intent);
                }
//                dialog.dismiss();
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
//                            for (DataSnapshot attendanceSnapshot : subjectSnapshot.getChildren()) {
//                                String status = attendanceSnapshot.getValue(String.class);
//                                String name = attendanceSnapshot.getValue(String.class);
//                                System.out.println(status+" : "+name);
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });




//                attendanceQuery.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        int absent = 0;
//                        int present = 0;
//                        int total = 0;
//                        // Iterate over the attendance data and display it
//                        for (DataSnapshot attendanceSnapshot : dataSnapshot.getChildren()) {
//                            String date = attendanceSnapshot.child("date").getValue(String.class);
//                            String status = attendanceSnapshot.child("status").getValue(String.class);
//                            String name = attendanceSnapshot.child("studentName").getValue(String.class);
//                            // Display the attendance data in a TextView or some other UI element
//                            if(status == "A"){
//                                absent++;
//                                total++;
//                            }else{
//                                present++;
//                                total++;
//                            }
//                            Log.d("Attendance", "Date: " + date + ", Status: " + status+", name: "+name);
//                        }
//                        System.out.println("Total: "+total+"   "+"Present:  "+present+"   "+"Absent: "+absent);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Handle any errors that occur while fetching the attendance data
//                    }
//                });

}