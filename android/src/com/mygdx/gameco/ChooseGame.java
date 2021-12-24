package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ChooseGame extends AppCompatActivity {

    ImageButton bt_Menu;
    ImageButton bt_oanquan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        bt_Menu = (ImageButton) findViewById(R.id.bt_menu_A_choosegame);
        bt_oanquan = (ImageButton) findViewById(R.id.iButton);

        bt_oanquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_waitroom = new Intent(ChooseGame.this, WaitRoom.class);
                startActivity(intent_waitroom);
            }
        });

        bt_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_menu = new Intent(ChooseGame.this, Menu.class);
                startActivity(intent_menu);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

    }
}