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

/*
 * Adapted from JDK14LoggerAdapter v. 1.6.4; licensed as follows: 
 * 
 * Copyright (c) 2004-2005 SLF4J.ORG
 * Copyright (c) 2004-2005 QOS.ch
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute, and/or sell copies of  the Software, and to permit persons
 * to whom  the Software is furnished  to do so, provided  that the above
 * copyright notice(s) and this permission notice appear in all copies of
 * the  Software and  that both  the above  copyright notice(s)  and this
 * permission notice appear in supporting documentation.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR  A PARTICULAR PURPOSE AND NONINFRINGEMENT
 * OF  THIRD PARTY  RIGHTS. IN  NO EVENT  SHALL THE  COPYRIGHT  HOLDER OR
 * HOLDERS  INCLUDED IN  THIS  NOTICE BE  LIABLE  FOR ANY  CLAIM, OR  ANY
 * SPECIAL INDIRECT  OR CONSEQUENTIAL DAMAGES, OR  ANY DAMAGES WHATSOEVER
 * RESULTING FROM LOSS  OF USE, DATA OR PROFITS, WHETHER  IN AN ACTION OF
 * CONTRACT, NEGLIGENCE  OR OTHER TORTIOUS  ACTION, ARISING OUT OF  OR IN
 * CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 * Except as  contained in  this notice, the  name of a  copyright holder
 * shall not be used in advertising or otherwise to promote the sale, use
 * or other dealings in this Software without prior written authorization
 * of the copyright holder.
 *
 */

package org.slf4j;

import org.slf4j.MessageFormatter.FormattingTuple;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A wrapper over {@link java.util.logging.Logger java.util.logging.Logger} in
 * conformity with the {@link Logger} interface. Note that the logging levels
 * mentioned in this class refer to those defined in the java.util.logging
 * package.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * @author Peter Royal
 */
public final class Logger {

	private final String name;

	final java.util.logging.Logger logger;

	// WARN: JDK14LoggerAdapter constructor should have only package access so
	// that only JDK14LoggerFactory be able to create one.
	Logger(java.util.logging.Logger logger) {
		this.logger = logger;
		this.name = logger.getName();
	}

	/**
	 * Returns own name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Is this logger instance enabled for the FINEST level?
	 * 
	 * @return True if this Logger is enabled for level FINEST, false otherwise.
	 */
	public boolean isTraceEnabled() {
		return logger.isLoggable(Level.FINEST);
	}

	/**
	 * Log a message object at level FINEST.
	 * 
	 * @param msg
	 *            - the message object to be logged
	 */
	public void trace(String msg) {
		if (logger.isLoggable(Level.FINEST)) {
			log(SELF, Level.FINEST, msg, null);
		}
	}

	/**
	 * Log a message at level FINEST according to the specified format and
	 * argument.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for level FINEST.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg
	 *            the argument
	 */
	public void trace(String format, Object arg) {
		if (logger.isLoggable(Level.FINEST)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at level FINEST according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the FINEST level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg1
	 *            the first argument
	 * @param arg2
	 *            the second argument
	 */
	public void trace(String format, Object arg1, Object arg2) {
		if (logger.isLoggable(Level.FINEST)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at level FINEST according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the FINEST level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param argArray
	 *            an array of arguments
	 */
	public void trace(String format, Object[] argArray) {
		if (logger.isLoggable(Level.FINEST)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
			log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log an exception (throwable) at level FINEST with an accompanying
	 * message.
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param t
	 *            the exception (throwable) to log
	 */
	public void trace(String msg, Throwable t) {
		if (logger.isLoggable(Level.FINEST)) {
			log(SELF, Level.FINEST, msg, t);
		}
	}

	/**
	 * Is this logger instance enabled for the FINE level?
	 * 
	 * @return True if this Logger is enabled for level FINE, false otherwise.
	 */
	public boolean isDebugEnabled() {
		return logger.isLoggable(Level.FINE);
	}

	/**
	 * Log a message object at level FINE.
	 * 
	 * @param msg
	 *            - the message object to be logged
	 */
	public void debug(String msg) {
		if (logger.isLoggable(Level.FINE)) {
			log(SELF, Level.FINE, msg, null);
		}
	}

	/**
	 * Log a message at level FINE according to the specified format and
	 * argument.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for level FINE.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg
	 *            the argument
	 */
	public void debug(String format, Object arg) {
		if (logger.isLoggable(Level.FINE)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at level FINE according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the FINE level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg1
	 *            the first argument
	 * @param arg2
	 *            the second argument
	 */
	public void debug(String format, Object arg1, Object arg2) {
		if (logger.isLoggable(Level.FINE)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at level FINE according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the FINE level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param argArray
	 *            an array of arguments
	 */
	public void debug(String format, Object... argArray) {
		if (logger.isLoggable(Level.FINE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
			log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log an exception (throwable) at level FINE with an accompanying message.
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param t
	 *            the exception (throwable) to log
	 */
	public void debug(String msg, Throwable t) {
		if (logger.isLoggable(Level.FINE)) {
			log(SELF, Level.FINE, msg, t);
		}
	}

	/**
	 * Is this logger instance enabled for the INFO level?
	 * 
	 * @return True if this Logger is enabled for the INFO level, false
	 *         otherwise.
	 */
	public boolean isInfoEnabled() {
		return logger.isLoggable(Level.INFO);
	}

	/**
	 * Log a message object at the INFO level.
	 * 
	 * @param msg
	 *            - the message object to be logged
	 */
	public void info(String msg) {
		if (logger.isLoggable(Level.INFO)) {
			log(SELF, Level.INFO, msg, null);
		}
	}

	/**
	 * Log a message at level INFO according to the specified format and
	 * argument.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the INFO level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg
	 *            the argument
	 */
	public void info(String format, Object arg) {
		if (logger.isLoggable(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at the INFO level according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the INFO level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg1
	 *            the first argument
	 * @param arg2
	 *            the second argument
	 */
	public void info(String format, Object arg1, Object arg2) {
		if (logger.isLoggable(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at level INFO according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the INFO level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param argArray
	 *            an array of arguments
	 */
	public void info(String format, Object[] argArray) {
		if (logger.isLoggable(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
			log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log an exception (throwable) at the INFO level with an accompanying
	 * message.
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param t
	 *            the exception (throwable) to log
	 */
	public void info(String msg, Throwable t) {
		if (logger.isLoggable(Level.INFO)) {
			log(SELF, Level.INFO, msg, t);
		}
	}

	/**
	 * Is this logger instance enabled for the WARNING level?
	 * 
	 * @return True if this Logger is enabled for the WARNING level, false
	 *         otherwise.
	 */
	public boolean isWarnEnabled() {
		return logger.isLoggable(Level.WARNING);
	}

	/**
	 * Log a message object at the WARNING level.
	 * 
	 * @param msg
	 *            - the message object to be logged
	 */
	public void warn(String msg) {
		if (logger.isLoggable(Level.WARNING)) {
			log(SELF, Level.WARNING, msg, null);
		}
	}

	/**
	 * Log a message at the WARNING level according to the specified format and
	 * argument.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the WARNING level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg
	 *            the argument
	 */
	public void warn(String format, Object arg) {
		if (logger.isLoggable(Level.WARNING)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at the WARNING level according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the WARNING level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg1
	 *            the first argument
	 * @param arg2
	 *            the second argument
	 */
	public void warn(String format, Object arg1, Object arg2) {
		if (logger.isLoggable(Level.WARNING)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at level WARNING according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the WARNING level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param argArray
	 *            an array of arguments
	 */
	public void warn(String format, Object[] argArray) {
		if (logger.isLoggable(Level.WARNING)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
			log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log an exception (throwable) at the WARNING level with an accompanying
	 * message.
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param t
	 *            the exception (throwable) to log
	 */
	public void warn(String msg, Throwable t) {
		if (logger.isLoggable(Level.WARNING)) {
			log(SELF, Level.WARNING, msg, t);
		}
	}

	/**
	 * Is this logger instance enabled for level SEVERE?
	 * 
	 * @return True if this Logger is enabled for level SEVERE, false otherwise.
	 */
	public boolean isErrorEnabled() {
		return logger.isLoggable(Level.SEVERE);
	}

	/**
	 * Log a message object at the SEVERE level.
	 * 
	 * @param msg
	 *            - the message object to be logged
	 */
	public void error(String msg) {
		if (logger.isLoggable(Level.SEVERE)) {
			log(SELF, Level.SEVERE, msg, null);
		}
	}

	/**
	 * Log a message at the SEVERE level according to the specified format and
	 * argument.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the SEVERE level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg
	 *            the argument
	 */
	public void error(String format, Object arg) {
		if (logger.isLoggable(Level.SEVERE)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at the SEVERE level according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the SEVERE level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param arg1
	 *            the first argument
	 * @param arg2
	 *            the second argument
	 */
	public void error(String format, Object arg1, Object arg2) {
		if (logger.isLoggable(Level.SEVERE)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log a message at level SEVERE according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the SEVERE level.
	 * </p>
	 * 
	 * @param format
	 *            the format string
	 * @param argArray
	 *            an array of arguments
	 */
	public void error(String format, Object[] argArray) {
		if (logger.isLoggable(Level.SEVERE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
			log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
		}
	}

	/**
	 * Log an exception (throwable) at the SEVERE level with an accompanying
	 * message.
	 * 
	 * @param msg
	 *            the message accompanying the exception
	 * @param t
	 *            the exception (throwable) to log
	 */
	public void error(String msg, Throwable t) {
		if (logger.isLoggable(Level.SEVERE)) {
			log(SELF, Level.SEVERE, msg, t);
		}
	}

	/**
	 * Log the message at the specified level with the specified throwable if
	 * any. This method creates a LogRecord and fills in caller date before
	 * calling this instance's JDK14 logger.
	 * 
	 * See bug report #13 for more details.
	 * 
	 * @param level
	 * @param msg
	 * @param t
	 */
	private void log(String callerFQCN, Level level, String msg, Throwable t) {
		// millis and thread are filled by the constructor
		LogRecord record = new LogRecord(level, msg);
		record.setLoggerName(getName());
		record.setThrown(t);
		fillCallerData(callerFQCN, record);
		logger.log(record);
	}

	static String SELF = Logger.class.getName();

	// not using markers
	// was: static String SUPER = MarkerIgnoringBase.class.getName();
	static String SUPER = SELF;

	/**
	 * Fill in caller data if possible.
	 * 
	 * @param record
	 *            The record to update
	 */
	final private void fillCallerData(String callerFQCN, LogRecord record) {
		// NOP, since GWT does not implement LogRecord.setSourceClassName or setSourceMethodName 
	}
}
