package es.eucm.eadventure.engine.home.repository.database;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * Represents the information that each game has on the repository
 * 
 * @author Roberto Tornero
 */
public class GameInfo implements Comparable<GameInfo> , Serializable {

	/**
	 * Tag for the class GameInfo
	 */
	public static final String TAG = "GameInfoCache";
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Stored information for each game on the repository
	 */
	private String gameTitle = "";
	private String gameDescription = "";
	private String eadUrl;
	private Bitmap imageIcon;
	private String gameFileName;

	/**
	 * Constructor
	 */
	public GameInfo(String ti, String desc, String eadURL, Bitmap imageIcon) {

		gameDescription = desc;
		gameTitle = ti;
		eadUrl = eadURL;
		this.imageIcon = imageIcon;

		int last = eadURL.lastIndexOf("/");
		gameFileName = eadURL.substring(last + 1);

	}

	/**
	 * Allows comparing between two instances of GameInfo
	 */
	public int compareTo(GameInfo other) {

		if (this.gameDescription != null)
			return this.gameDescription.compareTo(other.gameDescription);
		else
			throw new IllegalArgumentException();
	}

	/**
	 * Returns the title of the game
	 */
	public String getGameTitle() {
		return gameTitle;
	}

	/**
	 * Returns the description of the game
	 */
	public String getGameDescription() {
		return gameDescription;
	}

	/**
	 * Returns the url of the game
	 */
	public String getEadUrl() {
		return eadUrl;
	}

	/**
	 * Returns the name of the game file
	 */
	public String getFileName() {
		return gameFileName;
	}

	/**
	 * Returns the icon image of the game
	 */
	public Bitmap getImageIcon() {
		return imageIcon;
	}


}