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

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.common.model.guievents.EAdMouseEvent.MouseButton;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.guiactions.impl.MouseActionImpl;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

@Singleton
public class EAdventureSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;

    private EAdventureRenderingThread mThread;
    
    private static final Logger logger = Logger.getLogger("EAdventureSurfaceView");
    
    private class OnTouchListener implements View.OnTouchListener {
		
    	private MouseState mouseState;
    	
    	private long downTime;
    	
    	public OnTouchListener(MouseState mouseState) {
    		this.mouseState = mouseState;
    	}
    	
  		@Override
		public boolean onTouch(View v, MotionEvent event) {
  			
  			mouseState.setMousePosition((int) event.getRawX(), (int) event.getRawY());
  			if (event.getAction() == MotionEvent.ACTION_DOWN) {
  				downTime = System.currentTimeMillis();
  			}
  			if (event.getAction() == MotionEvent.ACTION_UP) {
  				logger.info("Right click " + mouseState.getGameObjectUnderMouse());
	  			MouseAction action;
	  			if (System.currentTimeMillis() - downTime > 1500)
	  				action = new MouseActionImpl(MouseActionType.CLICK, MouseButton.BUTTON_2, mouseState.getMouseX(), mouseState.getMouseY());
	  			else 
	  				action = new MouseActionImpl(MouseActionType.CLICK, MouseButton.BUTTON_1, mouseState.getMouseX(), mouseState.getMouseY());
	  			mouseState.getMouseEvents().add(action);
				mouseState.setMousePosition(-1, -1);
  			}
  			return true;
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
        setOnTouchListener(new OnTouchListener(mouseState));
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
