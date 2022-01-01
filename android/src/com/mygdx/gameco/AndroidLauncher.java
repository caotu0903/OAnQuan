package com.mygdx.gameco;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	public OAnQuanGame oAnQuanGame;
	OperationNetwork operationNetwork;
	String roomID, userName, opponentName;
	boolean canGo;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		Intent intent = this.getIntent();
		roomID = intent.getStringExtra("RoomID");
		userName = intent.getStringExtra("Username");
		opponentName = intent.getStringExtra("Opponentname");
		canGo = intent.getBooleanExtra("Gofirst", true);

		operationNetwork = new OperationNetwork() {
			@Override
			public String GetMessage() {
				return Login.getLoginActivity().GetMessage();
			}

			@Override
			public void SendMessage(String message) {
				Login.getLoginActivity().SendMessage(message);
			}

			@Override
			public void CallFinish() {
				finish();
			}
		};

		oAnQuanGame = new OAnQuanGame(operationNetwork, roomID, userName, opponentName, canGo);
		initialize(oAnQuanGame, config);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	public void SendMessage(String message) {
		Login.getLoginActivity().SendMessage(message);
	}


}
