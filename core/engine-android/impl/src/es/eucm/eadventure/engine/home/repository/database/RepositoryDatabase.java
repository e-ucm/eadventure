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

package es.eucm.eadventure.engine.home.repository.database;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementation of the games repository database, as a list of information
 * 
 * @author Roberto Tornero
 */
public class RepositoryDatabase implements Iterable<GameInfo> {

	/**
	 * The list of games and their information
	 */
	private ArrayList<GameInfo> repoGames = new ArrayList<GameInfo>();

	/**
	 * Returns the list of games
	 */
	public ArrayList<GameInfo> getRepoData() {
		return repoGames;		
	}

	/**
	 * Adds a new game to the database
	 */
	public void addGameInfo(GameInfo ginfo){
		repoGames.add(ginfo);
	}

	/**
	 * Removes a game from the database
	 */
	public void removeGameInfo(GameInfo ginfo){
		repoGames.remove(ginfo);
	}

	/**
	 * Iterates through the database
	 */
	public Iterator<GameInfo> iterator() {
		return repoGames.iterator();
	}

	/**
	 * Clears the information on the database
	 */
	public void clear() {
		repoGames.clear();	
	}

}
