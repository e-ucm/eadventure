package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.variables.impl.operations.AssignOperation;
import es.eucm.eadventure.common.resources.assets.drawable.OrientedDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.StateDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.OrientedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.StateDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public class CharacterScene extends EmptyScene {

	private String standUris[] = new String[] { "@drawable/stand_up.png",
			"@drawable/red_stand_right.png", "@drawable/stand_down.png",
			"@drawable/red_stand_left.png" };
	
	private String talkDownUris[] = new String[]{
			"@drawable/stand_down.png", "@drawable/stand_down_talking.png"
	};
	
	private String talkRightUris[] = new String[]{
			"@drawable/red_stand_right.png", "@drawable/red_stand_right_talking.png"
	};
	
	private String talkLeftUris[]= new String[]{
			"@drawable/red_stand_left.png", "@drawable/red_stand_left_talking.png"
	};
	
	private String walkDownUris[] = new String[]{
			"@drawable/walking_down.png", "@drawable/walking_down_2.png"	
	};
	
	private String walkUpUris[] = new String[]{
			"@drawable/walking_up_1.png", "@drawable/walking_up_2.png"	
	};
	
	private String walkRightUris[] = new String[]{
			"@drawable/walking_right_1.png", "@drawable/walking_right_2.png"	
	};
	
	private String walkLeftUris[] = new String[]{
			"@drawable/walking_left_1.png", "@drawable/walking_left_2.png"	
	};
	
	private static final String STATE_STAND = "stand";
	private static final String STATE_TALK = "talk";
	private static final String STATE_WALK = "walk";

	public CharacterScene() {

		EAdBasicSceneElement element = EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(getStateDrawable(), 100, 300);
		
		element.stateVar().setInitialValue(STATE_STAND);

		element.setScale(3.0f);

		this.getSceneElements().add(element);

		EAdEffect goUpEffect = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getChangeVarValueEffect(element.orientationVar(),
						new AssignOperation("assign", Orientation.N));
		EAdBasicSceneElement goUpArrow = EAdElementsFactory
				.getInstance()
				.getSceneElementFactory()
				.createSceneElement(new ImageImpl("@drawable/arrow_up.png"),
						100, 10, goUpEffect);
		this.getSceneElements().add(goUpArrow);
		
		EAdEffect goDownEffect = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getChangeVarValueEffect(element.orientationVar(),
						new AssignOperation("assign", Orientation.S));
		EAdBasicSceneElement goDownArrow = EAdElementsFactory
				.getInstance()
				.getSceneElementFactory()
				.createSceneElement(new ImageImpl("@drawable/arrow_down.png"),
						100, 120, goDownEffect);
		this.getSceneElements().add(goDownArrow);
		
		EAdEffect goLeftEffect = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getChangeVarValueEffect(element.orientationVar(),
						new AssignOperation("assign", Orientation.W));
		EAdBasicSceneElement goLeftArrow = EAdElementsFactory
				.getInstance()
				.getSceneElementFactory()
				.createSceneElement(new ImageImpl("@drawable/arrow_left.png"),
						0, 60, goLeftEffect);
		this.getSceneElements().add(goLeftArrow);
		
		EAdEffect goRightEffect = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getChangeVarValueEffect(element.orientationVar(),
						new AssignOperation("assign", Orientation.E));
		EAdBasicSceneElement goRightArrow = EAdElementsFactory
				.getInstance()
				.getSceneElementFactory()
				.createSceneElement(new ImageImpl("@drawable/arrow_right.png"),
						200, 60, goRightEffect);
		this.getSceneElements().add(goRightArrow);
		
		// Change state buttons
		EAdEffect standEffect = EAdElementsFactory.getInstance().getEffectFactory().getChangeVarValueEffect(element.stateVar(), new AssignOperation("assignState", STATE_STAND));
		EAdBasicSceneElement stand = EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement("Stand", 300, 10, standEffect );
		this.getSceneElements().add(stand);
		
		EAdEffect talkEffect = EAdElementsFactory.getInstance().getEffectFactory().getChangeVarValueEffect(element.stateVar(), new AssignOperation("assignState", STATE_TALK));
		EAdBasicSceneElement talk = EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement("Talk", 300, 110, talkEffect );
		this.getSceneElements().add(talk);
		
		EAdEffect walkEffect = EAdElementsFactory.getInstance().getEffectFactory().getChangeVarValueEffect(element.stateVar(), new AssignOperation("assignState", STATE_WALK));
		EAdBasicSceneElement walk = EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement("Walk", 300, 210, walkEffect );
		this.getSceneElements().add(walk);
	}
	
	private OrientedDrawable getTalkDrawable(){
		OrientedDrawableImpl oriented = new OrientedDrawableImpl();
		oriented.setDrawable(Orientation.N, new ImageImpl("@drawable/stand_up.png"));
		
		FramesAnimation right = EAdElementsFactory.getInstance().getDrawableFactory().getFramesAnimation(talkRightUris, 500);
		oriented.setDrawable(Orientation.E, right);
		
		FramesAnimation left = EAdElementsFactory.getInstance().getDrawableFactory().getFramesAnimation(talkLeftUris, 500);
		oriented.setDrawable(Orientation.W, left);
		
		FramesAnimation down = EAdElementsFactory.getInstance().getDrawableFactory().getFramesAnimation(talkDownUris, 500);
		oriented.setDrawable(Orientation.S, down);
		
		return oriented;
	}
	
	private OrientedDrawable getWalkDrawable( ){
		OrientedDrawableImpl oriented = new OrientedDrawableImpl();
		
		FramesAnimation up = EAdElementsFactory.getInstance().getDrawableFactory().getFramesAnimation(walkUpUris, 500);
		oriented.setDrawable(Orientation.N, up);
		
		FramesAnimation right = EAdElementsFactory.getInstance().getDrawableFactory().getFramesAnimation(walkRightUris, 500);
		oriented.setDrawable(Orientation.E, right);
		
		FramesAnimation left = EAdElementsFactory.getInstance().getDrawableFactory().getFramesAnimation(walkLeftUris, 500);
		oriented.setDrawable(Orientation.W, left);
		
		FramesAnimation down = EAdElementsFactory.getInstance().getDrawableFactory().getFramesAnimation(walkDownUris, 500);
		oriented.setDrawable(Orientation.S, down);
		
		return oriented;
	}
	
	private StateDrawable getStateDrawable( ){
		OrientedDrawable stand = EAdElementsFactory.getInstance()
				.getDrawableFactory().getOrientedDrawable(standUris);
		
		
		StateDrawableImpl stateDrawable = new StateDrawableImpl( );
		stateDrawable.addDrawable(STATE_STAND, stand);
		stateDrawable.addDrawable(STATE_TALK, getTalkDrawable());
		stateDrawable.addDrawable(STATE_WALK, getWalkDrawable());
		
		return stateDrawable;
	}

}
