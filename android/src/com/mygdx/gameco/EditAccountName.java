package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class EditAccountName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_name);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent_back = new Intent(EditAccountName.this, EditInfo.class);
        startActivity(intent_back);
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }
}