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

package es.eucm.ead.techdemo.elementfactories.scenes.normalguy;

import es.eucm.ead.model.interfaces.features.enums.Orientation;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.animation.Frame;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.model.assets.drawable.compounds.StateDrawable;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.enums.CommonStates;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.EAdSceneElementDef;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;

public class NgCommon {

	private static boolean init = false;

	private static EAdSceneElementDef mainCharacter;

	private static EAdField<EAdSceneElement> mainCharacterSceneElement;

	private static ChangeFieldEf lookNorth;
	private static ChangeFieldEf lookEast;
	private static ChangeFieldEf lookSouth;

	public static void init() {
		if (!init) {
			init = true;
			createMainCharacter();
			createEffects();
		}
	}

	private static void createEffects() {
		lookNorth = new ChangeFieldEf();
		lookNorth.addField(new BasicField<Orientation>(
				mainCharacterSceneElement, SceneElement.VAR_ORIENTATION));
		lookNorth.setOperation(new ValueOp(Orientation.N));

		lookEast = new ChangeFieldEf();
		lookEast.addField(new BasicField<Orientation>(
				mainCharacterSceneElement, SceneElement.VAR_ORIENTATION));
		lookEast.setOperation(new ValueOp(Orientation.E));

		lookSouth = new ChangeFieldEf();
		lookSouth.addField(new BasicField<Orientation>(
				mainCharacterSceneElement, SceneElement.VAR_ORIENTATION));
		lookSouth.setOperation(new ValueOp(Orientation.S));
	}

	private static void createMainCharacter() {

		int blink = 200;
		int notBlink = 2000;
		int walkTime = 500;
		int talkTime = 500;

		StateDrawable stateDrawables = new StateDrawable();
		mainCharacter = new SceneElementDef(stateDrawables);

		// Stand
		StateDrawable oriented = new StateDrawable();
		// South
		FramesAnimation frames = new FramesAnimation();
		Frame standSouth = new Frame("@drawable/man_stand_s_1.png", notBlink);
		frames.addFrame(standSouth);
		frames.addFrame(new Frame("@drawable/man_stand_s_2.png", blink));

		oriented.setDrawable(Orientation.S, frames);
		// North
		Image standNorth = new Image("@drawable/man_stand_n.png");
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

		stateDrawables.addDrawable(CommonStates.DEFAULT + "", oriented);

		// Walk
		oriented = new StateDrawable();
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

		stateDrawables.addDrawable(CommonStates.WALKING + "", oriented);

		// Talk
		oriented = new StateDrawable();
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

		stateDrawables.addDrawable(CommonStates.TALKING + "", oriented);

		mainCharacterSceneElement = new BasicField<EAdSceneElement>(
				mainCharacter, SceneElementDef.VAR_SCENE_ELEMENT);

	}

	public static EAdSceneElementDef getMainCharacter() {
		return mainCharacter;
	}

	public static EAdEffect getLookNorthEffect() {
		return lookNorth;
	}

	public static EAdEffect getLookEastEffect() {
		return lookEast;
	}

	public static EAdEffect getLookSouthEffect() {
		return lookSouth;
	}

}
