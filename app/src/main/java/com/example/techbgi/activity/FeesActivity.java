package com.example.techbgi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.techbgi.R;

public class FeesActivity extends AppCompatActivity implements View.OnClickListener{

    TextView sdbce,sdbct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sdbce = findViewById(R.id.sdbce);
        sdbct = findViewById(R.id.sdbct);

        sdbce.setOnClickListener(this);
        sdbct.setOnClickListener(this);

    }
    public void onClick(View v)
    {
        Intent i;
        switch (v.getId())
        {
            case R.id.sdbce:
                openSdbce();
                break;
            case R.id.sdbct:
                openSdbct();
                break;
        }

    }
    private void openSdbce(){
        Uri uri = Uri.parse("http://161.202.197.85:7051/smis/");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        this.startActivity(intent);
    }
    private void openSdbct(){
        Uri uri = Uri.parse("http://161.202.197.85:7041/smis/");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        this.startActivity(intent);
    }

}