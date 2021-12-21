package com.mygdx.gameco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TabPlay extends Activity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_play);

        TextView textView = (TextView)findViewById(R.id.tv);

        Animation blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        textView.startAnimation(blink);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(TabPlay.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}