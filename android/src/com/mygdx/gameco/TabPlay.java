package com.mygdx.gameco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class TabPlay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_play);

        TextView textView = (TextView)findViewById(R.id.tv);

        Animation blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        textView.startAnimation(blink);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabPlay.this, Login.class);
                startActivity(intent);
            }
        });
    }
}