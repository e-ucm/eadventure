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

package ead.editor;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Simple configuration for log4j logging. Should be called from "main" files
 * or unit-tests in this directory. A check for log4j existence is performed
 * first, to bail out cleanly if it is not present.
 * @author mfreire
 */
public class Log4jConfig {
    
    /**
     * Decouples slf4j levels from log4j levels. This avoids adding any mention
     * to log4j in the calling class, or to rely on they being basically the same.
     */
    public enum Slf4jLevel {
        Trace(Level.TRACE),
        Debug(Level.DEBUG),
        Info(Level.INFO),
        Warn(Level.WARN),
        Error(Level.ERROR),
        Fatal(Level.FATAL);
        
        private final Level log4jLevel;
        Slf4jLevel(Level log4jLevel) {
            this.log4jLevel = log4jLevel;
        }
        public Level getLog4jLevel() {
            return log4jLevel;
        }
    }
    
    private static boolean isLog4jPresent() {
        Class<?> c = null;
        try {
            c = Log4jConfig.class.getClassLoader()
                .loadClass("org.apache.log4j.ConsoleAppender");
        } catch (Exception e) {
            // loading of a sample log4j class failed: no log4j for you
        }
        return c != null;
    }
    
    /**
     * Initial log4j configuration. Fails if no log4j present; should be 
     * called only from main().
     * @param defaultLevel
     * @param otherLevels if you want loggers "A" and "B" to use Debug, 
     * and logger "C" to use Warn, you would pass in
     * new Object[] {"A", Debug, "B", Debug, "C", Warn}; null is also valid.
     */
    public static void configForConsole(Slf4jLevel defaultLevel, Object[] otherLevels) {
        if ( ! isLog4jPresent()) {
            System.err.println("Log4j is not present. Configuration request ignored");
            return;
        }
        
        Logger root = Logger.getRootLogger();
        if ( ! root.getAllAppenders().hasMoreElements()) {
            root.setLevel(defaultLevel.getLog4jLevel());
            root.addAppender(new ConsoleAppender(
                    new PatternLayout("%-5p [%c|%t]: %m%n")));
        }        
        if (otherLevels != null) {
            for (int i=0; i<otherLevels.length; i+=2) {
                String loggerName = (String)otherLevels[i];
                Slf4jLevel level = (Slf4jLevel)otherLevels[i+1];
                setLevel(loggerName, level);
            }
        }
    }    
    
    /**
     * Sets a logger to a level.
     * @param loggerName
     * @param level 
     */
    public static void setLevel(String loggerName, Slf4jLevel level) {
        Logger.getLogger(loggerName).setLevel(level.getLog4jLevel());
    }
}
