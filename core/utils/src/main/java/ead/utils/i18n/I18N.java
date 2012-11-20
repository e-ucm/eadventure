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

package ead.utils.i18n;

import ead.utils.clazz.ClassLoaderUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common superclass for all message bundle classes. Provides convenience
 * methods for manipulating messages. <p> The
 * <code>#bind</code> methods perform string substitution and should be
 * considered a convenience and <em>not</em> a full substitute replacement for
 * <code>MessageFormat#format</code> method calls. </p> <p> Text appearing
 * within curly braces in the given message, will be interpreted as a numeric
 * index to the corresponding substitution object in the given array. Calling
 * the
 * <code>#bind</code> methods with text that does not map to an integer will
 * result in an {@link java.lang.IllegalArgumentException} </p> <p> Text
 * appearing within single quotes is treated as a literal. A single quote is
 * escaped by a preceeding single quote. </p> <p> Clients who wish to use the
 * full substitution power of the
 * <code>MessageFormat</code> class should call that class directly and not use
 * these
 * <code>#bind</code> methods. </p> <p> Clients may subclass this type. </p>
 *
 * <p>This class is based on Eclipse {@link org.eclipse.osgi.util.NLS} class</p>
 */
public abstract class I18N {

	private static final Logger logger = LoggerFactory.getLogger(I18N.class
			.getName());

	private static final String referenceValueRegex = "[{]([a-z][a-z0-9_]+)[}]";

	/**
	 * Creates a new I18N instance.
	 */
	protected I18N() {
		super();
	}

	//
	// API methods
	//
	/**
	 * Bind the given message's substitution locations with the given string
	 * values.
	 *
	 * @param message the message to be manipulated
	 * @param bindings An array of objects to be inserted into the message
	 * @return the manipulated String
	 */
	public static String bind(String message, Object... bindings) {
		if (message == null) {
			return "No message available."; //$NON-NLS-1$
		}
		if (bindings == null || bindings.length == 0) {
			bindings = EMPTY_ARGS;
		}

		int length = message.length();
		// estimate correct size of string buffer to avoid growth
		int bufLen = length + (bindings.length * 5);
		StringBuilder buffer = new StringBuilder(bufLen);
		int i = 0;
		while (i < length) {
			char c = message.charAt(i);
			switch (c) {
			case '{':
				int index = message.indexOf('}', i);
				// if we don't have a matching closing brace then...
				if (index == -1) {
					buffer.append(c);
					break;
				}
				i++;
				if (i >= length) {
					buffer.append(c);
					break;
				}
				// look for a substitution
				int number = -1;
				String numberSubstring = message.substring(i, index);
				try {
					number = Integer.parseInt(numberSubstring);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("In message " + message
							+ ", '" + numberSubstring + "' is not a number");
				}
				if (number >= bindings.length || number < 0) {
					logger.warn("Missing argument for {} in {}", new Object[] {
							numberSubstring, message });
					buffer.append("<missing argument>"); //$NON-NLS-1$
					i = index;
					break;
				}
				buffer.append(bindings[number]);
				i = index;
				break;
			case '\'':
				// if a single quote is the last char on the line then skip it
				int nextIndex = i + 1;
				if (nextIndex >= length) {
					buffer.append(c);
					break;
				}
				char next = message.charAt(nextIndex);
				// if the next char is another single quote then write out one
				if (next == '\'') {
					i++;
					buffer.append(c);
					break;
				}
				// otherwise we want to read until we get to the next single
				// quote
				index = message.indexOf('\'', nextIndex);
				// if there are no more in the string, then skip it
				if (index == -1) {
					buffer.append(c);
					break;
				}
				// otherwise write out the chars inside the quotes
				buffer.append(message.substring(nextIndex, index));
				i = index;
				break;
			default:
				buffer.append(c);
			}
			i++;
		}
		return buffer.toString();
	}

	/**
	 * Initialize the given class with the values from the specified message
	 * bundle.
	 *
	 * @param bundleName fully qualified path of the class name
	 * @param clazz the class where the constants will exist
	 */
	public static void initializeMessages(final String bundleName,
			final Class<?> clazz) {
		if (System.getSecurityManager() == null) {
			loadMessages(bundleName, clazz);
			return;
		}
		AccessController.doPrivileged(new PrivilegedAction<Object>() {

			@Override
			public Object run() {
				loadMessages(bundleName, clazz);
				return null;
			}
		});
	}

	/**
	 * Initialize the given class with the values from the specified resources
	 * bundle.
	 *
	 * @param bundleName fully qualified path of the class name
	 * @param clazz the class where the constants will exist
	 * @param files
	 */
	public static void initializeResources(final String bundleName,
			final Class<?> clazz, Set<String> files) {
		loadResources(bundleName, clazz, files);
	}

	//
	// Internal methods
	//
	/**
	 * Expected field's modifiers.
	 */
	private static final int MOD_EXPECTED = Modifier.PUBLIC | Modifier.STATIC;
	/**
	 * Field's modifiers mask
	 */
	private static final int MOD_MASK = MOD_EXPECTED | Modifier.FINAL;

	/**
	 * Load the given resource bundle using the specified class loader.
	 *
	 * @param baseName the base name of the resource bundle, a fully qualified
	 * class name
	 * @param clazz
	 * <code>Class</code> holding the messages.
	 */
	private static void loadMessages(final String baseName, final Class<?> clazz) {
		loadMessages(baseName, clazz, Locale.getDefault());
	}

	/**
	 * Resolves all references within a string. A reference is something of 
	 * the form {key}, which is replaced by props.get(key). Replacement is 
	 * performed until no references remain, or until a set limit of
	 * replacement-iterations is reached.
	 * 
	 * The final, substituted value is stored in its key for further reference.
	 * 
	 * @param input
	 * @param props
	 * @return substituted value
	 */
	private static String resolveReferences(String key, String initialValue,
			Properties props) {
		Pattern p = Pattern.compile(referenceValueRegex);

		String output = initialValue;
		int maxIterations = 10;
		boolean found = false;
		for (int i = 0; i < maxIterations; i++) {
			Matcher m = p.matcher(output);
			found = false;
			StringBuffer sb = new StringBuffer();
			while (m.find()) {
				found = true;
				String value = props.getProperty(m.group(1));
				if (value == null) {
					value = m.group().replaceAll("[{}]", "?");
					logger
							.warn(
									"Could not resolve value {} in key {}, iteration {}",
									new String[] { m.group(), key, "" + (i + 1) });
				} else {
					logger
							.debug(
									"Resolved value {} in key {} as {}, iteration {}",
									new String[] { m.group(), key, value,
											"" + (i + 1) });
				}
				m.appendReplacement(sb, value);
			}

			if (!found) {
				break;
			}

			// prepare for next iteration
			m.appendTail(sb);
			output = sb.toString();
		}

		if (found) {
			// too many iterations: bail out
			logger
					.warn(
							"Circular reference in key {}: reached {} replacement iterations",
							new String[] { key, "" + maxIterations });
		}
		props.setProperty(key, output);
		return output;
	}

	/**
	 * Load the
	 * <code>baseName</code> messages bundle and initialize
	 * <code>clazz</code> fields.
	 *
	 * <p> This method initializes
	 * <code>clazz</code> public static String fields using the messages loaded
	 * from <code>baseName</code> bundle. </p>. References to other keys
	 * are replaced; but no guarantee is made regarding order-of-interpretation.
	 *
	 * @param baseName the base name of the resource bundle, a fully qualified
	 * class name
	 * @param locale the locale for which a resource bundle is desired
	 * @param clazz
	 * <code>Class</code> holding the messages.
	 */
	private static void loadMessages(final String baseName,
			final Class<?> clazz, Locale locale) {
		synchronized (clazz) {
			Field[] fieldDecls = clazz.getDeclaredFields();
			Map<String, Field> fields = new HashMap<String, Field>();
			Set<String> fieldNames = new HashSet<String>();
			for (int i = 0; i < fieldDecls.length; i++) {
				String name = fieldDecls[i].getName();
				fields.put(name, fieldDecls[i]);
				fieldNames.add(name);
			}

			// Compute bundle file names using the default system Locale
			boolean noBundleFound = true;
			String[] fileNames = buildBundleFileNames(baseName, locale);
			ClassLoader loader = ClassLoaderUtils.getClassLoader(I18N.class);
			for (int i = 0; i < fileNames.length; i++) {
				InputStream input = loader.getResourceAsStream(fileNames[i]);
				logger.debug("Searching for file {} for bundle {}",
						fileNames[i], baseName);
				if (input == null) {
					logger.debug("Bundle-file NOT FOUND in classpath:'{}'",
							fileNames[i]);
					continue;
				}

				logger.info("Processing file {} for bundle {}", fileNames[i],
						baseName);
				try {
					Properties props = new Properties();
					props.load(input);

					for (Map.Entry<Object, Object> e : props.entrySet()) {
						String key = (String) e.getKey();
						String value = resolveReferences(key, e.getValue()
								.toString(), props);

						if (fieldNames.contains(key)) {
							if (fields.containsKey(key)) {
								logger.debug("setting key {} to '{}'",
										new String[] {
												fields.get(key).getName(),
												value });
								assignField(fields.get(key), e.getValue());
								fields.remove(key);
							}
						} else {
							logger.warn("Bundle '{}' has an unused message {}",
									new Object[] { baseName, key });
						}
					}
					noBundleFound = false;
				} catch (IOException e) {
					logger.error("Error loading message bundle '{}'", baseName,
							e);
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							logger.error(
									"Error closing stream when loading for {}",
									baseName, e);
						}
					}
				}
			}

			if (noBundleFound) {
				logger.error("No bundle (or fallback) found for {}", baseName);
			}

			//TODO: set the value to the default error resources
			for (Map.Entry<String, Field> e : fields.entrySet()) {
				Field field = e.getValue();
				String value = "Bundle " + baseName + ": message "
						+ field.getName() + " is missing";
				assignField(field, value);
				logger.warn(value);
			}
		}
	}

	private static void assignField(Field field, Object value) {
		// Test if field has static and public modifiers
		if ((field.getModifiers() & MOD_MASK) == MOD_EXPECTED) {
			try {
				/*
				 * Check to see if we are allowed to modify the field. If we
				 * aren't (for instance if the class is not public) then change
				 * the accessible attribute of the field before trying to set
				 * the value.
				 */
				boolean isAccessible = (field.getDeclaringClass()
						.getModifiers() & Modifier.PUBLIC) != 0;
				if (!isAccessible) {
					/*
					 * Set the value into the field. We should never get an
					 * exception here because we know we have a public static
					 * non-final field. If we do get an exception, silently log
					 * it and continue. This means that the field will (most
					 * likely) be un-initialized and will fail later in the code
					 * and if so then we will see both the NPE and this error.
					 */
					boolean oldValue = field.isAccessible();
					field.setAccessible(true);
					field.set(null, value);
					field.setAccessible(oldValue);
				} else {
					field.set(null, value);
				}
			} catch (Exception e) {
				logger.error("Error setting field value for {}", field
						.getName(), e);
			}
		}
	}

	private static final Object[] EMPTY_ARGS = new Object[0];
	private static String[] I18N_SUFFIXES;

	/**
	 * Build an array of property files to search.
	 *
	 * @param bundleName User provided properties file bundle name.
	 * @param locale User provided locale
	 *
	 * @return Returns an array of file names
	 */
	private static String[] buildBundleFileNames(String bundleName,
			Locale locale) {
		if (I18N_SUFFIXES == null) {
			I18N_SUFFIXES = buildBundleFileNameSuffixes(locale);
		}
		bundleName = bundleName.replace('.', '/');
		String[] variants = new String[I18N_SUFFIXES.length];
		for (int i = 0; i < variants.length; i++) {
			variants[i] = bundleName + I18N_SUFFIXES[i];
		}
		return variants;
	}

	/**
	 * Default messages bundle name file extension.
	 */
	private static final String EXTENSION = ".properties";

	/**
	 * Calculate the bundle name suffixes for the system default locale.
	 *
	 * <p> Build the suffixes list for a particular
	 * <code>Locale</code> using a similar algorithm to the described in
	 * {@link java.util.ResourceBundle#getBundle(String, Locale, ClassLoader)}}
	 * </p>
	 *
	 * @param locale
	 * <code>Locale</code> use to build the suffixes list.
	 *
	 * @see java.util.ResourceBundle#getBundle(String, Locale, ClassLoader)
	 * @see java.util.ResourceBundle
	 *
	 * @return Return the list of bundle name suffixes
	 */
	private static String[] buildBundleFileNameSuffixes(Locale locale) {
		ArrayList<String> result = new ArrayList<String>(4);
		String localeString = locale.toString();
		int lastSeparator;

		// 1. Build the list of suffixes using the provided locale
		do {
			result.add('_' + localeString + EXTENSION);
			lastSeparator = localeString.lastIndexOf('_');
			if (lastSeparator != -1) {
				localeString = localeString.substring(0, lastSeparator);
			}
		} while (lastSeparator != -1);

		// 2. Build the list of suffixes using the default locale if needed
		if (!locale.equals(Locale.getDefault())) {
			do {
				result.add('_' + localeString + EXTENSION);
				lastSeparator = localeString.lastIndexOf('_');
				if (lastSeparator != -1) {
					localeString = localeString.substring(0, lastSeparator);
				}
			} while (lastSeparator != -1);
		}

		// 3. Add the default extension
		result.add(EXTENSION);

		return result.toArray(new String[0]);
	}

	/**
	 * Load the resources for the resources class with the default locale
	 *
	 * @param baseName The base name of the class
	 * @param clazz The class
	 * @param files
	 */
	private static void loadResources(final String baseName,
			final Class<?> clazz, Set<String> files) {
		loadResources(baseName, clazz, files, Locale.getDefault());
	}

	/**
	 * Load the resources for the resources class with the given locale
	 *
	 * @param baseName the base name of the class
	 * @param clazz the class
	 * @param files
	 * @param locale the locale
	 */
	private static void loadResources(final String baseName,
			final Class<?> clazz, Set<String> files, Locale locale) {
		synchronized (clazz) {
			Field[] fieldDecls = clazz.getDeclaredFields();
			Map<String, Field> fields = new HashMap<String, Field>();
			List<String> fieldNames = new ArrayList<String>();
			for (int i = 0; i < fieldDecls.length; i++) {
				String name = fieldDecls[i].getName();
				fields.put(name, fieldDecls[i]);
				fieldNames.add(name);
			}

			String[] temp = baseName.split("[\\.$]");
			String baseFolder = temp[temp.length - 1].toLowerCase();

			for (int i = 0; i < fieldNames.size(); i++) {
				String fieldName = fieldNames.get(i);

				for (String name : buildResourceFileNames(fieldName, locale)) {
					if (files.contains(name)) {
						String toAssign = (name
								.matches("[a-z][a-z](_[A-Z][A-Z])?/.*")) ? baseFolder
								+ "-" + name
								: baseFolder + "/" + name;
						assignField(fields.get(fieldName), toAssign);
						fields.remove(fieldName);
						String value = "Bundle '" + baseName + "' resource "
								+ fieldName + " OK";
						logger.debug(value);
						break;
					}
				}
			}

			//TODO: set the value to the default error resources
			for (Map.Entry<String, Field> e : fields.entrySet()) {
				Field field = e.getValue();
				String value = "Bundle " + baseName + ": message "
						+ field.getName() + " is missing";
				assignField(field, value.replaceAll("[ :.]+", "_"));
				logger.warn(value);
			}
		}
	}

	/**
	 * Build all the possible names for the file corresponding to fieldName
	 * 
	 * @param fieldName The name of the field
	 * @param locale The current locale
	 * @return The list of possible file names
	 */
	private static ArrayList<String> buildResourceFileNames(String fieldName,
			Locale locale) {
		ArrayList<String> result = new ArrayList<String>();
		String localeString = locale.toString();
		int lastSeparator;

		// from hi_my_name_is_bob to hi/my/name/is.bob
		int lastUnderscore = fieldName.lastIndexOf('_');
		String fileName = fieldName.substring(0, lastUnderscore) + "."
				+ fieldName.substring(lastUnderscore + 1);
		fileName = fileName.replaceAll("__", "/");

		// 1. Build the list of suffixes using the provided locale
		do {
			result.add(localeString + File.separator + fileName);
			lastSeparator = localeString.lastIndexOf('_');
			if (lastSeparator != -1) {
				localeString = localeString.substring(0, lastSeparator);
			}
		} while (lastSeparator != -1);

		// 2. Build the list of suffixes using the default locale if needed
		if (!locale.equals(Locale.getDefault())) {
			do {
				result.add(localeString + File.separator + fileName);
				lastSeparator = localeString.lastIndexOf('_');
				if (lastSeparator != -1) {
					localeString = localeString.substring(0, lastSeparator);
				}
			} while (lastSeparator != -1);
		}

		// 3. Add the default extension
		result.add(fileName);

		if (logger.isDebugEnabled()) {
			logger.debug("Generated {} alternatives for {}:", new Object[] {
					result.size(), fieldName });
			for (String s : result) {
				logger.debug("\t{}", s);
			}
		}

		return result;
	}
}
