package es.eucm.eadventure.common.elmentfactories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;

/**
 * A factory providing {@link EAdString}s for testing
 * 
 */
@Singleton
public class StringFactory {

	private static int ID_GENERATOR = 0;

	public enum StringType {
		VERY_LONG_STRING, LONG_STRING, MEDIUM_STRING, SHORT_STRING;

		/**
		 * Returns the string associated to this {@link StringType}
		 * 
		 * @return
		 */
		public String getString() {
			switch (this) {
			case VERY_LONG_STRING:
				String s = "";
				for ( int i = 0; i < 5; i++ ){
					s += LONG_STRING.getString() + MEDIUM_STRING.getString() + SHORT_STRING.getString();
				}
				return s;
			case LONG_STRING:
				return "This is a very looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong string, to test show text effects and other eventso or effectes related to text. Just to make sure that evvvvvveryyyyyyyythinnnngggggg is OKKKKKKK!!!!!!!!!!!!!";
			case MEDIUM_STRING:
				return "Hi there! It's everythin OK or what?";
			case SHORT_STRING:
				return "OK.";
			default:
				return "The default string. Never mind.";

			}
		}
	}

	private static ArrayList<EAdString> strings;

	private Map<EAdString, String> userStrings;

	public StringFactory() {
		userStrings = new HashMap<EAdString, String>();
		if (strings == null) {
			strings = new ArrayList<EAdString>();
			for (StringType type : StringType.values()) {
				EAdString string = new EAdString(type.toString());
				strings.add(string);
			}
		}
	}

	/**
	 * Adds the strings define by this factory to the given string handler
	 * 
	 * @param stringHandler
	 *            string handler
	 */
	public void addStrings(StringHandler stringHandler) {
		int i = 0;
		for (EAdString string : strings) {
			stringHandler.addString(string,
					StringType.values()[i++].getString());
		}

		for (EAdString string : userStrings.keySet()) {
			stringHandler.addString(string, userStrings.get(string));
		}
	}

	/**
	 * Returns the {@EAdString} associated to given string type
	 * 
	 * @param stringType
	 *            the string type
	 * @return the {@EAdString} associated to given string type
	 */
	public EAdString getString(StringType stringType) {
		return strings.get(stringType.ordinal());
	}

	/**
	 * Creates an EAdString for the given string
	 * 
	 * @param string
	 * @return
	 */
	public EAdString getString(String string) {
		EAdString eadString = new EAdString("string" + ID_GENERATOR
				+ (int) (Math.random() * 100));
		userStrings.put(eadString, string);
		return eadString;
	}
}
