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

package ead.common.model.elements.predef;

import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.drawable.basics.shapes.RectangleShape;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.operations.MathOp;
import ead.common.model.elements.operations.SystemFields;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.util.Position.Corner;

public class LoadingScreen extends BasicScene implements EAdScene {

	private static final String ID = "#engine.LoadingScreen";

	public LoadingScreen() {
		super(new RectangleShape(800, 600, ColorFill.WHITE));
		this.setId(ID);

		SceneElement s = new SceneElement(new Image("@drawable/loadingbar.png"));
		s.setPosition(Corner.TOP_RIGHT, 0, 195);

		SceneElementEv updateLoad = new SceneElementEv();
		MathOp op = new MathOp("[0] * (800 / 100 )");
		op.getOperationsList().add(SystemFields.LOADING);
		updateLoad.addEffect(SceneElementEvType.ALWAYS, new ChangeFieldEf(s
				.getField(SceneElement.VAR_X), op));
		s.getEvents().add(updateLoad);
		add(s);

		SceneElement bg = new SceneElement(new Image("@drawable/loading.png"));
		add(bg);
	}

}
