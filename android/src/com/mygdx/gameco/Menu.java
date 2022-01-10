package com.mygdx.gameco;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    ImageButton bt_Back;
    Button bt_Logout;
    Button bt_Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        bt_Back = (ImageButton) findViewById(R.id.bt_back_A_menu);
        bt_Logout = (Button) findViewById(R.id.bt_logout_A_menu);
        bt_Edit = (Button) findViewById(R.id.bt_edit_info_A_menu);

        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });

        bt_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.getLoginActivity().SendMessage("102");
                String logoutMessage = "";
                while (logoutMessage == "") {
                    logoutMessage = Login.getLoginActivity().GetMessage();
                }
                if (logoutMessage.contains("102")) {
                    Intent intent_logout = new Intent(Menu.this, Login.class);
                    startActivity(intent_logout);
                    Toast.makeText(getApplicationContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_edit = new Intent(Menu.this, EditInfo.class);
                startActivity(intent_edit);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }
}
