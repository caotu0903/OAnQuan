package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ChooseGame extends AppCompatActivity {

    ImageButton bt_Menu;
    ImageButton bt_oanquan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
        getSupportActionBar().hide();

        bt_Menu = (ImageButton) findViewById(R.id.bt_menu_A_choosegame);
        bt_oanquan = (ImageButton) findViewById(R.id.iButton);

        bt_oanquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_waitroom = new Intent(ChooseGame.this, ChooseRoom.class);
                startActivity(intent_waitroom);
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Login.getLoginActivity().SendMessage("102");
        String logoutMessage = "";
        while (logoutMessage.equals("")) {
            logoutMessage = Login.getLoginActivity().GetMessage();
        }
        if (logoutMessage.contains("102")) {
            Intent intent_logout = new Intent(ChooseGame.this, Login.class);
            startActivity(intent_logout);
            Toast.makeText(getApplicationContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        }
    }
}