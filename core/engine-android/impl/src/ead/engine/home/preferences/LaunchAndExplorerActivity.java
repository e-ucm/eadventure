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

package ead.engine.home.preferences;

import java.io.File;
import java.net.URI;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;
import ead.engine.R;
import ead.engine.home.WorkspaceActivity;
import ead.engine.home.repository.handler.RepoResourceHandler;
import ead.engine.home.utils.directory.Paths;

/**
 * Launches the AndExplorer application to navigate through the external storage
 * 
 * @author Roberto Tornero
 */
public class LaunchAndExplorerActivity extends PreferenceActivity {
	
	/**
	 * The tag of this Activity
	 */
	private static String TAG = "AndExplorerActivity";
	/**
	 * Returning value from the intent that launches AndExplorer 
	 */
	private static int PICK_REQUEST_CODE = 0;
	/**
	 * The path where the game selected will be installed
	 */
	private String path_from = null;

	/**
	 * Starts AndExplorer application if it is installed on the device 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final boolean andExplorerAvailable = isIntentAvailable(this,
				Intent.ACTION_PICK);

		if (andExplorerAvailable) {

			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			Uri startDir = Uri.fromFile(new File("/sdcard"));
			// Files and directories
			intent.setDataAndType(startDir,
			"vnd.android.cursor.dir/lysesoft.andexplorer.file");
			// Optional filtering on file extension.
			intent.putExtra("browser_filter_extension_whitelist", "*.ead");
			// Title
			intent.putExtra("explorer_title", "Select a file");
			// Optional colors
			intent.putExtra("browser_title_background_color", "440000AA");
			intent.putExtra("browser_title_foreground_color", "FFFFFFFF");
			intent.putExtra("browser_list_background_color", "66000000");
			// Optional font scale
			intent.putExtra("browser_list_fontscale", "140%");
			// Optional 0=simple list, 1 = list with filename and size, 2 = list
			// with filename, size and date.
			intent.putExtra("browser_list_layout", "1");
			startActivityForResult(intent, PICK_REQUEST_CODE);
		}

		else this.showAndNavigatorAppNotInstalled();

	}

	/**
	 * Checks if AndExplorer is installed on the device
	 */
	public static boolean isIntentAvailable(Context context, String action ) {

		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		Uri startDir = Uri.fromFile(new File("/sdcard"));  
		// Files and directories  
		intent.setDataAndType(startDir, "vnd.android.cursor.dir/lysesoft.andexplorer.file");  
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size()   > 0;
	}

	/**
	 * Builds an AlertDialog to show that AndExplorer is not installed
	 */
	public void showAndNavigatorAppNotInstalled() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
		.setMessage(
				"You need to install AndExplorer in order to install ead games from SDCard")
				.setCancelable(false).setPositiveButton("Install",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent i = new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("market://search?q=pname:lysesoft.andexplorer"));
						startActivity(i);
						finish();
					}
				}).setNegativeButton("Quit",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * If the result from launching AndExplorer is picking up a valid ead game, install it.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {


		Uri uri = null;
		String path = null;

		if (requestCode == PICK_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				uri = intent.getData();
				String type = intent.getType();
				Log.i(TAG, "Pick completed: " + uri + " " + type);
				if (uri != null) {
					path = uri.toString();
					if (path.toLowerCase().startsWith("file://") && path.toLowerCase().endsWith(".ead")) {
						path = (new File(URI.create(path))).getAbsolutePath();
						Log.i(TAG, "Pick completed: " + path);
						if (path==null)
							Toast.makeText(this, "You have not selected a game to install", Toast.LENGTH_LONG);
					}
					else Toast.makeText(this, "You have not selected a valid eadventure game file",Toast.LENGTH_LONG);

				}
			} else
				Log.i(TAG, "Back from pick with cancel status");
		}

		if (path!=null) {
			installEadGame(path);
		}
		else this.finish();

	}

	/**
	 * Returns the path where the game will be installed
	 */
	private String getPathFrom() {
		return path_from;
	}

	/**
	 * Installs the game 
	 */
	private void installEadGame(String path_from) {

		this.path_from = path_from;

		this.showDialog(DIALOG_INSTALL_ID);		

		Thread t = new Thread(new Runnable() {
			public void run()
			{	

				String path_from = getPathFrom();
				int last = path_from.lastIndexOf("/");
				String gameFileName = path_from.substring(last + 1);
				path_from= path_from.substring(0, last+1);
				Log.d(TAG,"PathFrom: " + path_from);
				Log.d(TAG,"FileName: " + gameFileName);
				RepoResourceHandler.unzip(path_from,Paths.eaddirectory.GAMES_PATH,gameFileName,false);
				dismissDialog(DIALOG_INSTALL_ID);

				startGamesTabActivity();
				finish();
			}
		});
		t.start();

	}

	/**
	 * Starts the workspace to return to the installed games view once the current game is installed
	 */
	private void startGamesTabActivity() {
		Intent i = new Intent(this, WorkspaceActivity.class);
		i.putExtra("Tag", 0);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

	/**
	 * Id of the installation progress Dialog
	 */
	private static final int DIALOG_INSTALL_ID = 0;

	/**
	 * A dialog to display the progress of installing games
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_INSTALL_ID:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setCancelable(false);		
			progressDialog.setTitle("Please wait");
			progressDialog.setIcon(R.drawable.dialog_icon);
			progressDialog.setMessage("Installing game...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
			dialog = progressDialog;			
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

}
