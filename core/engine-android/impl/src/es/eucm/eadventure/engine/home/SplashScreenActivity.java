package es.eucm.eadventure.engine.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import es.eucm.eadventure.engine.R;
import es.eucm.eadventure.engine.home.repository.handler.RepoResourceHandler;
import es.eucm.eadventure.engine.home.utils.EngineResInstaller;
import es.eucm.eadventure.engine.home.utils.directory.Paths;

/**
 * Opening activity for the application. It shows the eAdventure Mobile logo for two seconds.
 * 
 * @author Roberto Tornero
 */
public class SplashScreenActivity extends Activity{

	/**
	 * A runnable to execute after two seconds of showing the main logo.
	 */
	private Runnable endSplash;
	/**
	 * This value allows us not to initialize {@link endSplash} if it is installing.
	 */
	private boolean installing = false;
	/**
	 * The data included with the Intent that started this application.
	 */
	private Uri data;

	/**
	 * Class that defines the information messages for the {@link ActivityHandler}
	 * 
	 * @author Roberto Tornero
	 */
	public class ActivityHandlerInstalling {

		public static final int FINISHINSTALLING = 0;

	}

	/**
	 * A dialog to show the installation progress to the user.
	 */
	private ProgressDialog dialog;
	/**
	 * A Handler for controlling when the application has ended installing
	 */
	public Handler ActivityHandler = new Handler() {
		@Override
		/**    
		 * Called when a message is sent to Engines Handler Queue 
		 */
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case ActivityHandlerInstalling.FINISHINSTALLING:
				dialog.setIndeterminate(false);
				dialog.dismiss();
				break;
			}

		}

	};

	/**
	 * Shows the eAdventure logo screen and checks if an installation is needed 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//DEBUG
		Log.e("Inicio aplicacion",String.valueOf(Debug.getNativeHeapAllocatedSize()));

		setContentView(R.layout.splash_screen);

		if (!RepoResourceHandler.checkEadDirectory(Paths.eaddirectory.ROOT_PATH)) {
			installing = true;
			EngineResInstaller is = new EngineResInstaller(this, ActivityHandler);
			is.start();

			dialog = new ProgressDialog(this);
			dialog.setTitle("Welcome to eAdventure");
			dialog.setIcon(R.drawable.dialog_icon);
			dialog.setMessage("Please wait...\nSetting up engine resources");
			dialog.setIndeterminate(true);
			dialog.show();

		}

		if (this.getIntent().getData() != null){
			data = this.getIntent().getData();
		}

		if (!installing){
			endSplash = new Runnable() {
				public void run() {
					startEngineHomeActivity();
				}
			};

			ActivityHandler.postDelayed(endSplash, 2500);
		}
	}		

	/**
	 * Register the touching events on the screen to end the showing of the logo screen  
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		startEngineHomeActivity();
		ActivityHandler.removeCallbacks(endSplash);
		return true;
	}

	/**
	 * Method that finishes {@link SplashScreenActivity} and starts {@link HomeActivity}
	 */
	public void startEngineHomeActivity() {

		Intent i = new Intent(this, HomeActivity.class);
		if (data != null)
			i.setData(data);			
		startActivity(i);
		overridePendingTransition(R.anim.fade, R.anim.hold);
		this.finish();

	}

}
