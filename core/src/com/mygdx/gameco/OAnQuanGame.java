package com.mygdx.gameco;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.net.Socket;

public class OAnQuanGame extends Game {

	GameScreen gameScreen;
	private final OperationNetwork operationNetwork;
	private final String roomID, userName, opponentName;
	private final boolean canGo;

	public OAnQuanGame(OperationNetwork operationNetwork, String roomID, String userName, String opponentName, boolean canGo) {
		this.operationNetwork = operationNetwork;
		this.roomID = roomID;
		this.userName = userName;
		this.opponentName = opponentName;
		this.canGo = canGo;
	}

	@Override
	public void create() {
		gameScreen = new GameScreen(operationNetwork, roomID, userName, opponentName, canGo);
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		gameScreen.dispose();

	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}


}
