package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.model.QuestionsDetailsModel;
import com.example.techbgi.model.StudentRegistrationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FrontQuizDetails extends AppCompatActivity {
    TextView subjectName,facultyName,timeStart,timeEnd,date;
    Button start;
    RelativeLayout layout;
    private String dateQ;
    private String timeStartQ;
    private String timeEndQ;
    private String mobileNumber;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
        setContentView(R.layout.activity_front_quiz_details);

        subjectName = findViewById(R.id.subjectName);
        facultyName = findViewById(R.id.facultyName);
        timeStart = findViewById(R.id.timeStart);
        timeEnd = findViewById(R.id.timeEnd);
        start = findViewById(R.id.start);
        date = findViewById(R.id.date);
        layout = findViewById(R.id.layout);

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("All Users").child(uid);
        usersRef.child("mobileNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mobileNumber = snapshot.getValue(String.class);
                processFirst(mobileNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here
            }
        });


    }
    private void processFirst(String mobile){
        DatabaseReference studentRef = reference.child("students");
        studentRef.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentRegistrationModel studentModel = snapshot.getValue(StudentRegistrationModel.class);
                processSecond(studentModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void processSecond(StudentRegistrationModel studentModel) {
        String semesterS = studentModel.getSemester();
        String branchS = studentModel.getBranch();
        String firstName = studentModel.getFirstName();
        String lastName = studentModel.getLastName();
        String nameS = firstName+" "+lastName;
        String rollNumS = studentModel.getRollNumber();

        String subject = getIntent().getStringExtra("subjectName");

        reference.child("Questions").orderByChild("subjectName").equalTo(subject).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<QuestionsDetailsModel> quizzes = new ArrayList<>();

                // Step 2: Fetch all quizzes and add them to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String facultyName = snapshot.child("facultyName").getValue(String.class);
                    String subjectName = snapshot.child("subjectName").getValue(String.class);
                    dateQ = snapshot.child("date").getValue(String.class);
                    timeStartQ = snapshot.child("timeStart").getValue(String.class);
                    timeEndQ = snapshot.child("timeEnd").getValue(String.class);
                    String semester = snapshot.child("semester").getValue(String.class);
                    String branch = snapshot.child("branch").getValue(String.class);
                    String json = snapshot.child("jsonQuestion").getValue(String.class);

                    QuestionsDetailsModel quiz = new QuestionsDetailsModel(dateQ, facultyName,json, subjectName, timeStartQ, timeEndQ,semester,branch);
                    quizzes.add(quiz);
                }

                // Step 3: Sort the quizzes based on the date and time
                Collections.sort(quizzes, new Comparator<QuestionsDetailsModel>() {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

                    @Override
                    public int compare(QuestionsDetailsModel quiz1, QuestionsDetailsModel quiz2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                        try {
                            Date dateTime1 = sdf.parse(quiz1.getDate() + " " + quiz1.getTimeStart());
                            Date dateTime2 = sdf.parse(quiz2.getDate() + " " + quiz2.getTimeStart());
                            return Long.compare(dateTime1.getTime(), dateTime2.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }

                });

                // Step 4: Get the first quiz, which represents the quiz added first by faculty
                if (!quizzes.isEmpty()) {
                    Boolean flag = true;
                    for(int i = 0; i < quizzes.size(); i++){
                        if(quizzes.get(i).getSemester().equals(semesterS) && quizzes.get(i).getBranch().equals(branchS)){
                            QuestionsDetailsModel firstQuiz = quizzes.get(0);

                            // Display the first quiz details in your UI
                            timeEnd.setText(firstQuiz.getTimeEnd());
                            timeStart.setText(firstQuiz.getTimeStart());
                            date.setText(firstQuiz.getDate());
                            facultyName.setText(firstQuiz.getFacultyName());
                            subjectName.setText(firstQuiz.getSubjectName());

                            Calendar currentDateTime = Calendar.getInstance(); // Get current system date and time

                            // Create a SimpleDateFormat instance with the specified date format
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

                            //logic for convert 12hours time formate to 24 hours time formate
                            String time24Hour = "";
                            try {
                                SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");
                                SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
                                Date date = sdf12.parse(quizzes.get(0).getTimeEnd());
                                time24Hour = sdf24.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                Date firebaseDateTime = sdf.parse(firstQuiz.getDate() + " " + firstQuiz.getTimeStart());
                                String endTimeFirebase = time24Hour.substring(0,2)+time24Hour.substring(3,5);

                                String currentTime = currentDateTime.getTime().toString().substring(11,13)+currentDateTime.getTime().toString().substring(14,16);

                                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                                Date endTime = sdf1.parse(time24Hour);
                                Date curt = sdf1.parse(currentDateTime.getTime().toString().substring(11,16));

                                long endTimeMs = endTime.getTime();
                                long currentTimeMs = curt.getTime();
                                long quizDurationMs = endTimeMs - currentTimeMs;


                                if (currentDateTime.getTime().compareTo(firebaseDateTime) >= 0) {
                                    // System date and time is equal to or after the set date and time
                                    if(Integer.parseInt(endTimeFirebase) > Integer.parseInt(currentTime)){
                                        start.setEnabled(true);
                                        start.setText("START");
                                        start.setBackgroundResource(R.drawable.call_back);
                                    }else{
                                        layout.setVisibility(View.INVISIBLE);
                                        Toast.makeText(FrontQuizDetails.this, "Test/Quiz Over Be Ready for next time", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // System date and time is before the set date and time
                                    start.setEnabled(false);
                                }
                                start.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(),TestQuestionsScreen.class);
                                        intent.putExtra("json",quizzes.get(0).getJsonQuestion());
                                        intent.putExtra("name",nameS);
                                        intent.putExtra("date",quizzes.get(0).getDate());
                                        intent.putExtra("timeStart",quizzes.get(0).getTimeStart());
                                        intent.putExtra("timeEnd",quizzes.get(0).getTimeEnd());
                                        intent.putExtra("rollNumber",rollNumS);
                                        intent.putExtra("semester",semesterS);
                                        intent.putExtra("branch",branchS);
                                        intent.putExtra("subject",subject);
                                        intent.putExtra("quizDurationMs",quizDurationMs);
                                        final int[] flag = {0};
                                        reference.child("TestScore").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                                    String studentName = snapshot1.child("studentName").getValue(String.class);
                                                    String rollNumber = snapshot1.child("rollNumber").getValue(String.class);
                                                    String sub = snapshot1.child("subjectName").getValue(String.class);
                                                    String dat = snapshot1.child("date").getValue(String.class);
                                                    String timEnd = snapshot1.child("timeEnd").getValue(String.class);
                                                    String timStart = snapshot1.child("timeStart").getValue(String.class);

                                                    if (timEnd != null && timEnd.equals(timeEndQ) && Objects.equals(timStart, timeStartQ) && Objects.equals(dat, dateQ) && Objects.equals(sub, getIntent().getStringExtra("subjectName")) && Objects.equals(rollNumber, rollNumS) && Objects.equals(studentName, nameS)) {
                                                        Toast.makeText(FrontQuizDetails.this, "You have already attempted this Test. Be ready for the next schedule", Toast.LENGTH_SHORT).show();
                                                        flag[0] = 1;
                                                    }

                                                }
                                                if(flag[0] != 1){
                                                    startActivity(intent);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        layout.setVisibility(View.INVISIBLE);
                        Toast.makeText(FrontQuizDetails.this, "There is No any Test/Quizz Scheduled", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    layout.setVisibility(View.INVISIBLE);
                    Toast.makeText(FrontQuizDetails.this, "There is No any Test/Quizz Scheduled", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during the database operation
                Log.e("Firebase", "Database Error: " + error.getMessage());
                Toast.makeText(FrontQuizDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}