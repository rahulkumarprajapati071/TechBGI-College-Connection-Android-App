package com.example.techbgi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.techbgi.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PaperActivity extends AppCompatActivity {

    Spinner spinner1,spinner2;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        nextBtn = findViewById(R.id.nextBtn);

        String[] valueBranch = {"Choose Branch","CSE","AIML","CSBS","CSE-IOT","CSIT","CIVIL","ME","EC","EEE",
        "IT","EE","EX","AUTO","CHEMICAL","FT","MINING","TX","OTHERS"};
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(valueBranch));
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList1);
        spinner1.setAdapter(arrayAdapter1);

        String[] valueSem = {"Current Semester","1 SEMESTER","2 SEMESTER","3 SEMESTER","4 SEMESTER","5 SEMESTER","6 SEMESTER","7 SEMESTER","8 SEMESTER"};
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(valueSem));
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,R.layout.style_textspinner,arrayList2);
        spinner2.setAdapter(arrayAdapter2);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner1.getSelectedItemId() > 0 && spinner2.getSelectedItemId() > 0){
                    if(spinner2.getSelectedItemId() == 1 || spinner2.getSelectedItemId() == 2)
                    {
                        Uri uri = Uri.parse("https://www.rgpvonline.com/rgpv-first-year.html#list");
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }else{
                        char s = spinner2.getSelectedItem().toString().charAt(0);
                        String b = spinner1.getSelectedItem().toString().toLowerCase();

                        Uri uri = Uri.parse("https://www.rgpvonline.com/btech-"+b+"-question-papers.html#"+s+"s");
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                }else
                {
                    Toast.makeText(PaperActivity.this, "Please choose right branch or semester", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}