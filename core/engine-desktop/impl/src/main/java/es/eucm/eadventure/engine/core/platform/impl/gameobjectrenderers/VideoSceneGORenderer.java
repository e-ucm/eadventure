package es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers;

import java.awt.Graphics2D;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.impl.VideoSceneGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;

@Singleton
public class VideoSceneGORenderer implements GameObjectRenderer<Graphics2D, VideoSceneGO> {

	@Override
	public void render(Graphics2D graphicContext, VideoSceneGO object,
			float interpolation, int offsetX, int offsetY) {
		// Do nothing
	}

	@Override
	public void render(Graphics2D graphicContext, VideoSceneGO object,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		// Do nothing
	}

	@Override
	public boolean contains(VideoSceneGO object, int virtualX, int virtualY) {
		return true;
	}

}