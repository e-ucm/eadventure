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

import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.BasicField;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.predef.effects.MakeActiveElementEf;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.util.Position;
import es.eucm.ead.model.params.util.Position.Corner;

public class SharingEffectsScene extends EmptyScene {

	public SharingEffectsScene() {
		this.setId("SharingEffectsScene");
		SceneElement b = new SceneElement(new RectangleShape(50, 50,
				ColorFill.RED));

		EAdField<Float> field = new BasicField<Float>(
				SystemFields.ACTIVE_ELEMENT, SceneElement.VAR_ROTATION);
		ChangeFieldEf effect = new ChangeFieldEf(field, new MathOp("[0] + 0.1",
				field));
		b.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, effect);

		ChangeFieldEf changeAlpha1 = new ChangeFieldEf();
		changeAlpha1.setParentVar(SceneElement.VAR_ALPHA);
		changeAlpha1.setOperation(new ValueOp(new Float(0.5f)));

		ChangeFieldEf changeAlpha2 = new ChangeFieldEf();
		changeAlpha2.setParentVar(SceneElement.VAR_ALPHA);
		changeAlpha2.setOperation(new ValueOp(new Float(1.0f)));

		SceneElementEv event = new SceneElementEv();
		//		EAdVarInterpolationEffect rotate = new EAdVarInterpolationEffect( "rotate", )

		event.addEffect(SceneElementEvType.INIT, effect);

		b.setPosition(20, 20);
		this.getSceneElements().add(b);

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				SceneElement e = new SceneElement(new RectangleShape(30, 30,
						ColorFill.BLUE));
				e.setPosition(new Position(Corner.CENTER, i * 60 + 40,
						j * 60 + 100));
				MakeActiveElementEf ef = new MakeActiveElementEf(e);
				e.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, ef);
				e.addBehavior(MouseGEv.MOUSE_ENTERED, changeAlpha1);
				e.addBehavior(MouseGEv.MOUSE_EXITED, changeAlpha2);
				getSceneElements().add(e);
			}

	}

	@Override
	public String getSceneDescription() {
		return "A scene in which elements share effects";
	}

	public String getDemoName() {
		return "Sharing Effects Scene";
	}
}
