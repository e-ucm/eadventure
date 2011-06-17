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

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;

@Singleton
public class BasicSceneElementRenderer implements
		GameObjectRenderer<Graphics2D, SceneElementGO<?>> {

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
	 * float)
	 */
	@Override
	public void render(Graphics2D g, SceneElementGO<?> basicSceneElement,
			float interpolation, int offsetX, int offsetY) {
		factory.render(g, basicSceneElement.getAsset(), basicSceneElement.getPosition(), basicSceneElement.getScale(), offsetX, offsetY);
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
	public void render(Graphics2D g, SceneElementGO<?> basicSceneElement,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		factory.render(g, basicSceneElement.getAsset(), position, scale
				* basicSceneElement.getScale(), offsetX, offsetY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.GameObjectRenderer#contains(es
	 * .eucm.eadventure.engine.core.gameobjects.GameObject, int, int)
	 */
	@Override
	public boolean contains(SceneElementGO<?> basicSceneElement, int virutalX,
			int virtualY) {
		int x = (int) ((virutalX - basicSceneElement.getPosition().getJavaX(
				basicSceneElement.getWidth() * basicSceneElement.getScale())) / basicSceneElement
				.getScale());
		int y = (int) ((virtualY - basicSceneElement.getPosition().getJavaY(
				basicSceneElement.getHeight() * basicSceneElement.getScale())) / basicSceneElement
				.getScale());
		return x > 0 && y > 0
				&& factory.contains(x, y, basicSceneElement.getAsset());
	}

}