package es.eucm.eadventure.engine.home;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;
import es.eucm.eadventure.common.elementfactories.scenedemos.SceneDemos;
import es.eucm.eadventure.engine.EAdventureEngineActivity;
import es.eucm.eadventure.engine.R;
import es.eucm.eadventure.engine.home.local.DeletingGame;
import es.eucm.eadventure.engine.home.local.LocalGamesListAdapter;
import es.eucm.eadventure.engine.home.local.SearchGamesThread;
import es.eucm.eadventure.engine.home.preferences.PreferencesActivity;
import es.eucm.eadventure.engine.home.repository.connection.RepositoryServices;
import es.eucm.eadventure.engine.home.repository.database.GameInfo;
import es.eucm.eadventure.engine.home.repository.database.RepositoryDatabase;
import es.eucm.eadventure.engine.home.repository.handler.ProgressNotifier.ProgressMessage;
import es.eucm.eadventure.engine.home.repository.handler.RepoResourceHandler;
import es.eucm.eadventure.engine.home.saved.LoadGamesArray;
import es.eucm.eadventure.engine.home.saved.LoadGamesListAdapter;
import es.eucm.eadventure.engine.home.saved.SearchForSavedGames;
import es.eucm.eadventure.engine.home.utils.ActivityPipe;
import es.eucm.eadventure.engine.home.utils.ViewPagerIndicator;
import es.eucm.eadventure.engine.home.utils.directory.Paths;

/**
 * Displays different fragments using a ViewPager instance
 * 
 * @author Roberto Tornero
 */
public class WorkspaceActivity extends FragmentActivity {
	
	/**
	 * The number of items of the pager
	 */
    public static final int NUM_ITEMS = 4;
    /**
     * Id of the installed games fragment
     */
    public static final int GAMES = 0;
    /**
     * Id of the saved games fragment
     */
	public static final int LOAD_GAMES = 1;
	/**
     * Id of the games repository fragment
     */
	public static final int REPOSITORY = 2;
	/**
     * Id of the games demonstration fragment
     */
	public static final int DEMOS = 3;
	/**
	 * The static instance of the saved games fragment 
	 */
	protected static LoadGamesListFragment load_games;
	/**
	 * The static instance of the repository fragment 
	 */
	protected static RepositoryListFragment repository;
	/**
	 * Adapter for the {@link mPager} 
	 */
    private PagerAdapter mAdapter;
    /**
     * The pager to display the fragments
     */
    protected ViewPager mPager;
    /**
     * The indicator attached to the pager
     */
    private ViewPagerIndicator indicator;
    

    /**
     * Instantiation of the action bar, view pager and the other components of the activity 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
        
        overridePendingTransition(R.anim.fade, R.anim.hold);
        
        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this, HomeActivity.class), R.drawable.launcher_icon3));
        actionBar.setTitle("eAdventure");
        actionBar.addAction(new IntentAction(this, createIntent(this, PreferencesActivity.class), android.R.drawable.ic_menu_preferences));

        mAdapter = new PagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);        
   
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mPager.setOnPageChangeListener(indicator);
		Drawable prev = getResources().getDrawable(R.drawable.indicator_prev_arrow);
		Drawable next = getResources().getDrawable(R.drawable.indicator_next_arrow);
		indicator.setArrows(prev, next);
		
		Intent i = this.getIntent();
		
		int current = i.getExtras().getInt("Tab");
		if (current == 1){
			mPager.setCurrentItem(1);
			indicator.init(1, NUM_ITEMS, mAdapter);
			}
		else if (current == 2){
			mPager.setCurrentItem(2);
			indicator.init(2, NUM_ITEMS, mAdapter);
		}		
		else if (current == 3){
			mPager.setCurrentItem(3);
			indicator.init(3, NUM_ITEMS, mAdapter);
		}
		else indicator.init(0, NUM_ITEMS, mAdapter);
    }
    
    /**
     * Register every new intent 
     */
    @Override
    protected void onNewIntent(Intent i) {
    	
    	setIntent(i);	
    }
    
    /**
     * When the activity is brought to the front, select the right fragment of the pager 
     */
    @Override
    protected void onStart() {
    	
    	super.onStart();
    	
    	Intent i = this.getIntent();
		
		int current = i.getExtras().getInt("Tab");
		if (current == 1){
			mPager.setCurrentItem(1);
			indicator.init(1, NUM_ITEMS, mAdapter);
			}
		else if (current == 2){
			mPager.setCurrentItem(2);
			indicator.init(2, NUM_ITEMS, mAdapter);
		}
		else if (current == 3){
			mPager.setCurrentItem(3);
			indicator.init(3, NUM_ITEMS, mAdapter);
		}
		else indicator.init(0, NUM_ITEMS, mAdapter);
		
		overridePendingTransition(R.anim.fade, R.anim.hold);
    }    
    
    /**
	 * Static method for creating intents to start other activities
	 */
    public static Intent createIntent(Context context, Class<?> c) {
        Intent i = new Intent(context, c);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return i;
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	public void onDestroy(){
	
		super.onDestroy();
		System.gc();
	}
	
	/**
	 * Adapter for managing the fragments in the [@link mPager}. It also implements PageInfoProvider
	 * to control the positioning for the indicator 
	 * 
	 * @author Roberto Tornero
	 */
    public static class PagerAdapter extends FragmentPagerAdapter implements ViewPagerIndicator.PageInfoProvider {
    	
    	/**
    	 * Constructor
    	 */
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /*
         * (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        /**
         * Returns the current fragment
         */
        @Override
        public Fragment getItem(int position) {
        	Fragment f = null;
            switch (position){
            	case GAMES: f = LocalGamesListFragment.newInstance();
            			break;
            	case LOAD_GAMES: load_games = LoadGamesListFragment.newInstance();
            			return load_games;  
            	case REPOSITORY: repository = RepositoryListFragment.newInstance();
    					return repository;
            	case DEMOS: f = DemoSelectionFragment.newInstance();
            			break;
            }
            return f;            
        }

        /**
         * Returns the current title for the indicator 
         */
		public String getTitle(int position) {
			String title = null;
			switch (position){
        	case GAMES: title = "Installed games";
        			break;
        	case LOAD_GAMES: title = "Saved games";
					break;
        	case REPOSITORY: title = "Games repository";
					break;
        	case DEMOS: title = "Demo selection";
					break;
			}
			return title;
		}

    }
    
    /**
     * A ListFragment to display the games that are installed
     * 
     * @author Roberto Tornero
     */
    public static class LocalGamesListFragment extends ListFragment {
    	
    	/**
    	 * The list of games installed
    	 */
        private ArrayList<GameInfo> m_games;
        /**
         * The adapter for the list of the ListFragment
         */
        private LocalGamesListAdapter m_adapter;
        /**
         * A dialog to show the installation progress 
         */
        private ProgressDialog dialog;
        /**
         * A list with the names of the installed games
         */
        private String[] advList = null;

        /**
         * Local games fragment handler messages . Handled by
    	 * {@link LGActivityHandler} Defines the messages handled by this Fragment
         * 
         * @author Roberto Tornero
         */
    	public class LGAHandlerMessages {

    		public static final int GAMES_FOUND = 0;
    		public static final int NO_GAMES_FOUND = 1;
    		public static final int NO_SD_CARD = 2;
    		public static final int DELETING_GAME = 3;
    	}

    	/**
    	 * Local games activity Handler
    	 */
    	private Handler LGActivityHandler = new Handler() {
    		
    		
    		/**    
    		 * Called when a message is sent to Engines Handler Queue 
    		 **/
    		@Override
    		public void handleMessage(Message msg) {

    			switch (msg.what) {

    			case LGAHandlerMessages.GAMES_FOUND: {
    				Bundle b = msg.getData();
    				advList = b.getStringArray("adventuresList");
    				insertAdventuresToList(advList);
    				break;
    			}
    			case LGAHandlerMessages.NO_GAMES_FOUND:
    				break;
    			case LGAHandlerMessages.NO_SD_CARD:
    				showAlert("No SD card mounted");
    				break;
    			case LGAHandlerMessages.DELETING_GAME:
    				dialog.setIndeterminate(false);
    				dialog.dismiss();
    				searchForGames();
    				break;
    			}
    		}

    	};

        /**
         * Create a new instance 
         */
        static LocalGamesListFragment newInstance() {
        	LocalGamesListFragment f = new LocalGamesListFragment();
            return f;
        }

        /*
         * (non-Javadoc)
         * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        /*
         * (non-Javadoc)
         * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.local_games, container, false);
            return v;
        }

        /**
         * Initialize the components and search for existing games
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            registerForContextMenu(this.getListView());
            m_games = new ArrayList<GameInfo>();
            m_adapter = new LocalGamesListAdapter(getActivity(),
    				R.layout.local_games_listitem, m_games);
            setListAdapter(m_adapter);
            searchForGames();            
        }

        /**
         * If an item of the list is selected, load the game it represents
         */
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            
            GameInfo selectedAdventure = (GameInfo) this.getListView()
			.getItemAtPosition(position);
            Intent i = new Intent(getActivity(), EAdventureEngineActivity.class);
            i.putExtra("AdventureName", selectedAdventure.getGameTitle());
            this.
            getActivity().startActivity(i);
        }
        
        /*
         * (non-Javadoc)
         * @see android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
         */
        @Override
    	public void onCreateContextMenu(ContextMenu menu, View v,
    			ContextMenuInfo menuInfo) {

    		menu.setHeaderTitle("Options");
    		menu.setHeaderIcon(R.drawable.dialog_icon);
    		menu.add(0, 0, 0, "Play Game");
    		menu.add(0, 1, 0, "Uninstall Game");
    	}

        /**
         * Both playing and deleting games are available on the context menu.
         */
    	@Override
    	public boolean onContextItemSelected(MenuItem item) {
    		
    		AdapterContextMenuInfo information = (AdapterContextMenuInfo) item
    				.getMenuInfo();    		

    		switch (item.getItemId()) {

    		case 0:

    			GameInfo selectedAdventure = (GameInfo) this.getListView()
    					.getItemAtPosition(information.position);

    			Intent i = new Intent(getActivity(), EAdventureEngineActivity.class);
    			i.putExtra("AdventureName", selectedAdventure.getGameTitle());

    			getActivity().startActivity(i);
    			break;

    		case 1:
    			String[] paths = new String[2];
    			paths[0] = Paths.eaddirectory.GAMES_PATH
    					+ m_games.get(information.position).getGameTitle() + "/";
    			paths[1] = Paths.eaddirectory.SAVED_GAMES_PATH
    					+ m_games.get(information.position).getGameTitle() + "/";
    			DeletingGame instance = new DeletingGame(LGActivityHandler, paths);
    			instance.start();
    			
    			dialog = new ProgressDialog(getActivity());
    			dialog.setTitle("eAdventure");
    			dialog.setIcon(R.drawable.dialog_icon);
    			dialog.setMessage("Removing game...");
    			dialog.setIndeterminate(true);
    			dialog.show();

    			break;
    		}

    		return true;

    	}
    	
    	/**
    	 * Load the {@link m_games} list with the games which names are included in {@link advList} 
    	 */
    	private void insertAdventuresToList(String[] advList) {
    		
    		m_games.clear();
    		
    		for (int i = 0; i < advList.length; i++)
    			m_games.add(new GameInfo(advList[i], "", "",BitmapFactory.decodeFile(Paths.eaddirectory.GAMES_PATH+advList[i]+"/icon.png")));

    		m_adapter.notifyDataSetChanged();

    	}

    	/** 
    	 * Starts a new Thread that searches for eAd games 
    	 */
    	private void searchForGames() {

    		m_games.clear();
    		SearchGamesThread t = new SearchGamesThread(LGActivityHandler);
    		t.start();

    	}
    	
    	/**
    	 * Creates alert dialogs 
    	 */
    	private void showAlert(String msg) {

    		new AlertDialog.Builder(this.getActivity()).setMessage(msg).setNeutralButton("OK",
    				null).setIcon(R.drawable.dialog_icon).setTitle("External Storage").show();

    	}
        
    }
    
    /**
     * A ListFragment to display the saved games
     * 
     * @author Roberto Tornero
     */
    public static class LoadGamesListFragment extends ListFragment {
    	
    	/**
         * The adapter for the list of the ListFragment
         */
        private LoadGamesListAdapter mAdapter;
        /**
         * The list of saved games and their information
         */
    	private LoadGamesArray info = null;
    	
    	/**
    	 * Load games fragment handler messages . Handled by
    	 * {@link ActivityHandler} Defines the messages handled by this Fragment
    	 * 
    	 * @author Roberto Tornero
    	 */
    	public class SavedGamesHandlerMessages {

    		public static final int GAMES = 0;
    		public static final int NOGAMES = 1;

    	}

    	/**
    	 * A handler to control if there are saved games or not 
    	 */
    	public Handler ActivityHandler = new Handler() {
    		
    		/**
    		 * 
    		 */
    		@Override
    		public void handleMessage(Message msg) {

    			switch (msg.what) {
    			case SavedGamesHandlerMessages.GAMES:
    				Bundle b = msg.getData();
    				String text = b.getString("loadingsavedgames");
    				info = (LoadGamesArray) ActivityPipe.remove(text);
    				createlist();
    				break;

    			case SavedGamesHandlerMessages.NOGAMES:
    				nogames();
    				break;
    			}
    		}
    	};


        /**
         * Create a new instance
         */
        static LoadGamesListFragment newInstance() {
        	LoadGamesListFragment f = new LoadGamesListFragment();
            return f;
        }

        /*
         * (non-Javadoc)
         * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        
        /*
         * (non-Javadoc)
         * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.saved_games, container, false);
            return v;
        }

        /*
         * (non-Javadoc)
         * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState); 
        }
    	
        /**
         * Create the list adapter with the data of {@link info}
         */
    	private void createlist() {

    		mAdapter = new LoadGamesListAdapter(this.getActivity(), info.getSavedGames());
    		setListAdapter(mAdapter);
    		registerForContextMenu(this.getListView());

    	}

    	/**
    	 * If there are not saved games, clear the list adapter
    	 */
    	private void nogames() {
    		setListAdapter(null);
    	}

    	/**
    	 * If a saved game is selected from the list, load that game 
    	 */
    	@Override
    	public void onListItemClick(ListView l, View v, int position, long id) {
    		
    		if ((!info.getSavedGames().get(position).getGame().equals("No Games"))
    				&& (!info.getSavedGames().get(position).getSaved().equals("Saved but deleted"))) {
    			Intent i = new Intent(this.getActivity(), EAdventureEngineActivity.class);
    			i.putExtra("AdventureName", info.getSavedGames().get(position).getGame());
    			i.putExtra("restoredGame", Paths.eaddirectory.SAVED_GAMES_PATH
    					+ info.getSavedGames().get(position).getGame() + "/"
    					+ info.getSavedGames().get(position).getSaved());
    			i.putExtra("savedgame", true);
    			this.startActivity(i);
    		
    		}

    	}

    	/**
    	 * The context menu offers both loading and deleting saved games
    	 */
    	@Override
    	public void onCreateContextMenu(ContextMenu menu, View v,
    			ContextMenuInfo menuInfo) {
    		   		
    		menu.setHeaderTitle("Options");
    		menu.setHeaderIcon(R.drawable.dialog_icon);
    		menu.add(0, 0, 0, "Load");
    		menu.add(0, 1, 0, "Delete");
    	}

    	/**
    	 * Implementation of the loading and deleting options from the context menu
    	 */
    	@Override
    	public boolean onContextItemSelected(MenuItem item) {
    		
    		
    		int position = item.getItemId();

    		switch (item.getItemId()) {
    			case 0:
    				if ((!info.getSavedGames().get(position).getGame().equals("No Games"))
    	    				&& (!info.getSavedGames().get(position).getSaved().equals("Saved but deleted"))) {
    					Intent i = new Intent(this.getActivity(), EAdventureEngineActivity.class);
    	    			i.putExtra("AdventureName", info.getSavedGames().get(position).getGame());
    	    			i.putExtra("restoredGame", Paths.eaddirectory.SAVED_GAMES_PATH
    	    					+ info.getSavedGames().get(position).getGame() + "/"
    	    					+ info.getSavedGames().get(position).getSaved());
    	    			i.putExtra("savedgame", true);
    	    			this.startActivity(i);
    				}
    				break;
    			case 1:
    				RepoResourceHandler
    						.deleteFile(Paths.eaddirectory.SAVED_GAMES_PATH
    								+ info.getSavedGames().get(position).getGame() + "/"
    								+ info.getSavedGames().get(position).getSaved());
    				RepoResourceHandler
    						.deleteFile(Paths.eaddirectory.SAVED_GAMES_PATH
    								+ info.getSavedGames().get(position).getGame() + "/"
    								+ info.getSavedGames().get(position).getSaved()
    								+ ".png");

    				Toast.makeText(this.getActivity(), "Saved game suscesfully removed",
    						Toast.LENGTH_SHORT).show();

    				refresh();
    				break;
    			}
    		
    		return true;
    	}

    	/**
    	 * Refresh the information of the saved games list
    	 */
    	private void refresh() {
    		
    		SearchForSavedGames gettingdata = new SearchForSavedGames (ActivityHandler);
    		gettingdata.start();
    	}
    	
    	/**
    	 * When resuming the Activity, refresh the info on the list
    	 */
    	@Override
		public void onResume(){
    		super.onResume();
    		refresh();
    	}
    	
    }

    /**
     * A ListFragment to display the games repository
     * 
     * @author Roberto Tornero
     */
    public static class RepositoryListFragment extends ListFragment {
    	
    	/**
    	 * Updating value for the dialog
    	 */
    	static final int DIALOG_UPDATING_REPO_ID = 0;
    	/**
    	 * Error value for the dialog
    	 */
    	static final int DIALOG_ERROR_ID = 1;
    	/**
    	 * The games repository database, retrieved from the server
    	 */
    	private RepositoryDatabase db;
    	/**
    	 * The games repository services to download new games
    	 */
    	private RepositoryServices rs;
    	/**
    	 * A dialog to show the progress of updating information
    	 */
    	private ProgressDialog pd;
    	/**
    	 * The adapter for the repository list
    	 */
    	private LocalGamesListAdapter m_adapter;
    	/**
    	 * Allows flipping between the two views: the repository list and the detailed view of each game 
    	 */
    	private ViewFlipper mFlipper;
    	/**
    	 * Selected game information
    	 */
    	private GameInfo selectedGame = null;
    	/**
    	 * A dialog to show the progress of downloading and installing games from the repository
    	 */
    	private ProgressDialog progressDialog;
    	/**
    	 * The handler to control the progress of installing and downloading games 
    	 */
    	private Handler RAHandler = new Handler() {
    		
    		/*
    		 * (non-Javadoc)
    		 * @see android.os.Handler#handleMessage(android.os.Message)
    		 */
    		@Override
    		public void handleMessage(Message msg) {

    			String m = null;
    			int perc;
    			
    			ProgressDialog p = null;
    			
    			if (pd.isShowing())
    				p = pd;
    			else if (progressDialog.isShowing())
    				p = progressDialog;
    			
    			if (p!=null) {
    				 	 
    			switch (msg.what) {

    			case ProgressMessage.PROGRESS_PERCENTAGE:

    				p.setIndeterminate(false);
    				p.show();
    				m = msg.getData().getString("msg");
    				perc = msg.getData().getInt("ptg");
    				p.setProgress(perc);
    				p.setMessage(m);
    				break;

    			case ProgressMessage.PROGRESS_UPDATE_FINISHED:

    				p.setIndeterminate(false);
    				p.setProgress(100);
    				databaseUpdated();
    				p.dismiss();
    				break;

    			case ProgressMessage.PROGRESS_ERROR:

    				m = msg.getData().getString("msg");
    				p.setProgress(0);
    				p.setMessage(m);
    				p.dismiss();
    				break;

    			case ProgressMessage.INDETERMINATE:

    				p.setIndeterminate(true);
    				m = msg.getData().getString("msg");
    				p.setMessage(m);


    				break;
    				
    			case ProgressMessage.GAME_INSTALLED:

    				p.setIndeterminate(false);
    				p.dismiss();
    				
    				goToLocalGames();
    			
    				break;    				
    			}    			
    			}
    		}
    	};
    	
    	/**
         * Create a new instance
         */
        static RepositoryListFragment newInstance() {
        	RepositoryListFragment f = new RepositoryListFragment();
            return f;
        }

        /*
         * (non-Javadoc)
         * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.repository, container, false);
            return v;
        }
        
        /**
         * Return to the LocalGamesListFragment instance of the ViewPager
         */
    	private void goToLocalGames() {
    		
    		((WorkspaceActivity)this.getActivity()).mPager.setCurrentItem(0);
    		
    	}

    	/**
    	 * New instances when the activity is created
    	 */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            
            db = new RepositoryDatabase();
    		rs = new RepositoryServices();
    		
    		rs.updateDatabase(this.getActivity(), RAHandler, db);
            
        }
        
        /*
         * (non-Javadoc)
         * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
         */
        @Override
		public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		
    		pd = new ProgressDialog(this.getActivity());
    		pd.setTitle("eAdventure Repository");
    		pd.setIcon(R.drawable.dialog_icon);
    		pd.setMessage("Retrieving data...");
    		pd.setCancelable(false);
    		pd.show();
    		
    		progressDialog = new ProgressDialog(this.getActivity());
    		progressDialog.setTitle("eAdventure Repository");
    		progressDialog.setIcon(R.drawable.dialog_icon);
    		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		progressDialog.setCancelable(false);

    		super.setHasOptionsMenu(true);
    		this.setRetainInstance(true);
        }
    	
        /**
         * Setting the layout if the database info is updated
         */
    	private void databaseUpdated() {
    	
    		setLayout();    		
    		m_adapter.notifyDataSetChanged();

    	}
    	
    	/**
    	 * Get the layouts for all the views 
    	 */
    	private void setLayout() {

    		mFlipper = ((ViewFlipper) this.getActivity()
    				.findViewById(R.id.repository_activity_flipper));
    		mFlipper.setInAnimation(AnimationUtils.loadAnimation(this.getActivity(),
    				R.anim.zoom_enter));
    		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this.getActivity(),
    				R.anim.zoom_exit));
    		
    		m_adapter = new LocalGamesListAdapter(this.getActivity(),
    				R.layout.repository_listitem, db.getRepoData());

    		setListAdapter(m_adapter);

    		AnimationSet set = new AnimationSet(true);

    		Animation animation = new AlphaAnimation(0.0f, 1.0f);
    		animation.setDuration(1500);
    		set.addAnimation(animation);
    		
    		Animation animation2 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
    		Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
    		-1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
    		
    		animation2.setDuration(500);
    		set.addAnimation(animation2);

    		LayoutAnimationController controller = new LayoutAnimationController(
    				set, 0.5f);

    		getListView().setLayoutAnimation(controller);
    		getListView().setTextFilterEnabled(true);
    		
    		Button button = (Button) this.getActivity().findViewById(R.id.detailed_game_download_button);
    		
    		button.setOnClickListener(new OnClickListener(){

    			public void onClick(View arg0) {
    				downloadGame();
    			}
    			
    		});
    	}
    	
    	/**
    	 * Use the repository services to download a game
    	 */
    	private void downloadGame() {
    		

    		progressDialog.setTitle("Please wait");
    		progressDialog.setMessage("Starting download");
    		progressDialog.show();
    		
    		RepositoryServices rs = new RepositoryServices() ;
    		rs.downloadGame(this.getActivity(), RAHandler, selectedGame);
    	}

    	/**
    	 * If a game from the repository is selected, show the next view of the ViewFlipper to allow downloading
    	 */
    	@Override
		public void onListItemClick(ListView l, View v, int position, long id) {

    		GameInfo selectedGame = (GameInfo) this.getListView()
    				.getItemAtPosition(position);
    		
    		TextView title = (TextView)this.getActivity().findViewById(R.id.detailed_game_title);
    		TextView description = (TextView)this.getActivity().findViewById(R.id.detailed_game_description);
    		ImageView image = (ImageView)this.getActivity().findViewById(R.id.detailed_game_image_icon);
    		
    		title.setText(selectedGame.getGameTitle());
    		description.setText(selectedGame.getGameDescription());
    		if (selectedGame.getImageIcon()!=null)		
    			image.setImageBitmap(selectedGame.getImageIcon());
    		else image.setImageDrawable(this.getResources().getDrawable(R.drawable.icon));
    				
    		mFlipper.showNext();
    		
    		this.selectedGame = selectedGame;

    	}
    	
    	/**
    	 * If the back key is pressed show the previous view shown by the ViewFlipper
    	 */
    	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	    if (keyCode == KeyEvent.KEYCODE_BACK) {
    	    	mFlipper.showPrevious();
    	        return true;
    	    }
    	    else return false;
    	}
    	
    	/**
    	 * The options menu 
    	 */
    	public boolean onCreateOptionsMenu(Menu menu) {

    		MenuInflater inflater = this.getActivity().getMenuInflater();
    		inflater.inflate(R.layout.menu, menu);

    		return true;
    	}

    	/**
    	 * An option to update the database information
    	 */
    	@Override
    	public boolean onOptionsItemSelected(MenuItem item) {

    		db.clear();
    		pd = ProgressDialog.show(this.getActivity(), "Please wait...", "Retrieving data ...",
    				true);
    		rs.updateDatabase(this.getActivity(), RAHandler, db);
    		
    		return true;
    	}
    	
    	/**
    	 * Returns the ViewFlipper instance
    	 */
    	public ViewFlipper getFlipper(){
    		return mFlipper;
    	}
    	
    }
    
    /**
     * A ListFragment to display available demonstrations
     * 
     * @author Roberto Tornero
     */
    public static class DemoSelectionFragment extends ListFragment {
    	
    	/**
         * Create a new instance
         */
        static DemoSelectionFragment newInstance() {
        	DemoSelectionFragment f = new DemoSelectionFragment();
            return f;
        }
        
        /*
         * (non-Javadoc)
         * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
         */
        @Override
		public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);  

        }

        /*
         * (non-Javadoc)
         * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.demo_selection, container, false);
            return v;
        }
        
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            
        	super.onActivityCreated(savedInstanceState);            
            setListAdapter(new ArrayAdapter<String>( this.getActivity(), R.layout.demo_selection_listitem, SceneDemos.getInstance().getSceneDemosDescriptions() ));
            
        }
    	
    	/**
    	 * Loads the clicked demonstration of the list
    	 */
    	@Override
		public void onListItemClick(ListView l, View v, int position, long id) {

    		Intent i = new Intent(this.getActivity(), EAdventureEngineActivity.class);
	    	i.putExtra("demo", SceneDemos.getInstance().getScenes().get(position).getClass());
	    	this.getActivity().startActivity(i);
    	}
    	
    	
    }
    
    
    
}
