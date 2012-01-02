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
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import java.util.logging.Logger;

import playn.core.Canvas;
import playn.core.CanvasLayer;
import playn.core.Graphics;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.html.HtmlPlatform;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.inject.Inject;

import ead.common.model.elements.guievents.EAdMouseEvent;
import ead.common.model.elements.guievents.enums.MouseButtonType;
import ead.common.model.elements.guievents.enums.MouseEventType;
import ead.engine.core.game.Game;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.actions.MouseActionImpl;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.EngineConfiguration;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.PlayNAssetHandler;
import ead.engine.core.platform.PlayNGUI;

public class EAdEngine implements playn.core.Game, Keyboard.Listener {

	private Canvas gameLayer;

	@SuppressWarnings("unused")
	private float touchVectorX, touchVectorY;

	private Game game;

	private GUI gui;

	private InputHandler mouseState;

	private EngineConfiguration platformConfiguration;

	private static final Logger logger = Logger.getLogger("EAdEngine");

	@Inject
	public EAdEngine(Game game, GUI gui, AssetHandler assetHandler,
			InputHandler mouseState, EngineConfiguration platformConfiguration) {
		this.game = game;
		this.gui = gui;
		this.mouseState = mouseState;
		this.platformConfiguration = platformConfiguration;
		((PlayNAssetHandler) assetHandler).setEngine(this);
		logger.info("New instance");
	}

	@Override
	public void init() {
		graphics().setSize(platformConfiguration.getWidth(), platformConfiguration.getHeight());
		PlayN.log().debug("EAdEngine: init");
		
		HtmlPlatform.disableRightClickContextMenu();

		/*
		 * gameLayer = graphics().createSurfaceLayer(graphics().width(),
		 * graphics().height()); graphics().rootLayer().add(gameLayer);
		 */

		CanvasLayer layer = graphics().createCanvasLayer(graphics().width(),
				graphics().height());
		graphics().rootLayer().add(layer);

		gameLayer = layer.canvas();
		gameLayer.setStrokeWidth(2);
		gameLayer.setStrokeColor(0xffff0000);
		gameLayer.strokeRect(1, 1, 46, 46);

		((PlayNGUI) gui).initializeCanvas(gameLayer, layer);

		keyboard().setListener(this);
		pointer().setListener(new Pointer.Listener() {
			@Override
			public void onPointerEnd(Pointer.Event event) {
				touchVectorX = touchVectorY = 0;
			}

			@Override
			public void onPointerDrag(Pointer.Event event) {
				touchMove(event.x(), event.y());
			}

			@Override
			public void onPointerStart(Pointer.Event event) {
				touchMove(event.x(), event.y());
			}
		});

		com.google.gwt.user.client.Event
				.addNativePreviewHandler(new NativePreviewHandler() {
					public void onPreviewNativeEvent(
							final NativePreviewEvent event) {
						int eventType = event.getTypeInt();
						int eventX = event.getNativeEvent().getClientX();
						int eventY = event.getNativeEvent().getClientY();
						MouseButtonType b = getMouseButton(event.getNativeEvent()
								.getButton());
						// TODO double click
						EAdMouseEvent e = null;
						switch (eventType) {
						case com.google.gwt.user.client.Event.ONCLICK:
							// Do nothing. Clicks are processed in ONMOUSEUP;
							break;
						case com.google.gwt.user.client.Event.ONMOUSEDOWN:
							e = EAdMouseEvent.getMouseEvent(
									MouseEventType.PRESSED, b);
							break;
						case com.google.gwt.user.client.Event.ONMOUSEUP:
							e = EAdMouseEvent.getMouseEvent(
									MouseEventType.RELEASED, b);
							mouseState.addAction(new MouseActionImpl(EAdMouseEvent.getMouseEvent(
									MouseEventType.CLICK, b), eventX, eventY ));
							break;
						default:
							// not interested in other events
						}
						if (e != null)
							mouseState.addAction(
									new MouseActionImpl(e, eventX, eventY));
					}
				});
	}

	private MouseButtonType getMouseButton(int b) {
		switch (b) {
		case NativeEvent.BUTTON_LEFT:
			return MouseButtonType.BUTTON_1;
		case NativeEvent.BUTTON_MIDDLE:
			return MouseButtonType.BUTTON_2;
		case NativeEvent.BUTTON_RIGHT:
			return MouseButtonType.BUTTON_3;
		default:
			return MouseButtonType.NO_BUTTON;
		}
	}

	@Override
	public void onKeyDown(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyUp(Event event) {
		// TODO Auto-generated method stub

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

	private void touchMove(float x, float y) {
		float cx = graphics().screenWidth() / 2;
		float cy = graphics().screenHeight() / 2;

		touchVectorX = (x - cx) * 1.0f / cx;
		touchVectorY = (y - cy) * 1.0f / cy;
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

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub

	}

}
