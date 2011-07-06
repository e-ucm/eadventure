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
package es.eucm.eadventure.engine.gameobjectrenderers;

import java.util.logging.Logger;

import android.graphics.Canvas;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;

@Singleton
public class BasicSceneElementRenderer implements
		GameObjectRenderer<Canvas, SceneElementGO<?>> {

	/**
	 * The {@link GraphicRendererFactor} used to display the elements in the
	 * graphic context
	 */
	private GraphicRendererFactory<Canvas> factory;

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger("BasicSceneElementRenderer");

	@SuppressWarnings("unchecked")
	@Inject
	public BasicSceneElementRenderer(GraphicRendererFactory<?> factory) {
		this.factory = (GraphicRendererFactory<Canvas>) factory;
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
	public void render(Canvas g, SceneElementGO<?> basicSceneElement,
			float interpolation, int offsetX, int offsetY) {
		Canvas g2 = prepareGraphics( g, basicSceneElement );
		factory.render(g2, basicSceneElement.getAsset(), basicSceneElement.getPosition(), basicSceneElement.getScale(), offsetX, offsetY);
		g2.restore();
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
	public void render(Canvas g, SceneElementGO<?> basicSceneElement,
			EAdPosition position, float scale, int offsetX, int offsetY) {
		Canvas g2 = prepareGraphics( g, basicSceneElement );
		factory.render(g2, basicSceneElement.getAsset(), position, scale
				* basicSceneElement.getScale(), offsetX, offsetY);
		g2.restore();
	}
	
	private Canvas prepareGraphics( Canvas g, SceneElementGO<?> basicSceneElement ){
		g.save();
		float centerX = basicSceneElement.getPosition().getJavaX(basicSceneElement.getWidth()) + basicSceneElement.getWidth() / 2;
		float centerY = basicSceneElement.getPosition().getJavaY(basicSceneElement.getHeight()) + basicSceneElement.getHeight() / 2;
		g.rotate(basicSceneElement.getRotation(), centerX, centerY);
		//TODO composite!
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, basicSceneElement.getAlpha()));
		return g;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.GameObjectRenderer#contains(es
	 * .eucm.eadventure.engine.core.gameobjects.GameObject, int, int)
	 */
	@Override
	public boolean contains(SceneElementGO<?> basicSceneElement, int virtualX,
			int virtualY) {
		
		int centerX = basicSceneElement.getCenterX();
		int centerY = basicSceneElement.getCenterY();
		float rotation = -basicSceneElement.getRotation();
		
		virtualX = virtualX - centerX;
		virtualY = virtualY - centerY;
		int newVirtualX = (int) (virtualX * Math.cos(rotation) - virtualY * Math.sin(rotation)) + centerX;
		int newVirtualY = (int) (virtualX * Math.sin(rotation) + virtualY * Math.cos(rotation)) + centerY;
	
		int x = (int) ((newVirtualX - basicSceneElement.getPosition().getJavaX(
				basicSceneElement.getWidth() * basicSceneElement.getScale())) / basicSceneElement
				.getScale());
		int y = (int) ((newVirtualY - basicSceneElement.getPosition().getJavaY(
				basicSceneElement.getHeight() * basicSceneElement.getScale())) / basicSceneElement
				.getScale());
		
		return x > 0 && y > 0
				&& factory.contains(x, y, basicSceneElement.getAsset());
	}

}
