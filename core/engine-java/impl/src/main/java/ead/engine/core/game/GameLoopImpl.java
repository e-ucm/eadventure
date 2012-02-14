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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.game.Game;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.GameProfiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Based on:
 * <ul>
 * <li>Andrew Davison great book, <a
 * href="http://fivedots.coe.psu.ac.th/~ad/jg/">Killer Game Programming in
 * Java</a> source code.</li>
 * <li>Koen Witters' great article about <a
 * href="http://dev.koonsolo.com/7/dewitters-gameloop/">game loop
 * programming</a></li>
 * <li>Shaw Hargreaves's article about the <a href=
 * "http://blogs.msdn.com/b/shawnhar/archive/2007/07/25/understanding-gametime.aspx"
 * >XNA framework time handling</a>.</li>
 * </ul>
 */
@Singleton
public class GameLoopImpl implements GameLoop {

	private static final Logger logger = LoggerFactory.getLogger("GameLoopImpl");

	static final int SKIP_NANOS_TICK = 1000000000 / TICKS_PER_SECOND;

	static final int MAX_FRAMES_PER_SECOND = 30;

	static final int SKIP_MILLIS_FRAME = 1000 / MAX_FRAMES_PER_SECOND;

	static final int SKIP_NANOS_FRAME = 1000000000 / MAX_FRAMES_PER_SECOND;

	static final int MAX_FRAMESKIP = 5;

	private long nextTickTime = System.nanoTime();

	private long nextFrameTime = System.nanoTime();

	private Game game;

	private GameProfiler gameProfiler;

	private boolean game_is_running;

	@Inject
	public GameLoopImpl(GameProfiler gameProfiler) {
		this.gameProfiler = gameProfiler;
		game_is_running = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.engine.core.GameLoop#runLoop(boolean)
	 */
	@Override
	public void runLoop(boolean threaded) {
		if (!threaded)
			runLoop();
		else {
			Runnable runnable = new Runnable() {
				public void run() {
					runLoop();
				}
			};
			Thread t = new Thread(runnable);
			t.start();
		}

	}

	private void runLoop() {

		int loops;

		float interpolation;



		gameProfiler.setPrevStatsTime(System.nanoTime());

		long tempTime;

		logger.info("Game loop started...");

		while (game_is_running) {

			loops = 0;
			while (System.nanoTime() > nextTickTime && loops < MAX_FRAMESKIP) {
				game.update();
				gameProfiler.countTick();

				nextTickTime += SKIP_NANOS_TICK;
				loops++;
			}

			tempTime = System.nanoTime();
			if (tempTime > nextFrameTime) {
				nextFrameTime = tempTime + SKIP_NANOS_FRAME;
				interpolation = (float) (tempTime + SKIP_NANOS_TICK - nextTickTime)
						/ (float) (SKIP_NANOS_TICK);
				game.render(interpolation);
				gameProfiler.countFrame();
			} else {
				try {
					Thread.sleep((nextFrameTime - tempTime) / 1000000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.eucm.eadventure.engine.core.GameLoop#setGame(es.eucm.eadventure.engine
	 * .core.Game)
	 */
	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.engine.core.GameLoop#pause()
	 */
	@Override
	public void pause() {
		// TODO implement the pause mechanism
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.eucm.eadventure.engine.core.GameLoop#resume()
	 */
	@Override
	public void resume() {
		// TODO implement the pause mechanism
	}

	@Override
	public void stop() {
		this.game_is_running = false;
		
	}

	@Override
	public boolean isRunning() {
		return game_is_running;
	}
}
