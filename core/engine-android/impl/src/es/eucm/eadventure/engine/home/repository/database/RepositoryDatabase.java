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
