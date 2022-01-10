package com.mygdx.gameco;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Semaphore;

public class Waiting_Room extends AppCompatActivity {

    TextView tv_roomname;
    TextView tv_nameHost;
    TextView tv_namePlayer;
    TextView tv_ready;
    Button bt_Start;
    Button bt_Invite;
    Button bt_Kick;
    ImageButton bt_Back;
    String role;
    String roomID;
    Room room;
    Boolean gameStart;

    Semaphore semaphore;

    Intent start_game_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        getSupportActionBar().hide();

        Init();

        bt_Kick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (room.getNumberPlayer() == 2) {
                    Login.getLoginActivity().SendMessage("303" + roomID);
                }
            }
        });

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
                try {
                    semaphore.acquire();
                    while (ReceiveData.isEmpty()) {
                        ReceiveData = Login.getLoginActivity().GetMessage("001");
                    }
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (ReceiveData.startsWith("001")) {
                    finish();
                    overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void Init() {
        tv_roomname = (TextView) findViewById(R.id.tv_roomname_A_waiting_room);
        tv_nameHost = (TextView) findViewById(R.id.tv_player1_A_waiting_room);
        tv_namePlayer = (TextView) findViewById(R.id.tv_player2_A_waiting_room);
        tv_ready = (TextView) findViewById(R.id.tv_ready_A_waiting_room);
        bt_Start = (Button) findViewById(R.id.bt_start_A_waiting_room);
        bt_Invite = (Button) findViewById(R.id.bt_invite_A_waiting_room);
        bt_Kick = (Button) findViewById(R.id.bt_kick_A_waiting_room);
        bt_Back = (ImageButton) findViewById(R.id.bt_back_A_waiting_room);
        start_game_intent = new Intent(Waiting_Room.this, AndroidLauncher.class);

        semaphore = new Semaphore(1);

        Intent intent = getIntent();
        roomID = intent.getStringExtra("RoomID");
        role = intent.getStringExtra("Role");

        gameStart = false;

        SendGetInfo();
        GetRoomInfo();

        tv_ready.setVisibility(View.INVISIBLE);

        if (role.equals("player")) {
            room = new Room(roomID, 2);
            bt_Kick.setActivated(false);
            bt_Kick.setVisibility(View.INVISIBLE);
            bt_Invite.setActivated(false);
            bt_Invite.setVisibility(View.INVISIBLE);
            bt_Start.setText("Sẵn sàng");
        }
        else {
            room = new Room(roomID, 1);
            bt_Start.setActivated(false);
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

                String roomInfoMessage = "";
                String startInfoMessage = "";
                String kickInfoMessage = "";

                try {
                    semaphore.acquire();
                    roomInfoMessage = Login.getLoginActivity().GetMessage("310");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    semaphore.acquire();
                    startInfoMessage = Login.getLoginActivity().GetMessage("300");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    semaphore.acquire();
                    kickInfoMessage = Login.getLoginActivity().GetMessage("303");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (roomInfoMessage.length() != 0) {
                    roomInfoMessage = roomInfoMessage.replaceFirst("310", "");
                    String[] listStringRoom = roomInfoMessage.split("\\/\\*\\*\\/");
                    room.setNameHost(listStringRoom[1]);

                    if (!listStringRoom[2].equals("unknown")) {
                        room.setNamePlayer(listStringRoom[2]);
                        room.setNumberPlayer(2);
                    }
                    else {
                        role = "host";
                        room.setNamePlayer("Player2");
                        room.setNumberPlayer(1);
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
                if (startInfoMessage.length() != 0) {
                    gameStart = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            start_game_intent.putExtra("RoomID", room.getRoomID());
                            if (role.equals("host")) {
                                start_game_intent.putExtra("Username", room.getNameHost());
                                start_game_intent.putExtra("Opponentname", room.getNamePlayer());
                                start_game_intent.putExtra("Gofirst", true);
                            }
                            else {
                                start_game_intent.putExtra("Username", room.getNamePlayer());
                                start_game_intent.putExtra("Opponentname", room.getNameHost());
                                start_game_intent.putExtra("Gofirst", false);
                            }

                            startActivityForResult(start_game_intent, 1111);
                        }
                    });
                }

                if (kickInfoMessage.length() != 0) {
                    gameStart = true;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent_outroom = new Intent(Waiting_Room.this, ChooseRoom.class);
                            startActivity(intent_outroom);
                            overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
                            Toast.makeText(getApplicationContext(), "Bạn đã bị đuổi khỏi phòng", Toast.LENGTH_SHORT).show();
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
            if (room.getPlayerReady()) {
                tv_ready.setVisibility(View.VISIBLE);
                bt_Start.setActivated(true);
            }
            else {
                tv_ready.setVisibility(View.INVISIBLE);
                bt_Start.setActivated(false);
            }
        }
        tv_roomname.setText(room.getRoomName());
        tv_nameHost.setText(room.getNameHost());
        tv_namePlayer.setText(room.getNamePlayer());
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (role.equals("player")) {
            bt_Start.setText("Sẵn sàng");
        }

        room.setPlayerReady(false);
        gameStart = false;
        updateScreen();
        GetRoomInfo();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        gameStart = true;
        Login.getLoginActivity().SendMessage("202" + roomID);

        String ReceiveData = "";
        try {
            semaphore.acquire();
            while (ReceiveData.isEmpty()) {
                ReceiveData = Login.getLoginActivity().GetMessage("001");
            }
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ReceiveData.startsWith("001")) {
            finish();
            overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
        }
    }
}
