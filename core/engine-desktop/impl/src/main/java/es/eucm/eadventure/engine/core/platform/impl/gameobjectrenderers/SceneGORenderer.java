package es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers;

import java.awt.Graphics2D;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;

@Singleton
public class SceneGORenderer implements GameObjectRenderer<Graphics2D, SceneGO<?>> {

	@Override
	public void render(Graphics2D graphicContext, SceneGO<?> object,
			float interpolation, int offsetX, int offsetY) {
		// Do nothing
	}

	@Override
	public void render(Graphics2D graphicContext, SceneGO<?> object,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		// Do nothing
	}

	@Override
	public boolean contains(SceneGO<?> object, int virtualX, int virtualY) {
		return false;
	}

}
