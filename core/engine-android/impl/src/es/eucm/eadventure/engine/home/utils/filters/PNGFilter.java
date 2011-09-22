package es.eucm.eadventure.engine.home.utils.filters;

import java.io.File;
import java.io.FilenameFilter;

public class PNGFilter implements FilenameFilter {

		public boolean accept(File f, String name) {

			return ((name.endsWith(".png") == true));

		}

	}

