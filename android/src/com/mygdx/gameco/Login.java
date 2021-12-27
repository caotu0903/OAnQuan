package com.mygdx.gameco;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
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
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
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
    Button bt_Signup;
    Button bt_Login;
    Button bt_Test;

    public static WeakReference<Login> loginActivity;

    public static Login getLoginActivity() {
        return loginActivity.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bt_Signup = (Button)findViewById(R.id.bt_signup);
        bt_Login = (Button)findViewById(R.id.bt_login);

        et_Username = (EditText)findViewById(R.id.edt_username);
        et_Pass = (EditText)findViewById(R.id.edt_password);

        listenArrayMessage = new ArrayList<>();
        networkThread = new Thread(new NetworkThread());
        networkThread.start();

        loginActivity = new WeakReference<>(Login.this);

        bt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_Username.getText().toString().isEmpty()) {
                    if (!et_Pass.getText().toString().isEmpty()) {
                        String encryptPass = EncryptBase64(et_Pass.getText().toString().trim());
                        String LoginMess = "101" + et_Username.getText().toString().trim() + "/**/" + encryptPass;
                        SendMessage(LoginMess);
                        String ReceiveData = "";
                        while (ReceiveData.isEmpty()) {
                            ReceiveData = GetMessage();
                        }

                        if (ReceiveData.startsWith("001")) {
                            Intent intent = new Intent(Login.this, ChooseGame.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        } else if (ReceiveData.startsWith("000")) {
                            Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        } else if (ReceiveData.startsWith("002")) {
                            Toast.makeText(getApplicationContext(), "Tài khoản đang online", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tên tài khoản", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);

                startActivity(intent);
            }
        });
    }

    class NetworkThread implements Runnable {
        @Override
        public void run() {
            try {
                clientSocket = new Socket("192.168.100.8", 8080);
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
                        listenMessage = DecryptBase64(listenMessage);
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
            message = EncryptBase64(message);
            new Thread(new SendThread(message)).start();
        }
    }

    public String GetMessage (String startCode) {
        if (!listenArrayMessage.isEmpty() && listenArrayMessage.get(0).startsWith(startCode)) {
            String getMessage = listenArrayMessage.get(0);
            listenArrayMessage.remove(0);
            return getMessage;
        }
        else {
            return "";
        }
    }

    public String GetMessage () {
        if (!listenArrayMessage.isEmpty()) {
            String getMessage = listenArrayMessage.get(0);
            listenArrayMessage.remove(0);
            return getMessage;
        }
        else {
            return "";
        }
    }

    public String GetMessageNotStartWith (String startCode) {
        if (!listenArrayMessage.isEmpty() && !listenArrayMessage.get(0).startsWith(startCode)) {
            String getMessage = listenArrayMessage.get(0);
            listenArrayMessage.remove(0);
            return getMessage;
        }
        else {
            return "";
        }
    }

    public String EncryptBase64 (String input) {
        byte[] data = new byte[0];
        try {
            data = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        base64 = base64.replace("\n", "");
        return base64;
    }

    public String DecryptBase64 (String input) {
        byte[] data = Base64.decode(input, Base64.DEFAULT);
        String text = null;
        try {
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        text = text.replace("\n", "");
        return text;
    }
}