package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPassword extends AppCompatActivity {

    ImageButton bt_back;
    EditText et_current_password;
    EditText et_new_password;
    EditText et_confirm_password;
    Button bt_change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        getSupportActionBar().hide();

        Init();

        bt_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = et_current_password.getText().toString().trim();
                String newPassword = et_new_password.getText().toString().trim();
                String confirmPassword = et_confirm_password.getText().toString().trim();

                if (currentPassword.length() != 0 && newPassword.length() != 0 && confirmPassword.length() != 0) {
                    if (Password_Validation(newPassword)) {
                        if (newPassword.equals(confirmPassword)) {
                            currentPassword = Login.getLoginActivity().EncryptBase64(currentPassword);
                            newPassword = Login.getLoginActivity().EncryptBase64(newPassword);
                            String changePasswordMessage = "104" + currentPassword + "/**/" + newPassword;
                            Login.getLoginActivity().SendMessage(changePasswordMessage);

                            String ReceiveData = "";
                            while(ReceiveData.length() == 0) {
                                ReceiveData = Login.getLoginActivity().GetMessage("104");
                            }

                            ReceiveData = ReceiveData.replaceFirst("104", "");

                            if (ReceiveData.startsWith("001")) {
                                finish();
                                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                                Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            }
                            else if (ReceiveData.startsWith("000")) {
                                Toast.makeText(getApplicationContext(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Xác nhận mật khẩu sai", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Mật khẩu mới không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
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
        bt_back = (ImageButton) findViewById(R.id.bt_back_A_edit_password);
        et_current_password = (EditText) findViewById(R.id.et_current_password_A_edit_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password_A_edit_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password_A_edit_password);
        bt_change_password = (Button) findViewById(R.id.bt_change_password_A_edit_password);
    }

    public static boolean Password_Validation(String password)
    {
        if(password.length()>=8)
        {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();
        }
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }
}