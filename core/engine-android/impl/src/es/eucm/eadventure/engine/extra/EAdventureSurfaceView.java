package es.eucm.eadventure.engine.extra;

import java.util.logging.Logger;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.params.guievents.EAdMouseEvent.MouseActionType;
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
    	
    	public OnTouchListener(MouseState mouseState) {
    		this.mouseState = mouseState;
    	}
    	
  		@Override
		public boolean onTouch(View v, MotionEvent event) {
  			mouseState.setMousePosition((int) event.getRawX(), (int) event.getRawY());
  			if (event.getAction() == MotionEvent.ACTION_UP) {
  				logger.info("Right click " + mouseState.getGameObjectUnderMouse());
	  			MouseAction action = new MouseActionImpl(MouseActionType.RIGHT_CLICK, mouseState.getVirtualMouseX(), mouseState.getVirtualMouseY());
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
		logger.info("Tread created");
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
