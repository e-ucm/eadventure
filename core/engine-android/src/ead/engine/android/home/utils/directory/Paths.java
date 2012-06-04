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

package ead.engine.android.home.utils.directory;

public final class Paths {

	public static final class repository {
		
		public static final String DEFAULT_PATH = "http://eadventure-android.googlecode.com/files/";
		public static final String SOURCE_XML = "repository.xml";

	}

	public static final class eaddirectory {
		
		public static final String ROOT_PATH = Paths.device.EXTERNAL_STORAGE + "EadAndroid/";
		public static final String GAMES_PATH = ROOT_PATH + "games/" ;
		public static final String REPORTS_PATH = ROOT_PATH + "reports/" ;
		public static final String SAVED_GAMES_PATH = ROOT_PATH + "saved_games/" ;
		public static final String PREFERENCES = "preferences/";

	}

	public static final class device {

//		public static final String EXTERNAL_STORAGE = Environment
//				.getExternalStorageDirectory().toString() + "/";
		// FIXME En froyo cambia a /mnt/sdcard/ y no lo lee
		public static final String EXTERNAL_STORAGE = "/sdcard/";
	}

}
