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

package ead.engine.android.home.utils;

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
import ead.engine.android.home.repository.handler.RepoResourceHandler;
import ead.engine.android.home.utils.directory.Paths;

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
				e.printStackTrace();
			} catch (IOException e) {
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
