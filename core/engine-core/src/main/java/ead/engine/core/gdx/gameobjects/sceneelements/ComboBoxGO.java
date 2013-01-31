package ead.engine.core.gdx.gameobjects.sceneelements;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ead.common.model.elements.widgets.ComboBox;
import ead.engine.core.factories.EventGOFactory;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneElementGO;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;

public class ComboBoxGO extends SceneElementGO {

	private ComboBox comboBox;

	private SelectBox selectBox;

	public ComboBoxGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory,
				null);
		Skin s = null;
		selectBox = new SelectBox(new Object[0], s);
	}

}
