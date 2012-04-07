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

package ead.engine.core.game;

/**
 * <p>
 * The game profiler allows tracking and displaying of information regarding the
 * performance of the game
 * </p>
 */
public interface GameProfiler {
	
	public static final boolean LOG_FPS = false;

	/**
	 * 1ms in ns.
	 */
	public static final long MS_TO_NS = 1000000l;

	/**
	 * Default statistics gathering interval in ns.
	 */
	public static final long DEFAULT_STATS_INTERVAL = 1000l * MS_TO_NS;

	/**
	 * Sets the interval between stats gathering.
	 * 
	 * @param statsGatheringInterval
	 */
	public void setStatGartheringInterval(long statsGatheringInterval);

	/**
	 * Resets the current statistics
	 */
	public void resetStatistics();

	/**
	 * Sets the time of the last statistic gathered
	 * 
	 * @param gameStartTime
	 */
	public void setPrevStatsTime(long gameStartTime);

	/**
	 * Returns the current averange FPS (Frames per seconds) of the games.
	 * 
	 * @return
	 */
	public double getAverageFPS();

	/**
	 * Returns the time acutally spent in the game by the player.
	 * 
	 * @return
	 */
	public int getTimeSpentInGame();

	/**
	 * Count a new tick in the game loop.
	 */
	public void countTick();

	/**
	 * Count a new frame in the game loop.
	 */
	public void countFrame();

}
