package es.eucm.eadventure.engine.core;

import static playn.core.PlayN.*;

import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.impl.AbstractAssetHandler;
import es.eucm.eadventure.engine.core.platform.impl.PlayNAssetHandler;
import es.eucm.eadventure.engine.core.platform.impl.PlayNGUI;

import playn.core.Canvas;
import playn.core.CanvasLayer;
import playn.core.Graphics;
import playn.core.Image;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.PlayN;
import playn.core.Pointer;

public class EAdEngine implements playn.core.Game, Keyboard.Listener {

	private Canvas gameLayer;

	private float touchVectorX, touchVectorY;
	
	private Game game;
	
	private GUI gui;
	
	private AssetHandler assetHandler;
	
	private static final Logger logger = Logger.getLogger("EAdEngine");

	@Inject
	public EAdEngine(Game game, GUI gui, AssetHandler assetHandler) {
		this.game = game;
		this.gui = gui;
		this.assetHandler = assetHandler;
		((PlayNAssetHandler) assetHandler).setEngine(this);
	}
	
	@Override
	public void init() {
		graphics().setSize(600, 480);
		PlayN.log().debug("EAdEngine: init");
				
		/*
		gameLayer = graphics().createSurfaceLayer(graphics().width(),
				graphics().height());
		graphics().rootLayer().add(gameLayer);
		 */
		
		CanvasLayer layer = graphics().createCanvasLayer(graphics().width(), graphics().height());
		graphics().rootLayer().add(layer);

		gameLayer = layer.canvas();
		gameLayer.setStrokeWidth(2);
		gameLayer.setStrokeColor(0xffff0000);
		gameLayer.strokeRect(1, 1, 46, 46);

		((PlayNGUI) gui).initializeCanvas(gameLayer);

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
			PlayN.log().debug("EAdEngine: update " + (updateCont - completeUpdate));
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
	
	public Image getImage(String absolutePath) {
		return assetManager().getImage(absolutePath);
	}

	public Graphics getGraphics() {
		return graphics();
	}

}
