package es.eucm.eadventure.engine.core;

import static playn.core.PlayN.*;

import com.google.inject.Inject;

import playn.core.Canvas;
import playn.core.CanvasLayer;
import playn.core.Graphics;
import playn.core.Image;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Pointer;

public class EAdEngine implements playn.core.Game, Keyboard.Listener {

	private Canvas gameLayer;

	private float touchVectorX, touchVectorY;
	
	private Game game;

	@Inject
	public EAdEngine(Game game) {
		this.game = game;
	}
	
	@Override
	public void init() {
		graphics().setSize(800, 600);
				
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

	@Override
	public void update(float delta) {
		game.update();

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
		return 15;
	}

	public Canvas getCanvas() {
		return gameLayer;
	}
	
	public Image getImage(String absolutePath) {
		return assetManager().getImage(absolutePath);
	}

	public Graphics getGraphics() {
		return graphics();
	}

}
