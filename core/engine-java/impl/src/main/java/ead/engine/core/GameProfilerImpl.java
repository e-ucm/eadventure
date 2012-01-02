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

package ead.engine.core;

import java.util.logging.Logger;

import ead.engine.core.game.GameProfiler;

/**
 * 
 * 
 * Loosely based on:
 * <ul>
 *   <li>Andrew Davison great book, <a href="http://fivedots.coe.psu.ac.th/~ad/jg/">Killer Game
 *   Programming in Java</a> source code.</li>
 *   <li>Koen Witters' great article about <a href="http://dev.koonsolo.com/7/dewitters-gameloop/">game loop programming</a></li>
 *  <li>Shaw Hargreaves's article about the <a href="http://blogs.msdn.com/b/shawnhar/archive/2007/07/25/understanding-gametime.aspx">XNA framework time handling</a>.</li>
 * </ul>
 */
public class GameProfilerImpl implements GameProfiler {

	  /**
	   * Number of samples used to calculate average FPS and UPS.
	   */
	  private static final int SAMPLE_SIZE = 150;

	  /**
	   * Game loop logger.
	   */
	  private static final Logger logger = Logger.getLogger("Game Profiler");
	  	  
	  /**
	   * Time since last statistics gathering (in ns).
	   */
	  private long statsTime;

	  /**
	   * Last time statistics where collected (in ms).
	   */
	  private long prevStatsTime;

	  /**
	   * Time spent in game in seconds.
	   */
	  private int timeSpentInGame;

	  /**
	   * Average frames (rendered) per second (FPS).
	   */
	  private double averageFPS;

	  /**
	   * Average updates per second (UPS)
	   */
	  private double averageUPS;
	  
	  private int tick;
	  
	  private int frame;

	@Override
	public void setStatGartheringInterval(long statsGatheringInterval) {

	}

	@Override
	public void resetStatistics() {
	    this.statsTime = 0L;
	    this.timeSpentInGame = 0;
	    this.averageFPS = 0.0;
	    this.averageUPS = 0.0;
	    this.tick = 0;
	    this.frame = 0;
	}

	@Override
	public void setPrevStatsTime(long prevStatsTime) {
		this.prevStatsTime = prevStatsTime;
	}

	@Override
	public double getAverageFPS() {
		return averageFPS;
	}

	@Override
	public int getTimeSpentInGame() {
		return timeSpentInGame;
	}

	@Override
	public void countTick() {
		tick++;
		if (tick > SAMPLE_SIZE) {
			statsTime = System.nanoTime();
			averageFPS = (float) frame /  (statsTime - prevStatsTime) * 1000000000;
			averageUPS = (float) tick /  (statsTime - prevStatsTime) * 1000000000;
			prevStatsTime = statsTime;
			frame = 0;
			tick = 0;
			logger.info("FPS: " + averageFPS + "; UPS: " + averageUPS);
		}
	}

	@Override
	public void countFrame() {
		frame++;
	}

}
