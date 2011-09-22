package es.eucm.eadventure.engine.home.local;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import es.eucm.eadventure.engine.home.WorkspaceActivity.LocalGamesListFragment.LGAHandlerMessages;
import es.eucm.eadventure.engine.home.utils.filters.EADFileFilter;
import es.eucm.eadventure.engine.home.utils.directory.Paths;

/**
 * A thread to search for existing ead games in the installation folder
 * 
 * @author Roberto Tornero
 */
public class SearchGamesThread extends Thread {

	/**
	 * The handler queue to send messages to
	 */
	private Handler handler;

	/**
	 * Constructor
	 */
	public SearchGamesThread(Handler ha) {
		handler = ha;
	}

	/**
	 * Starts the thread and searches for ead games in the external storage (SDCard).
	 * When the task is finished, it sends a message through {@link handler}
	 */
	@Override
	public void run() {

		Message msg = handler.obtainMessage();

		Log.d("SearchGamesThread", "SDCard state : "
				+ Environment.getExternalStorageState().toString());

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			String adventures[]=null;
			File games = new File(Paths.eaddirectory.GAMES_PATH);

			if(games.exists())
				adventures = games.list(new EADFileFilter());

			if (adventures != null && adventures.length > 0) {
				Log.d("SearchGamesThread", "EAD files in sdCard : "
						+ adventures[0]);

				Bundle b = new Bundle();
				b.putStringArray("adventuresList", adventures);
				msg.what = LGAHandlerMessages.GAMES_FOUND;
				msg.setData(b);

			}

			else  msg.what = LGAHandlerMessages.NO_GAMES_FOUND;		

		}

		else  msg.what = LGAHandlerMessages.NO_SD_CARD;		

		msg.sendToTarget();

	}

}
