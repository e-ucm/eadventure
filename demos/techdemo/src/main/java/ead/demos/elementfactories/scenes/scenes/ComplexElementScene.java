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

import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.Paint;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.demos.elementfactories.EAdElementsFactory;

public class ComplexElementScene extends EmptyScene {

	public ComplexElementScene() {
		this.setId("ComplexElementScene");
		RectangleShape rectangle = new RectangleShape(400, 400);
		rectangle.setPaint(Paint.BLACK_ON_WHITE);
		GroupElement complex = new GroupElement(rectangle);
		complex.setBounds(400, 400);
		complex.setPosition(new EAdPosition(Corner.CENTER, 400, 300));

		RectangleShape r1 = new RectangleShape(400, 400, ColorFill.BLUE);
		RectangleShape r2 = new RectangleShape(400, 400, ColorFill.RED);
		SceneElement e = EAdElementsFactory.getInstance()
				.getSceneElementFactory().createSceneElement(r1, r2, 40, 40);

		e.setInitialScale(0.1f);
		e.setVarInitialValue(SceneElement.VAR_ROTATION, (float) Math.PI / 6);
		e.setPosition(new EAdPosition(Corner.CENTER, 50, 50));

		complex.getSceneElements().add(e);

		getSceneElements().add(complex);

		EAdField<Float> rotation = new BasicField<Float>(complex,
				SceneElement.VAR_ROTATION);

		InterpolationEf effect = new InterpolationEf(rotation, 0,
				2 * (float) Math.PI, 10000, InterpolationLoopType.RESTART,
				InterpolationType.LINEAR);

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.FIRST_UPDATE, effect);

		complex.getEvents().add(event);

		EAdField<Float> rotation2 = new BasicField<Float>(e,
				SceneElement.VAR_ROTATION);

		e.addBehavior(MouseGEv.MOUSE_RIGHT_CLICK, new ChangeFieldEf(rotation,
				new ValueOp((float) 0.1f)));

		InterpolationEf effect2 = new InterpolationEf(rotation2, 0,
				2 * (float) Math.PI, 1000, InterpolationLoopType.RESTART,
				InterpolationType.LINEAR);

		SceneElementEv event2 = new SceneElementEv();
		event2.addEffect(SceneElementEvType.FIRST_UPDATE, effect2);

		e.getEvents().add(event2);

		EAdField<Float> scale = new BasicField<Float>(complex,
				SceneElement.VAR_SCALE);

		complex.setInitialScale(0.5f);
		InterpolationEf effect3 = new InterpolationEf(scale, 0.0f, 1.5f, 5000,
				InterpolationLoopType.REVERSE, InterpolationType.LINEAR);

		event2.addEffect(SceneElementEvType.FIRST_UPDATE, effect3);

	}

}
