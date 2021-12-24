package com.mygdx.gameco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Waiting_Player extends AppCompatActivity {

    Button bt_Ready;
    ImageButton bt_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_player);

        bt_Ready = (Button) findViewById(R.id.bt_ready_A_waiting_player);
        bt_Back = (ImageButton) findViewById(R.id.bt_back_A_waiting_player);
        Intent intent_back = new Intent(Waiting_Player.this, WaitRoom.class);

        bt_Ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_Ready.getText().toString().trim() == "SẴN SÀNG")
                {
                    bt_Ready.setText("HUỶ");
                }
                else bt_Ready.setText("SẴN SÀNG");
            }
        });

        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(intent_back); }
        });

    }
}
