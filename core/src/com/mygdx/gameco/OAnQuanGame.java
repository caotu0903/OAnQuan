package com.mygdx.gameco;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.net.Socket;

public class OAnQuanGame extends Game implements InputProcessor {

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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

}
