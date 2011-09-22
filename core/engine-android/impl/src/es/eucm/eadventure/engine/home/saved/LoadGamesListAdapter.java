package es.eucm.eadventure.engine.home.saved;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.eucm.eadventure.engine.R;
import es.eucm.eadventure.engine.home.saved.LoadGamesArray.InfoLoadGames;

/**
 * An adapter for showing each saved game with its information in the list
 */
public class LoadGamesListAdapter extends BaseAdapter {

	/**
	 * The list of saved games
	 */
	private ArrayList<InfoLoadGames> infoSaved;
	/**
	 * The context of the application to get the ViewInflater
	 */
	private Context con;

	/**
	 * Constructor of the adapter
	 */
	public LoadGamesListAdapter(Context cont, ArrayList<InfoLoadGames> info) {

		super();
		this.con = cont;
		this.infoSaved = info;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infoSaved.get(position);
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * Create the view by inflating the proper layout
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		InfoLoadGames savedGame = infoSaved.get(position);

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) con
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.saved_games_listitem, null);
		}

		TextView gameText = (TextView) v.findViewById(R.id.toptext);
		TextView saveText = (TextView) v.findViewById(R.id.bottomtext);
		ImageView iconV = (ImageView) v.findViewById(R.id.icon);

		if (gameText != null) {
			gameText.setText(savedGame.getGame());
		}

		if (saveText != null) {
			saveText.setText(savedGame.getSaved());
		}

		if (iconV != null) {

			if (savedGame.getScreenShot() != null)

				iconV.setImageBitmap(savedGame.getScreenShot());
		}

		return v;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		// TODO Auto-generated method stub
		return infoSaved.size();
	}



}
