package ead.firstaidgame.desktop;

import java.util.Map;

import ead.common.model.elements.EAdAdventureModel;
import ead.common.params.text.EAdString;
import ead.common.reader.EAdAdventureDOMModelReader;
import ead.common.strings.DefaultStringFileHandler;
import ead.engine.desktop.DesktopGame;

public class FirstAid {

	public static void main(String args[]) {

		EAdAdventureDOMModelReader reader = new EAdAdventureDOMModelReader();
		EAdAdventureModel model = reader
				.read("src/main/resources/ead/resources/data.xml");
		DefaultStringFileHandler stringsHandler = new DefaultStringFileHandler();

		Map<EAdString, String> strings = stringsHandler
				.read("src/main/resources/ead/resources/strings.xml");
		DesktopGame game = new DesktopGame(model, strings);
		game.launch(60, false);

	}

}
