package com.mygdx.gameco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btSignup = (Button)findViewById(R.id.bt_signup);
        Button btLogin = (Button)findViewById(R.id.bt_login);

        EditText edUsername = (EditText)findViewById(R.id.edt_username);
        EditText edPass = (EditText)findViewById(R.id.edt_password);

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class );
                startActivity(intent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_choosegame = new Intent(Login.this, ChooseGame.class);
                startActivity(intent_choosegame);
            }
        });
    }
}