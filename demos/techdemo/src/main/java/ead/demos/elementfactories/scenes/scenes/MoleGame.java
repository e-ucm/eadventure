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

package ead.demos.elementfactories.scenes.scenes;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.drawable.basics.animation.Frame;
import ead.common.model.assets.drawable.basics.animation.FramesAnimation;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.TimedEv;
import ead.common.model.elements.events.enums.TimedEvType;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.BooleanOp;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.ListOp;
import ead.common.model.elements.operations.MathOp;
import ead.common.model.elements.operations.enums.ListOpType;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.variables.EAdVarDef;
import ead.common.model.params.variables.VarDef;

@SuppressWarnings("rawtypes")
public class MoleGame extends EmptyScene {

	private EAdDrawable holeImage = new Image("@drawable/hole.png");

	private FramesAnimation mole;

	private ChangeFieldEf dissapearMole;

	private EAdVarDef<EAdSceneElement> moleVar = new VarDef<EAdSceneElement>(
			"moleVar", EAdSceneElement.class, null);

	private EAdVarDef<EAdList> listVar;

	private EAdField<EAdList> listField;

	private EAdField<EAdSceneElement> moleField;

	public MoleGame() {
		setBackgroundFill(ColorFill.DARK_BROWN);
		moleField = new BasicField<EAdSceneElement>(this, moleVar);

		dissapearMole = new ChangeFieldEf();
		dissapearMole.setParentVar(SceneElement.VAR_VISIBLE);
		dissapearMole.setOperation(BooleanOp.FALSE_OP);

		mole = new FramesAnimation();
		mole.addFrame(new Frame("@drawable/mole1.png", 5000));
		mole.addFrame(new Frame("@drawable/mole2.png", 500));

		EAdList<EAdSceneElement> list = new EAdList<EAdSceneElement>();

		int row = 7;
		int col = row;
		int size = 60;
		float scale = 0.15f;
		for (int i = 0; i < col; i++)
			for (int j = 0; j < row; j++) {
				EAdSceneElement mole = getMole(size * i, size * j);
				mole.setVarInitialValue(SceneElement.VAR_SCALE, scale);
				getSceneElements().add(mole);
				list.add(mole);
				EAdSceneElement hole = getHole(size * i, size * j + 25);
				hole.setVarInitialValue(SceneElement.VAR_SCALE, scale);
				this.getSceneElements().add(hole);

			}
		listVar = new VarDef<EAdList>("moleListVar", EAdList.class, list);
		listField = new BasicField<EAdList>(this, listVar);
		initLoop();

	}

	private EAdSceneElement getHole(int x, int y) {
		SceneElement hole = new SceneElement(holeImage);
		hole.setPosition(x, y);
		hole.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, (EAdEffect) null);
		return hole;
	}

	private EAdSceneElement getMole(int x, int y) {
		SceneElement mole = new SceneElement(this.mole);
		mole.setPosition(x, y + 30);

		// EAdSceneElementEventImpl event = new EAdSceneElementEventImpl();
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
		mole.setVarInitialValue(SceneElement.VAR_TIME_DISPLAYED, initTime);

		mole.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, dissapearMole);
		// mole.getEvents().add(event);

		return mole;

	}

	public void initLoop() {
		TimedEv event = new TimedEv();

		ChangeFieldEf effect = new ChangeFieldEf();
		effect.setOperation(new ListOp(listField, ListOpType.RANDOM_ELEMENT));
		effect.addField(moleField);

		InterpolationEf interpolation = new InterpolationEf(moleField,
				SceneElement.VAR_Y, new MathOp("0"), new MathOp("-30"), 500, 0,
				InterpolationLoopType.REVERSE, 2, InterpolationType.LINEAR);
		event.addEffect(TimedEvType.END_TIME, effect);
		event.addEffect(TimedEvType.END_TIME, interpolation);
		event.setTime(1100);
		this.getBackground().getEvents().add(event);

	}

	@Override
	public String getSceneDescription() {
		return "Mole game";
	}

	public String getDemoName() {
		return "Mole game";
	}

}
