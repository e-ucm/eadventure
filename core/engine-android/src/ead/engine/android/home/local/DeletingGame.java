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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import ead.engine.android.home.WorkspaceActivity.LocalGamesListFragment.LGAHandlerMessages;
import ead.engine.android.home.repository.handler.RepoResourceHandler;

/**
 * A thread to delete existing games
 * 
 * @author Roberto Tornero
 */
public class DeletingGame extends Thread {

	/**
	 * The handler that controls if the game is deleted
	 */
	private Handler handler;
	/**
	 * The paths to delete the game from
	 */
	private String[] paths;

	/**
	 * Constructor
	 */
	public DeletingGame(Handler han,String[] paths) {

		super();
		this.handler=han;
		this.paths=paths;
	}

	/**
	 * The thread to delete games
	 */
	@Override
	public void run() {

		RepoResourceHandler.deleteFile(paths[0]);
		RepoResourceHandler.deleteFile(paths[1]);

		Message msg = handler.obtainMessage();
		Bundle b = new Bundle();

		msg.what = LGAHandlerMessages.DELETING_GAME;
		msg.setData(b);
		msg.sendToTarget();
	}

}
