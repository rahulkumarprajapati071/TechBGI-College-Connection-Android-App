package com.example.techbgi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.techbgi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class TestRoom extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_room);

        Toolbar toolbar = findViewById(R.id.attentoolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("All Quizz");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);


    }
    public void onViewClick(View view) {
        Intent intent = new Intent(getApplicationContext(),FrontQuizDetails.class);
        // Handle view click here
        switch (view.getId()) {
            case R.id.firstAdd:
                intent.putExtra("subjectName","Quantitative Apptitude");
                break;
            case R.id.secondAdd:
                intent.putExtra("subjectName","Logical Reasoning");
                break;
            case R.id.thirdAdd:
                intent.putExtra("subjectName","Verbal Reasoning");
                break;
            case R.id.fourthAdd:
                intent.putExtra("subjectName","Technical");
                break;
            // Add cases for other views
        }
        startActivity(intent);
    }
}