package es.eucm.eadventure.engine.extra;

import java.util.logging.Logger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
    
//    private Bitmap bitmap;
    
    private AndroidCanvas aCanvas;
    
    @SuppressWarnings("unused")
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

    	Bitmap bitmap = Bitmap.createBitmap(platformConfiguration.getWidth(), platformConfiguration.getHeight(), Bitmap.Config.RGB_565);
    	aCanvas = new AndroidCanvas(bitmap);
    	
    	/*
    	 * This is our main activity thread's loop, we go until
    	 * asked to quit.
    	 */            
    	while (!mDone) {               
    		
    		synchronized (this) {

    			if(needToWait()) {
                    while (needToWait()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {

                        }
                    }
                }
    			if (mDone) {
    				break;
    			}

    		}

	    		// Get ready to draw.            
	    		Canvas canvas2 = mSurfaceHolder.lockCanvas();                 
	
	    		if (canvas2 != null) {
	    			aCanvas.drawColor(Color.WHITE);
					Matrix matrix = new Matrix();		
					
					if (platformConfiguration.isFullscreen())
						matrix.preScale((float) platformConfiguration.getScaleW(),
							(float) platformConfiguration.getScale());
					else matrix.preScale((float) platformConfiguration.getScale(),
							(float) platformConfiguration.getScale()); 
					
					aCanvas.setMatrix(matrix);
	    			
					gui.setCanvas(aCanvas);
	    			gui.commit(0.0f);
	    			
	    			synchronized (EAdventureRenderingThread.canvasLock) {
	    				canvas2.drawBitmap(aCanvas.getBitmap(), 0, 0, new Paint());
	    			}
//	    			canvas2.drawBitmap(bitmap, 0, 0, new Paint());
	    			mSurfaceHolder.unlockCanvasAndPost(canvas2);            
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
