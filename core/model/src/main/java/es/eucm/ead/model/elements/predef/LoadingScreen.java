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

package es.eucm.ead.model.elements.predef;

import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.WatchFieldEv;
import es.eucm.ead.model.elements.events.enums.WatchFieldEvType;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.util.Position.Corner;

public class LoadingScreen extends Scene {

	private static final String ID = "#engine.LoadingScreen";

	public LoadingScreen() {
		super(new RectangleShape(800, 600, ColorFill.WHITE));
		this.setId(ID);

		SceneElement s = new SceneElement(new Image("@drawable/loadingbar.png"));
		s.setPosition(Corner.TOP_RIGHT, 0, 195);

		WatchFieldEv watchField = new WatchFieldEv();
		MathOp op = new MathOp("[0] * (800 / 100 )");
		op.addOperation(SystemFields.LOADING);
		watchField.watchField(SystemFields.LOADING);
		watchField.addEffect(WatchFieldEvType.WATCH, new ChangeFieldEf(s
				.getField(SceneElement.VAR_X), op));
		s.addEvent(watchField);
		add(s);

		SceneElement bg = new SceneElement(new Image("@drawable/loading.png"));
		add(bg);
	}

}
