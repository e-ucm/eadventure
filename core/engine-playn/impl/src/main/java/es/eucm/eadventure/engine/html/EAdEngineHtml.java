package es.eucm.eadventure.engine.html;

import es.eucm.eadventure.engine.core.EAdEngine;
import playn.core.PlayN;
import playn.html.HtmlAssetManager;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

public class EAdEngineHtml extends HtmlGame {

	@Override
	public void start() {
	    HtmlAssetManager assets = HtmlPlatform.register().assetManager();
	    assets.setPathPrefix("eAd/");
	    PlayN.run(new EAdEngine());
	}

}
