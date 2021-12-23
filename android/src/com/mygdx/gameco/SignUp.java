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

        Button btSignUp = (Button) findViewById(R.id.bt_signup_A_Signup);
        EditText edHoten = (EditText) findViewById(R.id.et_accountname_A_Signup);
        EditText edUname = (EditText) findViewById(R.id.et_username_A_Signup);
        EditText edEmail = (EditText) findViewById(R.id.et_email_A_Signup);
        EditText edPass = (EditText) findViewById(R.id.et_password_A_Signup);
        EditText edRepass = (EditText) findViewById(R.id.et_repassword_A_Signup);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }
}