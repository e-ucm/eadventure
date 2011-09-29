package es.eucm.eadventure.engine.home.local;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.eucm.eadventure.engine.R;
import es.eucm.eadventure.engine.home.repository.database.GameInfo;

/**
 * An adapter to store the installed games
 * 
 * @author Roberto Tornero
 */
public class LocalGamesListAdapter  extends ArrayAdapter<GameInfo> {

	/**
	 * The list of installed games
	 */
	private ArrayList<GameInfo> items;

	/**
	 * Constructor
	 */
	public LocalGamesListAdapter(Context context, int textViewResourceId, ArrayList<GameInfo> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	/**
	 * The view for each list item. Contains the name of the game, its description and its icon image
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.local_games_listitem, null);
		}
		GameInfo game = items.get(position);
		if (game != null) {
			TextView tt = (TextView) v.findViewById(R.id.toptext);
			TextView bt = (TextView) v.findViewById(R.id.bottomtext);
			ImageView iv = (ImageView) v.findViewById(R.id.icon);
			if (tt != null) {
				tt.setText(game.getGameTitle());                            }
			if(bt != null){
				bt.setText(game.getGameDescription());
			}
			if(iv != null){
				if (game.getImageIcon()!=null)
					iv.setImageBitmap(game.getImageIcon());
			}

		}
		return v;
	}

}


