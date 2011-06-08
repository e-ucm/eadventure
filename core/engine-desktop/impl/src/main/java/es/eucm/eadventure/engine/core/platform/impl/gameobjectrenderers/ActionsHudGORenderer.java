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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.ActionsHUDImpl;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

@Singleton
public class ActionsHudGORenderer implements
		GameObjectRenderer<Graphics2D, ActionsHUDImpl> {

	/**
	 * The current {@link PlatformConfiguration}
	 */
	private PlatformConfiguration platformConfiguration;

	/**
	 * Class logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ActionsHudGORenderer.class);

	@Inject
	public ActionsHudGORenderer(PlatformConfiguration platformConfiguration) {
		this.platformConfiguration = platformConfiguration;
		logger.info("New intance");
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
	public void render(Graphics2D g, ActionsHUDImpl actionsHUD,
			float interpolation, int offsetX, int offsetY) {
		Color color = g.getColor();
		Composite c = g.getComposite();
		g.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER, 0.8f));

		g.setColor(Color.BLACK);
		Area a = new Area(new Rectangle(platformConfiguration.getWidth(),
				platformConfiguration.getHeight()));
		a.transform(AffineTransform.getScaleInstance(
				1.0f / platformConfiguration.getScale(),
				1.0f / platformConfiguration.getScale()));
		a.subtract(new Area(new Ellipse2D.Float(actionsHUD.getX()
				- actionsHUD.getRadius(), actionsHUD.getY()
				- actionsHUD.getRadius(), actionsHUD.getRadius() * 2,
				actionsHUD.getRadius() * 2)));

		g.fill(a);
		g.setComposite(c);
		g.setColor(color);
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
	public void render(Graphics2D graphicContext, ActionsHUDImpl object,
			EAdPosition position, float scale, int offsetX, int offsetY) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.platform.GameObjectRenderer#contains(es
	 * .eucm.eadventure.engine.core.gameobjects.GameObject, int, int)
	 */
	@Override
	public boolean contains(ActionsHUDImpl object, int virutalX, int virtualY) {
		return true;
	}

}
