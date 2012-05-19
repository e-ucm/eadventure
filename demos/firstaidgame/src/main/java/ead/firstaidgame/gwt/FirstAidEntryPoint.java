package ead.firstaidgame.gwt;

import com.google.gwt.core.client.EntryPoint;

import ead.engine.playn.html.GWTGame;

public class FirstAidEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		GWTGame game = new GWTGame("firstaid");
		game.launch();
	}

}
