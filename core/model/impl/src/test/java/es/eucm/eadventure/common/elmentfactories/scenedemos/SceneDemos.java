package es.eucm.eadventure.common.elmentfactories.scenedemos;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.model.elements.EAdScene;

/**
 * A class holding all scenes that can be tested
 *
 */

public class SceneDemos {
	
	private static SceneDemos instance;
	
	private List<Class<? extends EAdScene>> sceneDemos;
	
	private SceneDemos(){
		sceneDemos = new ArrayList<Class<? extends EAdScene>>();
		sceneDemos.add(EmptyScene.class);
		sceneDemos.add(BasicScene.class);
		sceneDemos.add(ShapeScene.class);
		sceneDemos.add(TextsScene.class);
		sceneDemos.add(CharacterScene.class);
	}
	
	public List<Class<? extends EAdScene>> getSceneDemos( ){
		return sceneDemos;
	}
	
	public static SceneDemos getInstance(){
		if ( instance == null )
			instance = new SceneDemos();
		return instance;
	}
	

}
