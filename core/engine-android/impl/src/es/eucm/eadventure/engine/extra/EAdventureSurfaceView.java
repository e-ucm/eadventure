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

package es.eucm.eadventure.engine.extra;

import java.util.logging.Logger;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.google.inject.Singleton;
import es.eucm.eadventure.common.model.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.common.model.guievents.EAdMouseEvent.MouseButton;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.engine.AndroidPlatformConfiguration;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.guiactions.impl.MouseActionImpl;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

@Singleton
public class EAdventureSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder holder;

	private EAdventureRenderingThread mThread;

	private static final Logger logger = Logger.getLogger("EAdventureSurfaceView");

	private GestureDetector gestureDetector;

	private class EAdventureGestureListener extends SimpleOnGestureListener {

		private MouseState mouseState;

		public EAdventureGestureListener(MouseState mouseState) {
			this.mouseState = mouseState;
		}

		@Override
		public boolean onDown(MotionEvent e){

			mouseState.setMousePressed(true);
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e){

			mouseState.getMouseEvents().add(new MouseActionImpl(EAdMouseEventImpl.MOUSE_LEFT_PRESSED, 
					mouseState.getMouseX(), mouseState.getMouseY()));
			MouseAction action = new MouseActionImpl(MouseActionType.CLICK, 
					MouseButton.BUTTON_1, mouseState.getMouseX(), mouseState.getMouseY());
			mouseState.getMouseEvents().add(action);

			return true;
		}

		@Override
		public void onLongPress(MotionEvent e){

			MouseAction action = new MouseActionImpl(MouseActionType.CLICK, 
					MouseButton.BUTTON_2, mouseState.getMouseX(), mouseState.getMouseY());
			mouseState.getMouseEvents().add(action);
		}
	}

	private class OnTouchListener implements View.OnTouchListener {

		private MouseState mouseState;

		private AndroidPlatformConfiguration platformConfiguration;

		public OnTouchListener(MouseState mouseState, PlatformConfiguration platformConfiguration ) {
			this.mouseState = mouseState;
			this.platformConfiguration = (AndroidPlatformConfiguration) platformConfiguration;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			int x = (int) ( event.getRawX() / platformConfiguration.getScaleW());
			int y = (int) ( event.getRawY() / platformConfiguration.getScaleH());
			mouseState.setMousePosition(x, y);

			if (gestureDetector.onTouchEvent(event)) {
				return true;
			}

			if(event.getAction() == MotionEvent.ACTION_UP) {
				mouseState.setMousePressed(false);
			}

			return false;	
		}

	};

	public EAdventureSurfaceView(Context context) {
		super(context);
		init();
	}

	public EAdventureSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		holder = getHolder();
		holder.addCallback(this);
	}

	public SurfaceHolder getSurfaceHolder() {
		return holder;
	}

	public void start(GUI gui,
			PlatformConfiguration platformConfiguration,
			MouseState mouseState) {
		gestureDetector = new GestureDetector(new EAdventureGestureListener(mouseState));
		this.setOnTouchListener(new OnTouchListener(mouseState, platformConfiguration));
		mThread = new EAdventureRenderingThread(holder, gui, platformConfiguration);
		logger.info("Thread created");
		mThread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mThread.surfaceCreated();
		logger.info("Surface created");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mThread.onWindowResize(width, height);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mThread.surfaceDestroyed();
	}

	public void onPause() {
		mThread.onPause();
	}

	public void onResume() {
		mThread.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mThread.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mThread.requestExitAndWait();
	}

	public void stopDrawing() {
		mThread.requestExitAndWait();
	}

}
