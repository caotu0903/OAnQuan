package com.mygdx.gameco;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Waiting_Room extends AppCompatActivity {

    TextView tv_roomname;
    TextView tv_nameHost;
    TextView tv_namePlayer;
    Button bt_Start;
    Button bt_Invite;
    Button bt_Kick;
    ImageButton bt_Back;
    String role;
    String roomID;
    Room room;
    Boolean gameStart;

    Intent start_game_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        getSupportActionBar().hide();

        Init();

        bt_Start.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (bt_Start.getText().equals("Sẵn sàng")) {
                    Login.getLoginActivity().SendMessage("301" + roomID);
                    bt_Start.setText("Bỏ sẵn sàng");
                }
                else if (bt_Start.getText().equals("Bỏ sẵn sàng")) {
                    Login.getLoginActivity().SendMessage("302" + roomID);
                    bt_Start.setText("Sẵn sàng");
                }
                else if (bt_Start.getText().equals("Bắt đầu")) {
                    if (room.getPlayerReady()) {
                        Login.getLoginActivity().SendMessage("300" + roomID);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Vui lòng đợi người chơi còn lại sẵn sàng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        bt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStart = true;
                Login.getLoginActivity().SendMessage("202" + roomID);

                String ReceiveData = "";
                while (ReceiveData.isEmpty()) {
                    ReceiveData = Login.getLoginActivity().GetMessage("001");
                }

                if (ReceiveData.startsWith("001")) {
                    finish();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void Init() {
        tv_roomname = (TextView) findViewById(R.id.tv_roomname_A_waiting_room);
        tv_nameHost = (TextView) findViewById(R.id.tv_player1_A_waiting_room);
        tv_namePlayer = (TextView) findViewById(R.id.tv_player2_A_waiting_room);
        bt_Start = (Button) findViewById(R.id.bt_start_A_waiting_room);
        bt_Invite = (Button) findViewById(R.id.bt_invite_A_waiting_room);
        bt_Kick = (Button) findViewById(R.id.bt_kick_A_waiting_room);
        bt_Back = (ImageButton) findViewById(R.id.bt_back_A_waiting_room);
        start_game_intent = new Intent(Waiting_Room.this, AndroidLauncher.class);

        Intent intent = getIntent();
        roomID = intent.getStringExtra("RoomID");
        role = intent.getStringExtra("Role");

        gameStart = false;

        SendGetInfo();
        GetRoomInfo();

        if (role.equals("player")) {
            room = new Room(roomID, 1);
            bt_Kick.setActivated(false);
            bt_Kick.setVisibility(View.INVISIBLE);
            bt_Invite.setActivated(false);
            bt_Invite.setVisibility(View.INVISIBLE);
            bt_Start.setText("Sẵn sàng");
        }
        else {
            room = new Room(roomID, 2);
        }
    }

    public void SendGetInfo() {
        Login.getLoginActivity().SendMessage("310" + roomID);
    }

    public void GetRoomInfo() {
        new Thread(new WaitingRoomInfo()).start();
    }

    public class WaitingRoomInfo implements Runnable {

        @Override
        public void run() {
            while (!gameStart) {
                String roomInfoMessage = Login.getLoginActivity().GetMessage("310");
                String startInfoMessage = Login.getLoginActivity().GetMessage("300");
                if (!roomInfoMessage.isEmpty()) {
                    roomInfoMessage = roomInfoMessage.replaceFirst("310", "");
                    String[] listStringRoom = roomInfoMessage.split("\\/\\*\\*\\/");
                    room.setNameHost(listStringRoom[1]);
                    if (!listStringRoom[2].equals("unknown")) {
                        room.setNamePlayer(listStringRoom[2]);
                    }
                    else {
                        role = "host";
                        room.setNamePlayer("Player2");
                    }
                    if (listStringRoom[3].equals("true")) {
                        room.setPlayerReady(true);
                    }
                    else
                    {
                        room.setPlayerReady(false);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateScreen();
                        }
                    });
                }
                if (!startInfoMessage.isEmpty()) {
                    gameStart = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            start_game_intent.putExtra("RoomID", room.getRoomID());
                            if (role.equals("host")) {
                                start_game_intent.putExtra("Username", room.getNameHost());
                                start_game_intent.putExtra("Opponentname", room.getRoomID());
                                start_game_intent.putExtra("Gofirst", true);
                            }
                            else {
                                start_game_intent.putExtra("Username", room.getNameHost());
                                start_game_intent.putExtra("Opponentname", room.getRoomID());
                                start_game_intent.putExtra("Gofirst", false);
                            }

                            startActivity(start_game_intent);
                        }
                    });
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateScreen() {
        if (role.equals("host")) {
            bt_Kick.setActivated(true);
            bt_Kick.setVisibility(View.VISIBLE);
            bt_Invite.setActivated(true);
            bt_Invite.setVisibility(View.VISIBLE);
            bt_Start.setText("Bắt đầu");
        }
        tv_roomname.setText(room.getRoomName());
        tv_nameHost.setText(room.getNameHost());
        tv_namePlayer.setText(room.getNamePlayer());
    }
}
