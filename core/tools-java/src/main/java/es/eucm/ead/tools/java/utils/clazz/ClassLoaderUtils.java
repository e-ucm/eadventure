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

package es.eucm.ead.tools.java.utils.clazz;

import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ClassLoaderUtils {

	/**
	 * Mask containing public and static modifiers.
	 */
	public static final int PUBLIC_STATIC_MOD_MASK = Modifier.PUBLIC
			| Modifier.STATIC;

	/**
	 * Return a <code>ClassLoader</code>.
	 *
	 * <p>
	 * Return a suitable <code>ClassLoader</code> using the following
	 * algorithm:
	 * </p>
	 * <p>
	 * <ol>
	 * <li>Return the context <code>ClassLoader</code> if available.</li>
	 * <li>Return the <code>clazz</code> <code>ClassLoader</code>.</li>
	 * <li>Return the {@link ClassLoader#getSystemClassLoader()}} </li>
	 * </ol>
	 * </p>
	 *
	 * @param clazz
	 *            <code>Class</code> to use
	 *
	 * @return A <code>ClassLoader</code>
	 * @throws SecurityException
	 *             If a security manager exists and its checkPermission method
	 *             doesn't allow access to the system class loader.
	 */
	static public ClassLoader getClassLoader(Class<?> clazz) {
		ClassLoader cl = null;
		// 1. If available use the context ClassLoader
		if (System.getSecurityManager() != null) {
			cl = AccessController
					.<ClassLoader> doPrivileged(new PrivilegedAction<ClassLoader>() {
						public ClassLoader run() {
							return Thread.currentThread()
									.getContextClassLoader();
						}
					});
		} else {
			cl = Thread.currentThread().getContextClassLoader();
		}
		// 2. if not use the class ClassLoader
		if (cl == null) {
			cl = clazz.getClassLoader();
		}

		// 3. Use the System ClassLoader as fallback ClassLoader
		if (cl == null) {
			if (System.getSecurityManager() != null) {
				cl = AccessController
						.<ClassLoader> doPrivileged(new PrivilegedAction<ClassLoader>() {
							public ClassLoader run() {
								return ClassLoader.getSystemClassLoader();
							}
						});
			} else {
				cl = ClassLoader.getSystemClassLoader();
			}
		}

		return cl;
	}

	/**
	 * Check if if the <code>actualModifiers</code> contains the
	 * <code>expectedModifiers</code>
	 *
	 * @param actualModifiers
	 *            Element actual modifiers
	 * @param expectedModifiers
	 *            Expected modifiers
	 *
	 * @return <code>true</code> if the <code>actualModifiers</code>
	 *         contains the <code>expectedModifiers</code>,
	 *         <code>false</code> otherwise.
	 *
	 * @see java.lang.reflect.Modifier
	 */
	public static boolean checkModifiers(int actualModifiers,
			int expectedModifiers) {
		return checkModifiers(actualModifiers, expectedModifiers,
				expectedModifiers);
	}

	/**
	 * Check if if the <code>actualModifiers</code> are exactly the
	 * <code>expectedModifiers</code> using <code>mask</code> as
	 * <code>actualModifiers</code> mask.
	 *
	 * @param actualModifiers
	 *            Element actual modifiers
	 * @param expectedModifiers
	 *            Expected modifiers
	 * @param mask
	 *            Mask used to compare the modifiers
	 *
	 * @return <code>true</code> if the <code>actualModifiers</code> are
	 *         exactly the <code>expectedModifiers</code>, <code>false</code>
	 *         otherwise.
	 *
	 * @see java.lang.reflect.Modifier
	 */
	public static boolean checkModifiers(int actualModifiers,
			int expectedModifiers, int mask) {
		return (actualModifiers & mask) == expectedModifiers;
	}

}
