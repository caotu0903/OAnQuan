package com.mygdx.gameco.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.gameco.OAnQuanGame;
import com.mygdx.gameco.OperationNetwork;

public class DesktopLauncher {
	public static void main (String[] arg) {
		OperationNetwork operationNetwork = null;
		String roomID = null, userName = null, opponentName = null;
		boolean canGo = true;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 720;
		config.width = 1280;
		new LwjglApplication(new OAnQuanGame(operationNetwork,roomID, userName,opponentName,canGo), config);
	}
}
