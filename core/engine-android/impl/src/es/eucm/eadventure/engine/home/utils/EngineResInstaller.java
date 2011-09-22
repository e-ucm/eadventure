package es.eucm.eadventure.engine.home.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import es.eucm.eadventure.engine.home.repository.handler.RepoResourceHandler;
import es.eucm.eadventure.engine.home.utils.directory.Paths;

/**
 * Installs the main resources of EAdventure Mobile
 * 
 * @author Roberto Tornero
 */
public class EngineResInstaller extends Thread {

	/**
	 * The context from which an instance of this class is called
	 */
	private Context con;
	/**
	 * The handler to control the installation progress
	 */
	private Handler han;
	/**
	 * A dialog to show the progress of the installation of the resources
	 */
	private ProgressDialog dialog;
	/**
	 * The handler to control the appearance and visibility of the dialog
	 */
	public Handler ActivityHandler = new Handler() {
		@Override
		/**    * Called when a message is sent to Engines Handler Queue **/
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case ActivityHandlerInstalling.FINISHISTALLING:
				dialog.setIndeterminate(false);
				dialog.dismiss();
				break;
			}

		}

	};

	/**
	 * Create new instance with parameters
	 */
	public EngineResInstaller(Context con, Handler handler) {
		super();

		this.con = con;
		this.han = handler;

	}	

	/**
	 * Use {@link init} to extract the resources and update the dialog 
	 */
	@Override
	public void run() {
		this.init();

		Message msg = han.obtainMessage();
		Bundle b = new Bundle();
		msg.what = ActivityHandlerInstalling.FINISHISTALLING;
		msg.setData(b);
		msg.sendToTarget();

	}

	/**
	 * Extract the resources to the eAdventure folder
	 */
	private void init() {
		if (!new File(Paths.eaddirectory.ROOT_PATH).exists()) {

			try {
				InputStream is = con.getAssets().open("EadAndroid.zip");
				BufferedOutputStream file;
				file = new BufferedOutputStream(new FileOutputStream(
				"/sdcard/EadAndroid.zip"));
				RepoResourceHandler.copyInputStream(is, file);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			RepoResourceHandler.unzip(Paths.device.EXTERNAL_STORAGE,
					Paths.device.EXTERNAL_STORAGE, "EadAndroid.zip", true);
		}
	}

	/**
	 * Handler messages
	 * 
	 * @author Roberto Tornero
	 */
	public class ActivityHandlerInstalling {

		public static final int FINISHISTALLING = 0;

	}

}
