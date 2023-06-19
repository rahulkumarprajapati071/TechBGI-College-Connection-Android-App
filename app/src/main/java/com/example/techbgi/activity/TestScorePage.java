package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techbgi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TestScorePage extends AppCompatActivity {


    ImageView badge;
    TextView level,score,textView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_score_page);

        badge = findViewById(R.id.badge);
        level = findViewById(R.id.level);
        score = findViewById(R.id.score);
        textView8 = findViewById(R.id.textView8);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        String uniqueKey = reference.child("TestScore").push().getKey();

        String name = getIntent().getStringExtra("name");
        String rollNumber = getIntent().getStringExtra("rollNumber");
        Integer scores = getIntent().getIntExtra("score",0);
        Integer total = getIntent().getIntExtra("total",0);
        String subject = getIntent().getStringExtra("subject");
        String semester = getIntent().getStringExtra("semester");
        String branch = getIntent().getStringExtra("branch");
        String date = getIntent().getStringExtra("date");
        String timeEnd = getIntent().getStringExtra("timeEnd");
        String timeStart = getIntent().getStringExtra("timeStart");


        HashMap data = new HashMap();
        data.put("subjectName",subject);
        data.put("studentName",name);
        data.put("score",scores);
        data.put("total",total);
        data.put("semester",semester);
        data.put("branch",branch);
        data.put("rollNumber",rollNumber);
        data.put("date",date);
        data.put("timeStart",timeStart);
        data.put("timeEnd",timeEnd);

        reference.child("TestScore").child(uniqueKey).setValue(data);

        score.setText(String.format("%d/%d", scores, total));

        // Calculate the percentage
         Integer percentage = (scores * 100) / total;

        // Set the badge based on the percentage
        String badges;
        int badgeDrawable;
        if (percentage >= 96) {
            badges = "Diamond Elite";
            badgeDrawable = R.drawable.diamond;
        } else if (percentage >= 90) {
            badges = "Platinum Master";
            badgeDrawable = R.drawable.platinum;
        } else if (percentage >= 80) {
            badges = "Gold Star Performer";
            badgeDrawable = R.drawable.gold;
        } else if (percentage >= 70) {
            badges = "Silver Champion";
            badgeDrawable = R.drawable.silver;
        } else if (percentage >= 60) {
            badges = "Bronze Achiever";
            badgeDrawable = R.drawable.bronze;
        } else {
            badges = "-------------";
            textView8.setText("-----------------");
            badgeDrawable = R.drawable.good;
        }

        // Display the badge in the TextView
        level.setText(badges);
        badge.setImageResource(badgeDrawable);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}