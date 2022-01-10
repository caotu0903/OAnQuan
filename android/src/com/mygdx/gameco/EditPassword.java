package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class EditPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent_back = new Intent(EditPassword.this, EditInfo.class);
        startActivity(intent_back);
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }
}