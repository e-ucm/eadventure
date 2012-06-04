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

package ead.engine.android.home.local;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import ead.engine.android.home.WorkspaceActivity.LocalGamesListFragment.LGAHandlerMessages;
import ead.engine.android.home.utils.directory.Paths;
import ead.engine.android.home.utils.filters.EADFileFilter;

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
