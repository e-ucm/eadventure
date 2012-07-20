package ead.firstaidgame.desktop;

import java.util.Map;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.params.text.EAdString;
import ead.engine.desktop.DesktopGame;
import ead.reader.PropertiesReader;
import ead.reader.java.DefaultStringFileHandler;
import ead.reader.java.EAdAdventureDOMModelReader;
import ead.reader.java.PropertiesReaderImpl;

public class FirstAid {

	public static void main(String args[]) {
		
		DesktopGame game = new DesktopGame();
		game.launch(60, false);

		EAdAdventureDOMModelReader reader = new EAdAdventureDOMModelReader();
		EAdAdventureModel model = reader
				.read("src/main/resources/ead/engine/resources/datad.xml");
		DefaultStringFileHandler stringsHandler = new DefaultStringFileHandler();

		Map<EAdString, String> strings = stringsHandler
				.read("src/main/resources/ead/engine/resources/strings.xml");
		
		PropertiesReader propertiesReader = new PropertiesReaderImpl( );
		propertiesReader.setProperties(model, "ead/engine/resources/ead.properties");
		
		game.setGame(model, strings, null);

	}

}
