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

package es.eucm.eadventure.engine.home;

import com.markupartist.android.widget.ActionBar;
import es.eucm.eadventure.engine.R;
import es.eucm.eadventure.engine.home.preferences.PreferencesActivity;
import es.eucm.eadventure.engine.home.repository.handler.RepoResourceHandler;
import es.eucm.eadventure.engine.home.utils.directory.Paths;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The home menu of eAdventure Mobile. The several icons displayed allow navigating through 
 * the different screens, preferences included. A link to the eAdventure website is displayed
 * at the bottom. 
 * 
 * @author Roberto Tornero
 */
public class HomeActivity extends Activity {
	
	/**
     * The path of the data (game) included with the Intent
     */
    private String path_from = null;
    /**
     * Id of the installation dialog
     */
    static final int DIALOG_INSTALL_ID = 0;
	
	/**
	 * Creation of the grid of icons 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.home_grid);
	    
	    final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
	    actionBar.setHomeLogo(R.drawable.logo_home);

	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter());

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	
	        	Intent i = createIntent(HomeActivity.this, WorkspaceActivity.class);
	            
	        	switch(position){
	        		case 0: i.putExtra("Tab", 0);
	        				break;
	        		case 1: i.putExtra("Tab", 1);
    						break;
	        		case 2: i.putExtra("Tab", 2);
    						break;
	        		case 3: i.putExtra("Tab", 3);
							break;
	        		case 4: i = createIntent(HomeActivity.this, PreferencesActivity.class);
    						break;
	        	}
	        	
	        	startActivity(i);
	        }
	    });
	    
	    ImageView imview = (ImageView) findViewById(R.id.web_image);
	    imview.setImageResource(R.drawable.header);
	    
	    imview.setOnClickListener(new OnClickListener() {
	    	
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i= new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse("http://e-adventure.e-ucm.es/"));
				startActivity(i);
			}
	    });
	    
	    Intent i = this.getIntent();
		
		if (i.getData() != null){
			String data = this.getIntent().getData().getPath();
			installEadGame(data);
		} 
	    
	    overridePendingTransition(R.anim.fade, R.anim.hold);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
    protected void onStart() {
    	
    	super.onStart();
   		overridePendingTransition(R.anim.fade, R.anim.hold);
    } 
	
	/**
	 * If the back key is pressed, ends the application
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	this.finish();
	        return true;
	    }
	    else return false;
	}
	
	/**
	 * Static method for creating intents to start other activities
	 */
	public static Intent createIntent(Context context, Class<?> c) {
        Intent i = new Intent(context, c);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return i;
    }
	
	/**
	 * Install games in the proper folder
	 */
	private void installEadGame(String path_from) {
		
		this.path_from = path_from;		
		this.showDialog(DIALOG_INSTALL_ID);
		
		Thread t = new Thread(new Runnable() {
			public void run()
			{					
				String path_from = getPathFrom();
				int last = path_from.lastIndexOf("/");
				String gameFileName = path_from.substring(last + 1);
				path_from= path_from.substring(0, last+1);
				RepoResourceHandler.unzip(path_from,Paths.eaddirectory.GAMES_PATH,gameFileName,false);
				dismissDialog(DIALOG_INSTALL_ID);
			}
		});
		
		t.start();					
	}
	
	/**
     * Returns the path of the data in the Intent
     */
	private String getPathFrom() {
		return path_from;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_INSTALL_ID:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setCancelable(false);		
			progressDialog.setTitle("Please wait");
			progressDialog.setIcon(R.drawable.dialog_icon);
			progressDialog.setMessage("Installing game...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
			dialog = progressDialog;			
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
	
	/**
	 * An adapter for displaying images with a text below them on the grid
	 * 
	 * @author Roberto Tornero
	 */
	public class ImageAdapter extends BaseAdapter {
		
		/**
		 * The resources for the icons
		 */
	    private Integer[] mThumbIds = {R.drawable.kde_folder_games, R.drawable.diskette,
	            R.drawable.connect_to_network, R.drawable.preferences_desktop_gaming, R.drawable.settings1};	    
	    /**
	     * The text to display under each icon
	     */
	    private String[] mThumbStrings = {"Installed Games", "Saved Games", "Repository", "Demos", "Preferences"};

		/**
		 * Constructor
		 */
	    public ImageAdapter() {
	        super();
	    }

	    /*
	     * (non-Javadoc)
	     * @see android.widget.Adapter#getCount()
	     */
	    public int getCount() {
	        return mThumbIds.length;
	    }

	    /*
	     * (non-Javadoc)
	     * @see android.widget.Adapter#getItem(int)
	     */
	    public Object getItem(int position) {
	        return null;
	    }

	    /*
	     * (non-Javadoc)
	     * @see android.widget.Adapter#getItemId(int)
	     */
	    public long getItemId(int position) {
	        return position;
	    }

	    /**
	     * A grid view with the icons and their text
	     */
	    public View getView(int position, View convertView, ViewGroup parent) {
	        
	        View myView = convertView;
	        
	        //if (convertView == null){
	            
	        	//Inflate the layout
	        	LayoutInflater li = getLayoutInflater();
	        	myView = li.inflate(R.layout.home_grid_item, null);
	            
	        	// Add The Image          
	        	ImageView iv = (ImageView)myView.findViewById(R.id.grid_item_image);
	        	iv.setImageResource(mThumbIds[position]);
	            
	        	// Add The Text
	        	TextView tv = (TextView)myView.findViewById(R.id.grid_item_text);
	        	tv.setText(mThumbStrings[position]);
	        //}
	         
	        return myView;

	    }
	    
	}
}
