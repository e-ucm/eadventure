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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import es.eucm.eadventure.engine.AndroidGUI;
import es.eucm.eadventure.engine.AndroidPlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class EAdventureRenderingThread extends Thread {

	public static final Object canvasLock = new Object();

	private boolean mDone;
	private boolean mPaused;
	private boolean mHasFocus;
	private boolean mHasSurface;
	private boolean mContextLost;

	private SurfaceHolder mSurfaceHolder;

	private AndroidGUI gui;

	private AndroidPlatformConfiguration platformConfiguration;

	private static AndroidCanvas aCanvas;

	private static final Logger logger = Logger.getLogger("EAdventureRenderingThread");

	public EAdventureRenderingThread(SurfaceHolder holder, GUI aGUI, PlatformConfiguration platformConfiguration) {
		super();
		mDone = false;
		mSurfaceHolder = holder;
		this.platformConfiguration = (AndroidPlatformConfiguration) platformConfiguration;
		gui = (AndroidGUI) aGUI;        	
	}

	@Override
	public void run() {

		final Bitmap bitmap = Bitmap.createBitmap(platformConfiguration.getWidth(), platformConfiguration.getHeight(), Bitmap.Config.RGB_565);
		aCanvas = new AndroidCanvas(bitmap);
		gui.setCanvas(aCanvas);

		/*
		 * This is our main activity thread's loop, we go until
		 * asked to quit.
		 */            
		while (!mDone) {               

			synchronized (this) {

				while (needToWait()) {
					try {
						wait();
					} catch (InterruptedException e) {
						logger.info("Thread interrupted");
					}
				}
			}
			if (mDone) 
				break;

		}

		// Get ready to draw.            
		Canvas c = null;

		if (mHasSurface && !AndroidGameLoopImpl.canvasLockPost){

			try {
				c = mSurfaceHolder.lockCanvas();
				/*
	    		aCanvas.drawColor(Color.WHITE);
				Matrix matrix = new Matrix();		

				if (platformConfiguration.isFullscreen())
					matrix.preScale((float) platformConfiguration.getScaleW(),
						(float) platformConfiguration.getScale());
				else matrix.preScale((float) platformConfiguration.getScale(),
						(float) platformConfiguration.getScale()); 

				aCanvas.setMatrix(matrix);

	    		gui.commit(0.0f);*/
				if (c != null){
					synchronized (EAdventureRenderingThread.canvasLock){
						synchronized (mSurfaceHolder){
							c.drawBitmap(aCanvas.getBitmap(), 0, 0, null);
						}
					}
				}

			}
			finally {
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}


	private boolean needToWait() {
		return (mPaused || (! mHasFocus) || (! mHasSurface) || mContextLost)
		&& (! mDone);
	}

	public void surfaceCreated() {
		synchronized(this) {
			mHasSurface = true;
			mContextLost = false;
			notify();
		}
	}

	public void surfaceDestroyed() {
		synchronized(this) {
			mHasSurface = false;
			notify();
		}
	}

	public void onPause() {
		synchronized (this) {
			mPaused = true;
		}
	}

	public void onResume() {
		synchronized (this) {
			mPaused = false;
			notify();
		}
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		synchronized (this) {
			mHasFocus = hasFocus;
			if (mHasFocus == true) {
				notify();
			}
		}
	}
	public void onWindowResize(int w, int h) {
		synchronized (this) {
		}
	}

	public void requestExitAndWait() {
		synchronized(this) {
			mDone = true;
			notify();
		}
		try {
			join();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}


}
