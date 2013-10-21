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

package es.eucm.ead.techdemo.elementfactories.scenes.scenes;

import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.animation.Frame;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.InterpolationEf;
import es.eucm.ead.model.elements.effects.enums.InterpolationLoopType;
import es.eucm.ead.model.elements.effects.enums.InterpolationType;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.TimedEv;
import es.eucm.ead.model.elements.events.enums.TimedEvType;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.ListOp;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.enums.ListOpType;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.model.params.variables.VarDef;

@SuppressWarnings("rawtypes")
public class MoleGame extends EmptyScene {

	private EAdDrawable holeImage = new Image("@drawable/hole.png");

	private FramesAnimation mole;

	private ChangeFieldEf dissapearMole;

	private EAdVarDef<SceneElement> moleVar = new VarDef<SceneElement>(
			"moleVar", SceneElement.class, null);

	private EAdVarDef<EAdList> listVar;

	private ElementField<EAdList> listField;

	private ElementField<SceneElement> moleField;

	public MoleGame() {
		setBackgroundFill(ColorFill.DARK_BROWN);
		moleField = new ElementField<SceneElement>(this, moleVar);

		dissapearMole = new ChangeFieldEf();
		dissapearMole.setParentVar(VAR_VISIBLE);
		dissapearMole.setOperation(EmptyCond.FALSE);

		mole = new FramesAnimation();
		mole.addFrame(new Frame("@drawable/mole1.png", 5000));
		mole.addFrame(new Frame("@drawable/mole2.png", 500));

		EAdList<SceneElement> list = new EAdList<SceneElement>();

		int row = 7;
		int col = row;
		int size = 60;
		float scale = 0.15f;
		for (int i = 0; i < col; i++)
			for (int j = 0; j < row; j++) {
				SceneElement mole = getMole(size * i, size * j);
				mole.setVarInitialValue(VAR_SCALE, scale);
				getSceneElements().add(mole);
				list.add(mole);
				SceneElement hole = getHole(size * i, size * j + 25);
				hole.setVarInitialValue(VAR_SCALE, scale);
				this.getSceneElements().add(hole);

			}
		listVar = new VarDef<EAdList>("moleListVar", EAdList.class, list);
		listField = new ElementField<EAdList>(this, listVar);
		initLoop();

	}

	private SceneElement getHole(int x, int y) {
		SceneElement hole = new SceneElement(holeImage);
		hole.setPosition(x, y);
		hole.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, (Effect) null);
		return hole;
	}

	private SceneElement getMole(int x, int y) {
		SceneElement mole = new SceneElement(this.mole);
		mole.setPosition(x, y + 30);

		// SceneElementEventImpl event = new SceneElementEventImpl();
		// EAdFieldImpl<Integer> yField = new EAdFieldImpl<Integer>(mole,
		// EAdBasicSceneElement.VAR_Y);
		// EAdInterpolationEffect interpolation = new
		// EAdInterpolationEffect(mole,
		// EAdBasicSceneElement.VAR_Y, new MathOperation("[0]", yField),
		// new MathOperation("[0] + 30", yField), 2000,
		// (int) Math.round(Math.random() * 1000), LoopType.REVERSE, -1,
		// InterpolationType.LINEAR);
		// event.addEffect(SceneElementEvent.ADDED_TO_SCENE, interpolation);

		int initTime = (int) Math.round(Math.random() * 5500);
		mole.setVarInitialValue(VAR_TIME_DISPLAYED, initTime);

		mole.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, dissapearMole);
		// mole.addEvent(event);

		return mole;

	}

	public void initLoop() {
		TimedEv event = new TimedEv();

		ChangeFieldEf effect = new ChangeFieldEf();
		effect.setOperation(new ListOp(listField, ListOpType.RANDOM_ELEMENT));
		effect.addField(moleField);

		InterpolationEf interpolation = new InterpolationEf(moleField, VAR_Y,
				new MathOp("0"), new MathOp("-30"), 500, 0,
				InterpolationLoopType.REVERSE, 2, InterpolationType.LINEAR);
		event.addEffect(TimedEvType.END_TIME, effect);
		event.addEffect(TimedEvType.END_TIME, interpolation);
		event.setTime(1100);
		this.getBackground().addEvent(event);

	}

	@Override
	public String getSceneDescription() {
		return "Mole game";
	}

	public String getDemoName() {
		return "Mole game";
	}

}
