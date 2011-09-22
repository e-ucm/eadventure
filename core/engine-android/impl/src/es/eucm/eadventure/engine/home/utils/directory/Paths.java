package es.eucm.eadventure.engine.home.utils.directory;

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
