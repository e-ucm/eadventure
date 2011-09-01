package es.eucm.eadventure.engine.core;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Pointer;
import playn.core.SurfaceLayer;

public class EAdEngine implements Game, Keyboard.Listener {

	private SurfaceLayer gameLayer;

	private float touchVectorX, touchVectorY;

	@Override
	public void init() {
		graphics().setSize(800, 600);

		gameLayer = graphics().createSurfaceLayer(graphics().width(),
				graphics().height());
		graphics().rootLayer().add(gameLayer);

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
		// TODO Auto-generated method stub

	}

	private void touchMove(float x, float y) {
		float cx = graphics().screenWidth() / 2;
		float cy = graphics().screenHeight() / 2;

		touchVectorX = (x - cx) * 1.0f / cx;
		touchVectorY = (y - cy) * 1.0f / cy;
	}

	@Override
	public void paint(float alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public int updateRate() {
		return 15;
	}

}
