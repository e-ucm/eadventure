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

package es.eucm.ead.editor.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.apache.log4j.PatternLayout;

/**
 * Simple configuration for log4j logging. Should be called from "main" files or
 * unit-tests in this directory. A single check for log4j existence is performed
 * during initial class-loading, to bail out cleanly if it is not present.
 *
 * @author mfreire
 */
public class Log4jConfig {

	static private final boolean isLog4jPresent;

	static {
		boolean found = false;
		try {
			found = (null != Log4jConfig.class.getClassLoader().loadClass(
					"org.apache.log4j.Logger"));
		} catch (Exception e) {
			e.printStackTrace();
			// loading of a sample log4j class failed: no log4j for you
		}
		isLog4jPresent = found;
	}

	/**
	 * Decouples slf4j levels from log4j levels. This avoids adding any mention
	 * to log4j in the calling class, or to rely on their being basically the
	 * same.
	 */
	public enum Slf4jLevel {

		// very detailed, generally inactive
		Trace("TRACE"), Debug("DEBUG"),
		// generally active
		Info("INFO"),
		// always active
		Warn("WARN"), Error("ERROR"),
		// critical - only before crash
		Fatal("FATAL");

		private String log4jLevelName;

		Slf4jLevel(String log4jLevelName) {
			this.log4jLevelName = log4jLevelName;
		}

		public Level getLog4jLevel() {
			return Level.toLevel(log4jLevelName);
		}

		public String toString() {
			switch (this) {
			case Trace:
				return "trace";
			case Debug:
				return "debug";
			case Info:
				return "info";
			case Warn:
				return "warn";
			case Error:
				return "error";
			case Fatal:
				return "fatal";
			}
			return "trace";
		}
	}

	public static void pushNDC(String value) {
		NDC.push(value);
	}

	public static void popNDC() {
		NDC.pop();
	}
	
	public static void configForConsole(final Slf4jLevel defaultLevel) {
		configForConsole(defaultLevel, new Object[] {});
	}


	/**
	 * Initial log4j configuration. Fails if no log4j present; should be called
	 * only from main(), but can be called repeatedly without ill effects (only
	 * the 1st call will set the pattern, though).
	 *
	 * @param defaultLevel
	 * @param otherLevels
	 *            if you want loggers "A" and "B" to use Debug, and logger "C"
	 *            to use Warn, you would pass in new Object[] {"A", Debug, "B",
	 *            Debug, "C", Warn}; null is also valid.
	 */
	public static void configForConsole(final Slf4jLevel defaultLevel,
			final Object[] otherLevels) {
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel",
				defaultLevel.toString());
		if (!isLog4jPresent) {
			System.err
					.println("Log4j is not present. Configuration request ignored");
		} else {
			new Runnable() {
				@Override
				public void run() {
					Logger root = Logger.getRootLogger();
					if (!root.getAllAppenders().hasMoreElements()) {
						root.setLevel(defaultLevel.getLog4jLevel());
						root.addAppender(new ConsoleAppender(new PatternLayout(
								"%-5p %x [%c{1}|%t]: %m%n")));
					}
					if (otherLevels != null) {
						for (int i = 0; i < otherLevels.length; i += 2) {
							String loggerName = (otherLevels[i] instanceof Class) ? ((Class) otherLevels[i])
									.getName()
									: (String) otherLevels[i];
							Slf4jLevel level = (Slf4jLevel) otherLevels[i + 1];
							setLevel(loggerName, level);
						}
					}
				}
			}.run();
		}
	}

	/**
	 * Sets a logger to a level.
	 *
	 * @param loggerName
	 * @param level
	 */
	public static void setLevel(final String loggerName, final Slf4jLevel level) {
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", level
				.toString());
		if (!isLog4jPresent) {
			System.err
					.println("Log4j is not present. Configuration request ignored");
		} else {
			new Runnable() {
				@Override
				public void run() {
					Logger.getLogger(loggerName)
							.setLevel(level.getLog4jLevel());
				}
			}.run();
		}
	}
}
