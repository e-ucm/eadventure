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

package es.eucm.ead.model;

/**
 * Interface containing admissible orders by the command interpreter. They're here to be shared with importer and
 * with the engine
 */
public interface Commands {

	/**
	 * Goes to chapter
	 */
	public static final String GO_CHAPTER = "goc";

	/**
	 * Goes to a scene
	 */
	public static final String GO_SCENE = "go";

	/**
	 * Returns the id of the current chapter
	 */
	public static final String CHAPTER = "chapter";

	/**
	 * Returns the id of the current scene
	 */
	public static final String SCENE = "scene";

	/**
	 * Returns a field
	 */
	public static final String GET = "get";

	/**
	 * Go to a scene *
	 */
	public static final String GO = "go";

	/**
	 * List something (elements, variables...)
	 */
	public static final String LIST = "list";

	/**
	 * Load a file with valid commands *
	 */
	public static final String LOAD = "load";

	/**
	 * Sets a variable ( set id.var value )
	 */
	public static final String SET = "set";

	/**
	 * Ping an element in the scene (it's highlighted)
	 */
	public static final String PING = "ping";

	/**
	 * Toggle sound *
	 */
	public static final String SOUND = "sound";

	/**
	 * Returns how many watchers are watching an element
	 */
	public static final String WATCHING = "watching";

	/**
	 * Returns the id of the element below the cursor
	 */
	public static final String WHOIS = "whois";

	/**
	 * Moves the mouse (move x y), x and y are in window coordinates
	 */
	public static final String MOVE = "move";

	/**
	 * Returns the mouse position in window's coordinates
	 */
	public static final String WHERE = "where";

	/**
	 * Produces a click (click x y), x and y are in window coordinates
	 */
	public static final String CLICK = "click";

	/**
	 * Exits the game
	 */
	public static final String EXIT = "exit";

	/**
	 * It does nothing. It can be used to pass one update before executing other command
	 */
	public static final String PASS = "pass";

	/**
	 * Logs a message
	 */
	public static final String LOG = "log";

	/**
	 * Waits for a list of effects to be launched
	 */
	public static final String WAIT_EFFECTS = "waiteffects";

	/**
	 * Returns a list with the effects that have not been launched
	 */
	public static final String EFFECTS = "effects";

	/**
	 * Clears the waiting effects list
	 */
	public static final String CLEAR_EFFECTS = "cleareffects";

	/**
	 * Waits and ignores commands until receives a notify
	 */
	public static final String WAIT = "wait";

	/**
	 * Unlocks any possible waiting
	 */
	public static final String NOTIFY = "notify";
}
