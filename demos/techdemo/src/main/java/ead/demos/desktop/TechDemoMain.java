package ead.demos.desktop;

import java.util.Map;

import ead.engine.core.game.GameImpl;
import ead.engine.core.game.enginefilters.AbstractEngineFilter;
import ead.engine.core.gdx.desktop.DesktopGame;
import ead.utils.Log4jConfig;

public class TechDemoMain {

	/*public static void main(String[] args) {
		DesktopGame g = new DesktopGame();
		InitScene scene = new InitScene();
		BasicChapter chapter = new BasicChapter(scene);
		EAdAdventureModel model = new BasicAdventureModel();
		model.getChapters().add(chapter);
		g.setModel(null);
		g.start();
	}*/

	public static void main(String args[]) {
		Log4jConfig.setLevel(null, Log4jConfig.Slf4jLevel.Debug);
		DesktopGame game = new DesktopGame();
		game
				.addFilter(GameImpl.FILTER_STRING_FILES,
						new StringEngineFilter(10));
		game.start();
	}

	public static class StringEngineFilter extends
			AbstractEngineFilter<Map<String, String>> {

		public StringEngineFilter(int priority) {
			super(priority);
		}

		@Override
		public Map<String, String> filter(Map<String, String> o, Object[] params) {
			o.put("@json/campus/conv_banco.xml", "");
			o.put("@json/campus/conv_elegir_grupo.xml", "");
			o.put("@json/pasillo/conv_pasillo.xml", "");
			o.put("@json/aula/conv_aula.xml", "");
			return o;
		}
	}

}
