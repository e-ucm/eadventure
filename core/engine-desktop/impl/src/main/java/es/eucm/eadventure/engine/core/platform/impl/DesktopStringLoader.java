package es.eucm.eadventure.engine.core.platform.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;

public class DesktopStringLoader {
	
	public static void loadStrings(StringHandler sh, InputStream inputStream) {
		Properties properties = new Properties();
 		try {
			properties.load(inputStream);
			for (Object key : properties.keySet())
				sh.addString(new EAdString((String) key),
						properties.getProperty((String) key));
		} catch (IOException e) {
			(Logger.getLogger("DesktopStringLoader")).log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
