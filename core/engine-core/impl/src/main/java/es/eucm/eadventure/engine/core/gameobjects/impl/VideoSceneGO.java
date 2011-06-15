package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.impl.EAdVideoScene;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.platform.assets.impl.SpecialAssetRenderer;

public class VideoSceneGO extends AbstractGameObject<EAdVideoScene> implements SceneGO<EAdVideoScene> {

	private static final Logger logger = Logger.getLogger("VideoScreenGOImpl");

	private SpecialAssetRenderer<Video, ?> specialAssetRenderer;
	
	@Inject
	public VideoSceneGO( SpecialAssetRenderer<Video, ?> specialAssetRenderer ) {
		logger.info("New instance");	
		this.specialAssetRenderer = specialAssetRenderer;
	}
	
	public void doLayout(int offsetX, int offsetY) {
		gui.showSpecialResource(specialAssetRenderer.getComponent((Video) element.getAsset(EAdVideoScene.video)), 0, 0, true);
	}
	
	@Override
	public boolean acceptsVisualEffects() {
		return false;
	}
	
	@Override
	public void update(GameState state) {
		super.update(state);
	}


	
}
