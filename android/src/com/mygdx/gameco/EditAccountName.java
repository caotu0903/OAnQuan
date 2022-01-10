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

public class EditAccountName extends AppCompatActivity {

    EditText et_account_name;
    ImageButton bt_back;
    Button bt_change_account_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_name);
        getSupportActionBar().hide();

        Init();

        bt_change_account_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = et_account_name.getText().toString().trim();
                if (accountName.length() != 0) {
                    if (AccountName_Validation(accountName)) {
                        String changeAccountNameMessage = "103" + accountName;
                        Login.getLoginActivity().SendMessage(changeAccountNameMessage);

                        String ReceiveData = "";
                        while (ReceiveData.length() == 0) {
                            ReceiveData = Login.getLoginActivity().GetMessage("103");
                        }

                        ReceiveData = ReceiveData.replaceFirst("103", "");

                        if (ReceiveData.startsWith("001")) {
                            finish();
                            overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                            Toast.makeText(getApplicationContext(), "Đã thay đổi tên hiển thị", Toast.LENGTH_SHORT).show();
                        }
                        else if (ReceiveData.startsWith("003")) {
                            Toast.makeText(getApplicationContext(), "Tên hiển thị này đã tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Tên hiển thị không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tên hiển thị muốn đổi", Toast.LENGTH_SHORT).show();
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
        et_account_name = (EditText) findViewById(R.id.et_accountname_A_edit_account_name);
        bt_back = (ImageButton) findViewById(R.id.bt_back_A_edit_account_name);
        bt_change_account_name = (Button) findViewById(R.id.bt_change_account_name_A_edit_account_name);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }
}