package es.eucm.eadventure.common.elementfactories.demos;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.elementfactories.demos.normalguy.NgMainScreen;
import es.eucm.eadventure.common.elementfactories.demos.scenes.CharacterScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.ComplexElementScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.DepthZScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.DragDropScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.DrawablesScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.EmptyScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.InitScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.InventoryScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.PhysicsScene2;
import es.eucm.eadventure.common.elementfactories.demos.scenes.PositionScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.ShapeScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.SharingEffectsScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.ShowQuestionScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.SpeakAndMoveScene;
import es.eucm.eadventure.common.elementfactories.demos.scenes.TrajectoriesScene;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterImpl;

public class TechDemoAdventure extends EAdAdventureModelImpl {
	
	private List<SceneDemo> sceneDemos;
	
	private EAdChapterImpl chapter;
	
	public TechDemoAdventure( ){
		chapter = new EAdChapterImpl();
		this.getChapters().add(chapter);
		
		
		sceneDemos = new ArrayList<SceneDemo>();
		sceneDemos = new ArrayList<SceneDemo>();
		sceneDemos.add(new InitScene());
		sceneDemos.add(new EmptyScene());
		sceneDemos.add(new ShapeScene());
//		sceneDemos.add(new TextsScene());
		sceneDemos.add(new CharacterScene());
		sceneDemos.add(new SpeakAndMoveScene());
		sceneDemos.add(new ComplexElementScene());
//		sceneDemos.add(new SoundScene());
		sceneDemos.add(new DrawablesScene());
//		sceneDemos.add(new MoleGame());
		sceneDemos.add(new ShowQuestionScene());
		sceneDemos.add(new TrajectoriesScene());
//		sceneDemos.add(new PhysicsScene());
		sceneDemos.add(new PhysicsScene2());
		sceneDemos.add(new DragDropScene());
		sceneDemos.add(new PositionScene());
		sceneDemos.add(new DepthZScene());
		sceneDemos.add(new SharingEffectsScene());
		sceneDemos.add(new InventoryScene());
//		sceneDemos.add(new VideoScene());
		sceneDemos.add(new NgMainScreen());
//		sceneDemos.add(new NgRoom1());
	}
	
	
	public List<SceneDemo> getScenes(){
		return sceneDemos;
	}
	
	public List<String> getSceneDemosDescriptions(){
		ArrayList<String> strings = new ArrayList<String>();
		for ( SceneDemo s: sceneDemos ){
			strings.add(s.toString());
		}
		return strings;
	}
	
	public void setInitialScene( EAdScene scene ){
		chapter.setInitialScene(scene);
	}
	
	private static TechDemoAdventure instance;
	
	public static TechDemoAdventure getInstance( ){
		if ( instance == null ){
			instance = new TechDemoAdventure();
		}
		return instance;
	}

}
