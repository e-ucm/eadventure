/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.eadventure.engine.core.platform.impl.gameobjectrenderers;

import java.awt.Graphics2D;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

@Singleton
public class BasicSceneElementRenderer extends
		GameObjectRendererImpl<SceneElementGO<?>> {

	/**
	 * The {@link GraphicRendererFactor} used to display the elements in the
	 * graphic context
	 */
	private GraphicRendererFactory<Graphics2D> factory;

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger("BasicSceneElementRenderer");

	@SuppressWarnings("unchecked")
	@Inject
	public BasicSceneElementRenderer(GraphicRendererFactory<?> factory) {
		this.factory = (GraphicRendererFactory<Graphics2D>) factory;
		logger.info("New instance");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.GameObjectRenderer#render(java
	 * .lang.Object, es.eucm.eadventure.engine.core.gameobjects.GameObject,
	 * es.eucm.eadventure.common.model.params.EAdPosition, float)
	 */
	@Override
	public void render(Graphics2D g, SceneElementGO<?> e,
			EAdTransformation transformation) {
		factory.render(prepareGraphics(g, transformation), e.getRenderAsset());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.GameObjectRenderer#contains(es
	 * .eucm.eadventure.engine.core.gameobjects.GameObject, int, int)
	 */
	@Override
	public boolean contains(SceneElementGO<?> e, int virtualX, int virtualY,
			EAdTransformation transformation) {
		if (transformation.isVisible()) {
			float[] r = transformation.getMatrix().postMultiplyPoint(virtualX,
					virtualY);
			int x = (int) r[0];
			int y = (int) r[1];
			logger.info("Pos: ( " + x + ", " + y + " )");

			DrawableAsset<?> renderAsset = e.getRenderAsset();
			if (x > 0 && y > 0 && x < renderAsset.getWidth()
					&& y < renderAsset.getHeight())
				return factory.contains(x, y, renderAsset);
		}
		return false;
	}

}
