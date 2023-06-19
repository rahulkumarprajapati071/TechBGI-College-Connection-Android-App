package com.example.techbgi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.fullscreen.BaseActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SuggestionActivity extends BaseActivity {

    private EditText suggestionText,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        suggestionText = findViewById(R.id.suggestion_text);
        username = findViewById(R.id.username);
        Button submitButton = findViewById(R.id.sendBtn);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suggestion = suggestionText.getText().toString();
                if (suggestion.trim().isEmpty()) {
                    Toast.makeText(SuggestionActivity.this, "Please enter a suggestion", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");
                    reference.child("Feedback").child(username.getText().toString().trim()+":"+hour+":"+minute).setValue(suggestion);
                    Toast.makeText(SuggestionActivity.this, "Thank you for your suggestion", Toast.LENGTH_SHORT).show();
                    suggestionText.setText("");
                    startActivity(new Intent(getApplicationContext(),FrontScreen.class));
                }
            }
        });
    }
}
