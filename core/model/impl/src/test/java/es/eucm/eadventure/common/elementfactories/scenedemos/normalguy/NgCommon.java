package es.eucm.eadventure.common.elementfactories.scenedemos.normalguy;

import es.eucm.eadventure.common.interfaces.features.Oriented.Orientation;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement.CommonStates;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.OrientedDrawableImpl;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.StateDrawableImpl;

public class NgCommon {

	private static boolean init = false;

	private static EAdSceneElementDef mainCharacter;
	
	private static EAdChangeFieldValueEffect lookNorth;

	public static void init() {
		if (!init) {
			init = true;
			createMainCharacter();
			createEffects();
		}
	}

	private static void createEffects() {
		 lookNorth = new EAdChangeFieldValueEffect("lookNorth");
		 lookNorth.setParentVar(EAdBasicSceneElement.VAR_ORIENTATION);
		 lookNorth.setOperation(new ValueOperation( Orientation.N ));		
	}

	private static void createMainCharacter() {

		int blink = 200;
		int notBlink = 2000;
		int walkTime = 200;
		int talkTime = 300;

		mainCharacter = new EAdSceneElementDefImpl("mainCharacter");

		StateDrawableImpl stateDrawables = new StateDrawableImpl();
		mainCharacter.getResources().addAsset(mainCharacter.getInitialBundle(),
				EAdBasicSceneElement.appearance, stateDrawables);
		// Stand
		OrientedDrawableImpl oriented = new OrientedDrawableImpl();
		// South
		FramesAnimation frames = new FramesAnimation();
		Frame standSouth = new Frame("@drawable/man_stand_s_1.png", notBlink);
		frames.addFrame(standSouth);
		frames.addFrame(new Frame("@drawable/man_stand_s_2.png", blink));

		oriented.setDrawable(Orientation.S, frames);
		// North
		ImageImpl standNorth = new ImageImpl("@drawable/man_stand_n.png");
		oriented.setDrawable(Orientation.N, standNorth);

		// East
		frames = new FramesAnimation();
		Frame standEast = new Frame("@drawable/man_stand_e_1.png", notBlink);
		frames.addFrame(standEast);
		frames.addFrame(new Frame("@drawable/man_stand_e_2.png", blink));

		oriented.setDrawable(Orientation.E, frames);

		// West
		frames = new FramesAnimation();
		Frame standWest = new Frame("@drawable/man_stand_w_1.png", notBlink);
		frames.addFrame(standWest);
		frames.addFrame(new Frame("@drawable/man_stand_w_2.png", blink));

		oriented.setDrawable(Orientation.W, frames);

		stateDrawables.addDrawable(CommonStates.EAD_STATE_DEFAULT + "",
				oriented);

		// Walk
		oriented = new OrientedDrawableImpl();
		// South
		frames = new FramesAnimation();
		frames.addFrame(new Frame("@drawable/man_walk_s_1.png", walkTime));
		frames.addFrame(new Frame("@drawable/man_walk_s_2.png", walkTime));

		oriented.setDrawable(Orientation.S, frames);
		// North
		frames = new FramesAnimation();
		frames.addFrame(new Frame("@drawable/man_walk_n_1.png", walkTime));
		frames.addFrame(new Frame("@drawable/man_walk_n_2.png", walkTime));

		oriented.setDrawable(Orientation.N, frames);
		// East
		frames = new FramesAnimation();
		frames.addFrame(new Frame("@drawable/man_walk_e_1.png", walkTime));
		frames.addFrame(new Frame("@drawable/man_walk_e_2.png", walkTime));

		oriented.setDrawable(Orientation.E, frames);
		// West
		frames = new FramesAnimation();
		frames.addFrame(new Frame("@drawable/man_walk_w_1.png", walkTime));
		frames.addFrame(new Frame("@drawable/man_walk_w_2.png", walkTime));

		oriented.setDrawable(Orientation.W, frames);
		
		stateDrawables.addDrawable(CommonStates.EAD_STATE_WALKING + "",
				oriented);
		
		// Talk
		oriented = new OrientedDrawableImpl();
		// South
		frames = new FramesAnimation();
		frames.addFrame(new Frame("@drawable/man_stand_s_1.png", talkTime));
		frames.addFrame(new Frame("@drawable/man_talk_s.png", talkTime));

		oriented.setDrawable(Orientation.S, frames);
		// North
		oriented.setDrawable(Orientation.N, standNorth);
		// East
		frames = new FramesAnimation();
		frames.addFrame(new Frame("@drawable/man_stand_e_1.png", talkTime));
		frames.addFrame(new Frame("@drawable/man_talk_e.png", talkTime));

		oriented.setDrawable(Orientation.E, frames);
		// West
		frames = new FramesAnimation();
		frames.addFrame(new Frame("@drawable/man_stand_w_1.png", talkTime));
		frames.addFrame(new Frame("@drawable/man_talk_w.png", talkTime));

		oriented.setDrawable(Orientation.W, frames);
		
		stateDrawables.addDrawable(CommonStates.EAD_STATE_TALKING + "",
				oriented);

		
	}

	public static EAdSceneElementDef getMainCharacter() {
		return mainCharacter;
	}
	
	public static EAdEffect getLookNorthEffect(){
		return lookNorth;
	}

}
