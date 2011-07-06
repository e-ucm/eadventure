package es.eucm.eadventure.engine.gameobjectrenderers;

import java.util.logging.Logger;

import android.graphics.Canvas;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.TransitionGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;

@Singleton
public class TransitionGORenderer  implements GameObjectRenderer<Canvas, TransitionGO> {

	/**
	 * The {@link GraphicRendererFactor} used to display the elements in the
	 * graphic context
	 */
	private GraphicRendererFactory<Canvas> factory;

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger("TransitionGORenderer");

	@SuppressWarnings("unchecked")
	@Inject
	public TransitionGORenderer(GraphicRendererFactory<?> factory) {
		this.factory = (GraphicRendererFactory<Canvas>) factory;
		logger.info("New instance");
	}

	@Override
	public void render(Canvas graphicContext, TransitionGO object,
			float interpolation, int offsetX, int offsetY) {
		//Do nothing?
	}
	
	@Override
	public void render(Canvas graphicContext, TransitionGO object,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		if (object.getBackground() != null)
			factory.render(graphicContext, object.getBackground(), EAdPosition.volatileEAdPosition(0, 0), scale, offsetX, offsetY);
	}
	
	@Override
	public boolean contains(TransitionGO object, int virtualX, int virtualY) {
		return false;
	}
	
}
