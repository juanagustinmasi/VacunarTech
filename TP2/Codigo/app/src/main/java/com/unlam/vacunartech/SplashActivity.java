package com.unlam.vacunartech;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.unlam.vacunartech.models.SessionManager;

public class SplashActivity extends AppCompatActivity {

    ProgressBar splashProgress;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(this);
        splashProgress=findViewById(R.id.splashProgress);
        ObjectAnimator.ofInt(splashProgress,"progress",100).setDuration(5000).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isUserLoggedIn()) {
                    Intent intent = new Intent(SplashActivity.this, MiVacunaActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, SMSActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },3000);
    }
}
