package es.eucm.eadventure.engine.home.local;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import es.eucm.eadventure.engine.home.WorkspaceActivity.LocalGamesListFragment.LGAHandlerMessages;
import es.eucm.eadventure.engine.home.repository.handler.RepoResourceHandler;

/**
 * A thread to delete existing games
 * 
 * @author Roberto Tornero
 */
public class DeletingGame extends Thread {

	/**
	 * The handler that controls if the game is deleted
	 */
	private Handler handler;
	/**
	 * The paths to delete the game from
	 */
	private String[] paths;

	/**
	 * Constructor
	 */
	public DeletingGame(Handler han,String[] paths) {

		super();
		this.handler=han;
		this.paths=paths;
	}

	/**
	 * The thread to delete games
	 */
	@Override
	public void run() {

		RepoResourceHandler.deleteFile(paths[0]);
		RepoResourceHandler.deleteFile(paths[1]);

		Message msg = handler.obtainMessage();
		Bundle b = new Bundle();

		msg.what = LGAHandlerMessages.DELETING_GAME;
		msg.setData(b);
		msg.sendToTarget();
	}

}
