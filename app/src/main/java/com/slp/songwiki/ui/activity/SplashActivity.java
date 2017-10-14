package com.slp.songwiki.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.slp.songwiki.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPASH_TIME_OUT = 000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSplashScreen();

    }

    private void showSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,SongWikiActivity.class));
                finish();
            }
        },SPASH_TIME_OUT);
    }
}
