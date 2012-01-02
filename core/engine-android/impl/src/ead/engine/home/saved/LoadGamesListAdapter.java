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

package ead.engine.home.saved;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ead.engine.R;
import ead.engine.home.saved.LoadGamesArray.InfoLoadGames;

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
