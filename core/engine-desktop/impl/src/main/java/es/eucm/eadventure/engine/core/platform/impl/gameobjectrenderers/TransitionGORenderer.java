package es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers;

import java.awt.Graphics2D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.TransitionGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;

@Singleton
public class TransitionGORenderer  implements GameObjectRenderer<Graphics2D, TransitionGO> {

	/**
	 * The {@link GraphicRendererFactor} used to display the elements in the
	 * graphic context
	 */
	private GraphicRendererFactory<Graphics2D> factory;

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TransitionGORenderer.class);

	@SuppressWarnings("unchecked")
	@Inject
	public TransitionGORenderer(GraphicRendererFactory<?> factory) {
		this.factory = (GraphicRendererFactory<Graphics2D>) factory;
		logger.info("New instance");
	}

	@Override
	public void render(Graphics2D graphicContext, TransitionGO object,
			float interpolation, int offsetX, int offsetY) {
		//Do nothing?
	}
	
	@Override
	public void render(Graphics2D graphicContext, TransitionGO object,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		if (object.getBackground() != null)
			factory.render(graphicContext, object.getBackground(), EAdPosition.volatileEAdPosition(0, 0), scale, offsetX, offsetY);
	}
	
	@Override
	public boolean contains(TransitionGO object, int virtualX, int virtualY) {
		return false;
	}
	
}
