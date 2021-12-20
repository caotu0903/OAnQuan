package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ChooseGame extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        ImageButton bt_oanquan = (ImageButton) findViewById(R.id.iButton);

        bt_oanquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_waitroom = new Intent(ChooseGame.this, WaitRoom.class);
                startActivity(intent_waitroom);
            }
        });
    }
}