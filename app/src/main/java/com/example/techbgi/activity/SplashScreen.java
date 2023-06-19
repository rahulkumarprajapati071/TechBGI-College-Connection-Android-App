package com.example.techbgi.activity;

import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.os.Bundle;

import com.example.techbgi.R;
import com.example.techbgi.fullscreen.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends BaseActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        MotionLayout motionLayout = findViewById(R.id.motionLayout);
        motionLayout.startLayoutAnimation();

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener()
        {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1)
            {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v)
            {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i)
            {
                startActivity(new Intent(SplashScreen.this,FrontScreen.class));
                finish();

            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v)
            {

            }
        });
    }
}