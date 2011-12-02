package es.eucm.eadventure.engine.html;

import es.eucm.eadventure.common.elementfactories.EAdElementsFactory;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.reader.GWTReader;
import es.eucm.eadventure.engine.reader.GWTStringReader;

public class EAdEngineHTMLProtocol extends EAdEngineHtml {

	public Game loadGame() {
		Game game = injector.getGame();
		game.loadGame();

		GWTReader gwtReader = new GWTReader();
		// gwtReader.readXML("eadengine/binary/sceneDemo.xml", game);
		gwtReader.readXML("eadengine/binary/data.xml", game);

		//game.setGame(model, chapter);

		// String handler after creating the scene
		StringHandler stringHandler = injector.getStringHandler();
		stringHandler.addStrings(EAdElementsFactory.getInstance()
				.getStringFactory().getStrings());
		GWTStringReader stringReader = new GWTStringReader();
		stringReader.readXML("eadengine/values/strings.xml", stringHandler);
		
		return game;
	}
}
