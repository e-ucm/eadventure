package es.eucm.eadventure.engine.html;

import playn.core.PlayN;

import com.google.gwt.core.client.GWT;

import es.eucm.eadventure.engine.core.EAdEngine;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.platform.impl.PlayNGinInjector;

public class EAdEngineHtml extends HtmlGame {

	private final static PlayNGinInjector injector = GWT.create(PlayNGinInjector.class);

	@Override
	public void start() {
	    HtmlAssetManager assets = HtmlPlatform.register().assetManager();
	    assets.setPathPrefix("eAd/");

	    injector.getPlatformLauncher();
	    Game game = injector.getGame();
	    game.loadGame();
	   
	    PlayN.run(new EAdEngine(game));
	}

}
