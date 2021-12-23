package com.mygdx.gameco;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.badlogic.gdx.Net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Login extends Activity {

    public Socket clientSocket;
    public PrintWriter output;
    public BufferedReader input;
    public Thread networkThread;
    public ArrayList<String> listenArrayMessage;

    EditText et_Username;
    EditText et_Pass;
    Button btSignup;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btSignup = (Button)findViewById(R.id.bt_signup);
        btLogin = (Button)findViewById(R.id.bt_login);

        et_Username = (EditText)findViewById(R.id.edt_username);
        et_Pass = (EditText)findViewById(R.id.edt_password);

        listenArrayMessage = new ArrayList<>();
        networkThread = new Thread(new NetworkThread());
        networkThread.start();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String LoginMess = "11" + et_Username.getText().toString() + "/**/" + et_Pass.getText().toString();
                SendMessage(LoginMess);
                String ReceiveData = "";
                while (ReceiveData.isEmpty()) {
                    ReceiveData = GetMessage();
                }

                if (ReceiveData.startsWith("01")) {
                    Intent intent = new Intent(Login.this, ChooseGame.class);
                    startActivity(intent);
                }
                else if (ReceiveData.startsWith("00")) {
                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                }
                else if (ReceiveData.startsWith("02")) {
                    Toast.makeText(getApplicationContext(), "Tài khoản đang online", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "333333333333333333", Toast.LENGTH_LONG).show();
                }
            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class );
                startActivity(intent);
            }
        });
    }

    class NetworkThread implements Runnable {
        @Override
        public void run() {
            try {
                clientSocket = new Socket("192.168.100.7", 8080);
                output = new PrintWriter(clientSocket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                new Thread(new ListenThread()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ListenThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    String listenMessage = input.readLine();
                    if (!listenMessage.isEmpty()) {
                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listenArrayMessage.add(listenMessage);
                            }
                        });*/
                        listenArrayMessage.add(listenMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SendThread implements Runnable {
        String message;
        SendThread(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            output.write(message + "\r\n");
            output.flush();

        }
    }

    public void SendMessage (String message) {
        if (!message.isEmpty()) {
            new Thread(new SendThread(message)).start();
        }
    }

    public String GetMessage () {
        if (!listenArrayMessage.isEmpty()) {
            return listenArrayMessage.get(0);
        }
        else {
            return "";
        }
    }
}