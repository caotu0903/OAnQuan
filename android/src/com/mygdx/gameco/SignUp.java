package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button btSignUp = (Button) findViewById(R.id.bt_signup1);
        EditText edHoten = (EditText) findViewById(R.id.ed_hoten);
        EditText edUname = (EditText) findViewById(R.id.ed_username);
        EditText edEmail = (EditText) findViewById(R.id.ed_email);
        EditText edPass = (EditText) findViewById(R.id.ed_password);
        EditText edRepass = (EditText) findViewById(R.id.ed_repassword);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }
}