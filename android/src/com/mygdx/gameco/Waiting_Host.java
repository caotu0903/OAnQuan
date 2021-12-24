package com.mygdx.gameco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Waiting_Host extends AppCompatActivity {

    Button bt_Start;
    Button bt_Invite;
    Button bt_Kick;
    ImageButton bt_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_host);

        bt_Start = (Button) findViewById(R.id.bt_start_A_waiting_host);
        bt_Invite = (Button) findViewById(R.id.bt_invite_A_waiting_host);
        bt_Kick = (Button) findViewById(R.id.bt_kick_A_waiting_host);
        bt_Back = (ImageButton) findViewById(R.id.bt_back_A_waiting_host);
        Intent intent_back = new Intent(Waiting_Host.this, WaitRoom.class);

        bt_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameplay = new Intent(Waiting_Host.this, AndroidLauncher.class);
                startActivity(gameplay);
            }
        });

        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(intent_back); }
        });
    }
}
