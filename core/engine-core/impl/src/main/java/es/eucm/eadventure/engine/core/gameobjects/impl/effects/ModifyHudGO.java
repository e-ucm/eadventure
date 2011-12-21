package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.hud.ModifyHUDEf;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.game.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.go.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.BasicHUD;
import es.eucm.eadventure.engine.core.gameobjects.impl.effects.sceneelement.SceneElementEffectGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

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
		if (element.isAdd()) {
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
