package es.eucm.eadventure.engine.gameobjectrenderers;

import android.graphics.Canvas;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;

@Singleton
public class SceneGORenderer implements GameObjectRenderer<Canvas, SceneGO<?>> {

	@Override
	public void render(Canvas graphicContext, SceneGO<?> object,
			float interpolation, int offsetX, int offsetY) {
		// Do nothing
	}

	@Override
	public void render(Canvas graphicContext, SceneGO<?> object,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		// Do nothing
	}

	@Override
	public boolean contains(SceneGO<?> object, int virtualX, int virtualY) {
		return false;
	}

}
