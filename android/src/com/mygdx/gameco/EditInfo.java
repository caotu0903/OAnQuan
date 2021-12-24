package com.mygdx.gameco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class EditInfo extends AppCompatActivity {

    ImageButton bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        bt_back = (ImageButton) findViewById(R.id.bt_back_A_edit_info);
        Intent intent_back = new Intent(EditInfo.this, Menu.class);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(intent_back); }
        });
    }
}
