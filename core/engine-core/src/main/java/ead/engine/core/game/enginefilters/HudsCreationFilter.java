package ead.engine.core.game.enginefilters;

import java.util.List;

import ead.engine.core.game.Game;
import ead.engine.core.game.GameImpl;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.huds.BottomHUD;
import ead.engine.core.gameobjects.huds.EffectsHUD;
import ead.engine.core.gameobjects.huds.HudGO;
import ead.engine.core.gameobjects.huds.MenuHUD;
import ead.engine.core.gameobjects.huds.TopBasicHUDImpl;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class HudsCreationFilter extends AbstractEngineFilter<List<HudGO>> {

	public HudsCreationFilter() {
		super(0);
	}

	@Override
	public List<HudGO> filter(List<HudGO> o, Object[] params) {
		AssetHandler assetHandler = (AssetHandler) params[0];
		SceneElementGOFactory sceneElementFactory = (SceneElementGOFactory) params[1];
		GUI gui = (GUI) params[2];
		GameState gameState = (GameState) params[3];
		EventGOFactory eventFactory = (EventGOFactory) params[4];
		Game g = (Game) params[5];
		// Bottom HUD
		o.add(new BottomHUD(assetHandler, sceneElementFactory, gui, gameState,
				eventFactory));

		// gui.addHud(inventoryHUD, 1);

		// Menu HUD
		MenuHUD menuHud = new MenuHUD(assetHandler, sceneElementFactory, gui,
				gameState, eventFactory);
		o.add(menuHud);
		g.addFilter(GameImpl.FILTER_PROCESS_ACTION, menuHud);

		// Effects HUD
		EffectsHUD effectHUD = new EffectsHUD(assetHandler,
				sceneElementFactory, gui, gameState, eventFactory);
		o.add(effectHUD);

		TopBasicHUDImpl topBasicHUD = new TopBasicHUDImpl(assetHandler,
				sceneElementFactory, gui, gameState, eventFactory);
		o.add(topBasicHUD);
		return o;
	}

}
