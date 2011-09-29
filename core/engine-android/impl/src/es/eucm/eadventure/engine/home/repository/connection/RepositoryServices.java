package es.eucm.eadventure.engine.home.repository.connection;

import android.content.Context;
import android.os.Handler;
import es.eucm.eadventure.engine.home.repository.database.GameInfo;
import es.eucm.eadventure.engine.home.repository.database.RepositoryDatabase;

/**
 * Useful services for updating the repository database and downloading ead games
 * 
 * @author Roberto Tornero
 */
public class RepositoryServices {

	/**
	 * The thread that updates the database
	 */
	private UpdateDatabaseThread data_updater;
	/**
	 * The thread that downloads games
	 */
	private DownloadGameThread game_downloader;

	/**
	 * Starts {@link data_updater}
	 */
	public void updateDatabase(Context c, Handler handler, RepositoryDatabase rd) {

		data_updater = new UpdateDatabaseThread(handler, rd);
		data_updater.start();
	}

	/**
	 * Starts {@link game_downloader}
	 */
	public void downloadGame(Context c , Handler handler , GameInfo game) {

		game_downloader = new DownloadGameThread(c,handler,game);
		game_downloader.start();		
	}

}
