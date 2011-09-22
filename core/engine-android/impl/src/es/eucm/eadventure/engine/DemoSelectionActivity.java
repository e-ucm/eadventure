package es.eucm.eadventure.engine;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import es.eucm.eadventure.common.elmentfactories.scenedemos.SceneDemos;

public class DemoSelectionActivity extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>( this, R.layout.demo_selection_listitem, SceneDemos.getInstance().getSceneDemosDescriptions() ));
		
		ListView lv = this.getListView();
		
		  lv.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			    	
			    	Intent i = new Intent( DemoSelectionActivity.this, EAdventureEngineActivity.class );
			    	i.putExtra("demo", SceneDemos.getInstance().getSceneDemos().get(position));
			    	DemoSelectionActivity.this.startActivity(i);

			    }
			  });
		
	}

}
