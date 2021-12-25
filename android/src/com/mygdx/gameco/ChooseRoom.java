package com.mygdx.gameco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseRoom extends Activity {

    GridView gv_ListRoom;
    ImageButton bt_Back;
    ImageButton bt_Refresh;
    ImageButton bt_Create;
    ImageButton bt_Rank;

    ListRoomAdapter listRoomAdapter;
    List<Room> listRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);

        Init();

        GetRoomInfo();

        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_back = new Intent(ChooseRoom.this, ChooseGame.class);
                startActivity(intent_back); }
        });

        bt_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.getLoginActivity().SendMessage("200");

                String ReceiveData = "";
                while (ReceiveData.isEmpty()) {
                    ReceiveData = Login.getLoginActivity().GetMessage();
                }

                if (ReceiveData.startsWith("001")) {
                    ReceiveData = ReceiveData.replaceFirst("001", "");
                    Intent intent_join_room = new Intent(ChooseRoom.this, Waiting_Room.class);
                    Room room = new Room(ReceiveData, 1);
                    intent_join_room.putExtra("RoomID", ReceiveData);
                    intent_join_room.putExtra("Role", "host");
                    startActivity(intent_join_room);
                }

            }
        });

        bt_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetRoomInfo();
            }
        });

        bt_Rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        gv_ListRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Login.getLoginActivity().SendMessage("201" + listRoom.get(position).roomID);

                String ReceiveData = "";
                while (ReceiveData.isEmpty()) {
                    ReceiveData = Login.getLoginActivity().GetMessage();
                }

                if (ReceiveData.startsWith("210")) {
                    Intent intent_join_room = new Intent(ChooseRoom.this, Waiting_Room.class);
                    intent_join_room.putExtra("RoomID", listRoom.get(position).getRoomID());
                    intent_join_room.putExtra("Role", "player");
                    startActivity(intent_join_room);
                }
                else if (ReceiveData.startsWith("220")) {
                    Toast.makeText(getApplicationContext(), "Phòng không còn tồn tại", Toast.LENGTH_SHORT).show();
                    GetRoomInfo();
                }
                else if (ReceiveData.startsWith("221")) {
                    Toast.makeText(getApplicationContext(), "Phòng đã đầy, vui lòng chọn phòng khác", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void Init() {
        bt_Back = (ImageButton) findViewById(R.id.bt_back_A_choose_room);
        bt_Create = (ImageButton) findViewById(R.id.bt_create_A_choose_room);
        bt_Refresh = (ImageButton) findViewById(R.id.bt_refresh_A_choose_room);
        bt_Rank = (ImageButton) findViewById(R.id.bt_rank_A_choose_room);
        gv_ListRoom = (GridView) findViewById(R.id.gv_listgame_A_choose_room);
        listRoom = new ArrayList<>();
        listRoomAdapter = new ListRoomAdapter(this, R.layout.item_room, listRoom);
        gv_ListRoom.setAdapter(listRoomAdapter);
    }

    public void GetRoomInfo() {
        Login.getLoginActivity().SendMessage("222");

        String ReceiveData = "";
        while (ReceiveData.isEmpty()) {
            ReceiveData = Login.getLoginActivity().GetMessage();
        }

        ReceiveData = ReceiveData.replaceFirst("222", "");

        if (ReceiveData.contains("/**/")) {
            String[] listStringRoom = ReceiveData.split("\\/\\*\\*\\/");
            listRoom.clear();

            for (int i = 0; i < listStringRoom.length; i += 3) {
                listRoom.add(new Room(listStringRoom[i], Integer.parseInt(listStringRoom[i + 1]), listStringRoom[i + 2]));
            }

            listRoomAdapter.notifyDataSetChanged();
        }
    }
}