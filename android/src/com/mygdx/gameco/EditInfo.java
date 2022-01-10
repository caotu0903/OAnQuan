package com.mygdx.gameco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class EditInfo extends AppCompatActivity {

    ImageButton bt_back;
    Button bt_change_account_name;
    Button bt_change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        getSupportActionBar().hide();

        Init();

        bt_change_account_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_change_account_name = new Intent(EditInfo.this, EditAccountName.class);
                startActivity(intent_change_account_name);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        bt_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_change_password = new Intent(EditInfo.this, EditPassword.class);
                startActivity(intent_change_password);
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });
    }

    public void Init() {
        bt_back = (ImageButton) findViewById(R.id.bt_back_A_edit_info);
        bt_change_account_name = (Button) findViewById(R.id.bt_edit_account_name_A_edit_info);
        bt_change_password = (Button) findViewById(R.id.bt_edit_password_A_edit_info);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }
}
