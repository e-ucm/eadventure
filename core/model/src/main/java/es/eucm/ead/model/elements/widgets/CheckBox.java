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

package es.eucm.ead.model.elements.widgets;

import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.elements.conditions.Condition;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.ConditionedEv;
import es.eucm.ead.model.elements.events.enums.ConditionedEvType;
import es.eucm.ead.model.elements.operations.ElementField;
import es.eucm.ead.model.elements.operations.ValueOp;
import es.eucm.ead.model.elements.scenes.GroupElement;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.util.Position;

public class CheckBox extends GroupElement {

	public static final String CHECKED_BUNDLE = "checked_bundle";

	public static final String CHECKED = "checkbox_checked";

	public static final String CHECKED_INT = "checkbox_checked_int";

	public CheckBox() {

	}

	public CheckBox(boolean checked, String stringId, EAdFont font) {
		setVar(CHECKED, checked);
		setVar(CHECKED_INT, checked ? 1 : 0);
		setVar(SceneElement.VAR_BUNDLE_ID, (checked ? CHECKED_BUNDLE
				: SceneElementDef.INITIAL_BUNDLE));
		setAppearance(new Image("@drawable/checkboxoff.png"));
		setAppearance(CHECKED_BUNDLE, new Image("@drawable/checkboxon.png"));

		ConditionedEv event = new ConditionedEv();
		ElementField checkedField = new ElementField(this, CHECKED);
		ElementField bundleField = new ElementField(this,
				SceneElement.VAR_BUNDLE_ID);

		Condition c = new OperationCond(checkedField);
		event.setCondition(c);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, new ChangeFieldEf(
				bundleField, new ValueOp(CHECKED_BUNDLE)));
		event.addEffect(ConditionedEvType.CONDITIONS_UNMET, new ChangeFieldEf(
				bundleField, new ValueOp(SceneElementDef.INITIAL_BUNDLE)));

		ElementField checkedFieldInt = new ElementField(this, CHECKED_INT);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, new ChangeFieldEf(
				checkedFieldInt, new ValueOp(1)));
		event.addEffect(ConditionedEvType.CONDITIONS_UNMET, new ChangeFieldEf(
				checkedFieldInt, new ValueOp(0)));

		addEvent(event);
		ChangeFieldEf changeCheck = new ChangeFieldEf(checkedField,
				new NOTCond(c));
		addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, changeCheck);

		if (stringId != null) {
			Caption text = new Caption(stringId);
			text.setFont(font);
			text.setPadding(5);
			text.setTextPaint(ColorFill.BLACK);
			SceneElement e = new SceneElement(text);
			e.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, changeCheck);
			e.setPosition(new Position(45, 20, 0, 0.5f));
			addSceneElement(e);
		}
	}

}
