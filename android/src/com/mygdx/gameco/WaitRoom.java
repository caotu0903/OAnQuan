package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class WaitRoom extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_room);

        ImageButton bt_back = (ImageButton) findViewById(R.id.bt_back_A_waitroom);
        ImageButton bt_r1 = (ImageButton) findViewById(R.id.room1);
        ImageButton bt_r2 = (ImageButton) findViewById(R.id.room2);
        ImageButton bt_r3 = (ImageButton) findViewById(R.id.room3);
        ImageButton bt_r4 = (ImageButton) findViewById(R.id.room4);
        ImageButton bt_r5 = (ImageButton) findViewById(R.id.room5);
        ImageButton bt_r6 = (ImageButton) findViewById(R.id.room6);
        ImageButton bt_r7 = (ImageButton) findViewById(R.id.room7);
        ImageButton bt_r8 = (ImageButton) findViewById(R.id.room8);
        ImageButton bt_r9 = (ImageButton) findViewById(R.id.room9);
        ImageButton bt_r10 = (ImageButton) findViewById(R.id.room10);
        ImageButton bt_r11 = (ImageButton) findViewById(R.id.room11);
        ImageButton bt_r12 = (ImageButton) findViewById(R.id.room12);
        Intent intent_gameplay = new Intent(WaitRoom.this, AndroidLauncher.class);
        Intent intent_waiting_player = new Intent(WaitRoom.this, Waiting_Player.class);
        Intent intent_waiting_host = new Intent(WaitRoom.this, Waiting_Host.class);
        Intent intent_choose_game = new Intent(WaitRoom.this, ChooseGame.class);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(intent_choose_game); }
        });
        bt_r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_waiting_player);
            }
        });
        bt_r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_waiting_host);
            }
        });
        bt_r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
        bt_r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
        bt_r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
        bt_r6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
        bt_r7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
        bt_r8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
        bt_r9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
        bt_r10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
        bt_r11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
        bt_r12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_gameplay);
            }
        });
    }
}