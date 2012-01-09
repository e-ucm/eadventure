package ead.engine.core.gameobjects.effects;

import com.google.inject.Inject;

import ead.common.model.elements.effects.hud.ModifyHUDEf;
import ead.common.resources.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.effects.sceneelement.SceneElementEffectGO;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.gameobjects.huds.BasicHUD;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;

public class ModifyHudGO extends SceneElementEffectGO<ModifyHUDEf> {

	private BasicHUD basicHUD;

	@Inject
	public ModifyHudGO(AssetHandler assetHandler, StringHandler stringsReader,
			SceneElementGOFactory sceneElementGOFactory, GUI gui,
			GameState gameState, BasicHUD basicHUD) {
		super(assetHandler, stringsReader, sceneElementGOFactory, gui,
				gameState);
		this.basicHUD = basicHUD;
	}

	public void initilize() {
		super.initilize();
		DrawableGO<?> drawable = this.sceneElementFactory.get(sceneElement);
		if (element.getAdd()) {
			if (!basicHUD.getContaintedGOs().contains(drawable))
				basicHUD.getContaintedGOs().add(drawable);
		} else
			basicHUD.getContaintedGOs().remove(drawable);
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
