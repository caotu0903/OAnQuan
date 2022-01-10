package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends Activity {

    Button bt_SignUp;
    EditText et_Accountname;
    EditText et_Username;
    EditText et_Email;
    EditText et_Password;
    EditText et_Repassword;
    ImageButton bt_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Init();

        bt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = et_Accountname.getText().toString().trim();
                String userName = et_Username.getText().toString().trim();
                String email = et_Email.getText().toString().trim();
                String password = et_Password.getText().toString().trim();
                String repassword = et_Repassword.getText().toString().trim();
                if (!accountName.isEmpty() && !userName.isEmpty() && !email.isEmpty()
                        && !password.isEmpty() && !repassword.isEmpty()) {
                    if (AccountName_Validation(accountName)) {
                        if (Password_Validation(password)) {
                            if (password.equals(repassword)) {
                                password = Login.getLoginActivity().EncryptBase64(password);
                                String signUpMessage = "100" + userName + "/**/" + accountName + "/**/" + email + "/**/" + password;
                                Login.getLoginActivity().SendMessage(signUpMessage);
                                String ReceiveData = "";
                                while (ReceiveData.isEmpty()) {
                                    ReceiveData = Login.getLoginActivity().GetMessage();
                                }

                                if (ReceiveData.startsWith("001")) {
                                    Intent intent = new Intent(SignUp.this, Login.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                                } else if (ReceiveData.startsWith("002")) {
                                    Toast.makeText(getApplicationContext(), "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                                } else if (ReceiveData.startsWith("003")) {
                                    Toast.makeText(getApplicationContext(), "Tên hiển thị đã tồn tại", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Sai mật khẩu xác nhận", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Vui lòng sử dụng mật khẩu mạnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Tên hiển thị không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_to_login = new Intent(SignUp.this, Login.class);
                startActivity(back_to_login);
            }
        });
    }

    void Init() {
        bt_SignUp = (Button) findViewById(R.id.bt_signup_A_signup);
        et_Accountname = (EditText) findViewById(R.id.et_accountname_A_signup);
        et_Username = (EditText) findViewById(R.id.et_username_A_signup);
        et_Email = (EditText) findViewById(R.id.et_email_A_signup);
        et_Password = (EditText) findViewById(R.id.et_password_A_signup);
        et_Repassword = (EditText) findViewById(R.id.et_repassword_A_signup);
        bt_Back = (ImageButton)  findViewById(R.id.bt_back_A_singup);
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

    public static boolean Username_Validation(String username)
    {
        if(username.length()>=8)
        {
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasSpecial = special.matcher(username);

            return hasSpecial.find();
        }
        else
            return false;
    }

    public static boolean AccountName_Validation(String accountname)
    {
        if(accountname.length()<=12)
        {
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasSpecial = special.matcher(accountname);

            if (hasSpecial.find()) {
                return false;
            }
            else {
                return true;
            }
        }
        else
            return false;
    }
}