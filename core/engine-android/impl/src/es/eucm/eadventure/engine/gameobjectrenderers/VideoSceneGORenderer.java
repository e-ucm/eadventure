package es.eucm.eadventure.engine.gameobjectrenderers;

import android.graphics.Canvas;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.impl.VideoSceneGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;

@Singleton
public class VideoSceneGORenderer implements GameObjectRenderer<Canvas, VideoSceneGO> {

	@Override
	public void render(Canvas graphicContext, VideoSceneGO object,
			float interpolation, int offsetX, int offsetY) {
		// Do nothing
	}

	@Override
	public void render(Canvas graphicContext, VideoSceneGO object,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		// Do nothing
	}

	@Override
	public boolean contains(VideoSceneGO object, int virtualX, int virtualY) {
		return true;
	}

}
