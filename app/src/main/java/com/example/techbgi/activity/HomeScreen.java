package com.example.techbgi.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.techbgi.R;
import com.example.techbgi.fullscreen.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeScreen extends BaseActivity {

    BottomNavigationView bottom_navigation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techbgi-default-rtdb.firebaseio.com/");

        //for both two Fragment comes on one screen.....
        String flag = getIntent().getStringExtra("flag");
        String number = getIntent().getStringExtra("mobile");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(flag.equals("students"))
        {
            transaction.replace(R.id.content,new HomeFragment());
        }else{
            transaction.replace(R.id.content,new FacultyHomeFragment());
        }
        transaction.commit();

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        break;

                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(),NoticePage.class));
                        break;

                    case R.id.message:
                        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.profile:
                        if(flag.equals("students"))
                        {
                            Intent intent1 = new Intent(getApplicationContext(),StudentProfile.class);
                            intent1.putExtra("mobile",number);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent2 = new Intent(getApplicationContext(),FacultyProfile.class);
                            intent2.putExtra("mobile",number);
                            startActivity(intent2);
                        }
                        break;
                }

                return true;
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
    }@Override
    public void onPause() {
        super.onPause();
    }
}