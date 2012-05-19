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

package ead.engine.android.home.repository.connection;

import android.content.Context;
import android.os.Handler;
import ead.engine.android.home.repository.database.GameInfo;
import ead.engine.android.home.repository.database.RepositoryDatabase;

/**
 * Useful services for updating the repository database and downloading ead games
 * 
 * @author Roberto Tornero
 */
public class RepositoryServices {

	/**
	 * The thread that updates the database
	 */
	private UpdateDatabaseThread data_updater;
	/**
	 * The thread that downloads games
	 */
	private DownloadGameThread game_downloader;

	/**
	 * Starts {@link data_updater}
	 */
	public void updateDatabase(Context c, Handler handler, RepositoryDatabase rd) {

		data_updater = new UpdateDatabaseThread(handler, rd);
		data_updater.start();
	}

	/**
	 * Starts {@link game_downloader}
	 */
	public void downloadGame(Context c , Handler handler , GameInfo game) {

		game_downloader = new DownloadGameThread(c,handler,game);
		game_downloader.start();		
	}

}
