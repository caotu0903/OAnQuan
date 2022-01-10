package com.mygdx.gameco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Rank extends AppCompatActivity {

    ListView lv_ListPlayerRank;

    ImageButton bt_back;
    ImageButton bt_refresh;

    ListRankAdapter listRankAdapter;
    List<PlayerRankItem> listPlayerRankItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        getSupportActionBar().hide();

        Init();

        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetRankInfo();
                Toast.makeText(getApplicationContext(), "Đã cập nhật", Toast.LENGTH_SHORT).show();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
            }
        });
    }

    public void Init() {
        bt_back = (ImageButton) findViewById(R.id.bt_back_A_rank);
        bt_refresh = (ImageButton) findViewById(R.id.bt_refresh_A_rank);
        lv_ListPlayerRank = (ListView) findViewById(R.id.lv_list_player_A_rank);
        listPlayerRankItem = new ArrayList<>();
        listRankAdapter = new ListRankAdapter(this, R.layout.item_rank, listPlayerRankItem);
        lv_ListPlayerRank.setAdapter(listRankAdapter);

        GetRankInfo();
    }

    public void GetRankInfo() {
        Login.getLoginActivity().SendMessage("500");

        String ReceiveData = "";

        while (ReceiveData.isEmpty()) {
            ReceiveData = Login.getLoginActivity().GetMessage("500");
        }

        ReceiveData = ReceiveData.replaceFirst("500", "");

        if (ReceiveData.contains("/**/")) {
            String[] listStringRoom = ReceiveData.split("\\/\\*\\*\\/");
            listPlayerRankItem.clear();

            for (int i = 0; i < listStringRoom.length; i += 3) {
                listPlayerRankItem.add(new PlayerRankItem(Integer.parseInt(listStringRoom[i]), listStringRoom[i + 1], Integer.parseInt(listStringRoom[i + 2])));
            }
        }
        else {
            listPlayerRankItem.clear();
        }
        listRankAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
    }
}