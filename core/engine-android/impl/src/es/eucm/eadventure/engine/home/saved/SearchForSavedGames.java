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

package es.eucm.eadventure.engine.home.saved;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import es.eucm.eadventure.engine.home.WorkspaceActivity.LoadGamesListFragment.SavedGamesHandlerMessages;
import es.eucm.eadventure.engine.home.repository.handler.RepoResourceHandler;
import es.eucm.eadventure.engine.home.utils.ActivityPipe;

/**
 * Thread to retrieve the information of the saved games from their folder
 * 
 * @author Roberto Tornero
 */
public class SearchForSavedGames extends Thread {

	/**
	 * The handler to control the existence of saved games
	 */
	private Handler handler;

	/**
	 * Constructor
	 */
	public SearchForSavedGames(Handler han) {
		super();
		this.handler = han;

	}

	/**
	 * Retrieve the information of the saved games from their folder, if not empty
	 */
	@Override
	public void run() {

		LoadGamesArray info = null;

		RepoResourceHandler.updatesavedgames();

		info = RepoResourceHandler.getexpandablelist();

		if (info.getSavedGames().size() == 0) {

			Message msg = handler.obtainMessage();
			Bundle b = new Bundle();

			msg.what = SavedGamesHandlerMessages.NOGAMES;
			msg.setData(b);
			msg.sendToTarget();

		} else {

			String key = ActivityPipe.add(info); 

			Message msg = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("loadingsavedgames", key);
			msg.what = SavedGamesHandlerMessages.GAMES;
			msg.setData(b);
			msg.sendToTarget();
		}

	}

}
