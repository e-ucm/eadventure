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

package ead.engine.core;

import static playn.core.PlayN.graphics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Graphics;
import playn.core.PlayN;
import playn.html.HtmlPlatform;

import com.google.inject.Inject;

import ead.engine.core.game.Game;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.PlayNInputListener;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.PlayNAssetHandler;
import ead.engine.core.platform.PlayNGUI;

public class EAdEngine implements playn.core.Game {

	private Canvas gameLayer;

	private Game game;

	private GUI gui;

	private InputHandler inputHandler;

	private EngineConfiguration engineConfiguration;

	private static final Logger logger = LoggerFactory.getLogger("EAdEngine");

	@Inject
	public EAdEngine(Game game, GUI gui, AssetHandler assetHandler,
			InputHandler inputHandler, EngineConfiguration platformConfiguration) {
		this.game = game;
		this.gui = gui;
		this.inputHandler = inputHandler;
		this.engineConfiguration = platformConfiguration;
		((PlayNAssetHandler) assetHandler).setEngine(this);
		logger.info("New EAdEngine instance");
	}

	@Override
	public void init() {
		graphics().setSize(engineConfiguration.getWidth(),
				engineConfiguration.getHeight());
		PlayN.log().debug("EAdEngine: init");
		PlayNInputListener listener = new PlayNInputListener(inputHandler);
		PlayN.mouse().setListener(listener);
		PlayN.keyboard().setListener(listener);
		HtmlPlatform.disableRightClickContextMenu();
		HtmlPlatform.setCursor(null);

		CanvasImage canvas = graphics().createImage(graphics().width(), graphics().height());
		gameLayer = canvas.canvas();
		gameLayer.setStrokeColor(0xffff0000);
		gameLayer.drawText("Loading...", 20, 20);
		graphics().rootLayer().add(graphics().createImageLayer(canvas));
		((PlayNGUI) gui).initializeCanvas(gameLayer);

	}

	int updateCont = 0;

	int completeUpdate = 0;

	@Override
	public void update(float delta) {
		if (updateCont % 60 == 0) {
			PlayN.log().debug(
					"EAdEngine: update " + (updateCont - completeUpdate));
		}
		updateCont++;
		game.update();
		completeUpdate++;

	}

	@Override
	public void paint(float alpha) {
		game.render(alpha);
	}

	@Override
	public int updateRate() {
		return 67;
	}

	public Graphics getGraphics() {
		return graphics();
	}

}
