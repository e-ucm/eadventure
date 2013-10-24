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

package es.eucm.ead.model.elements.predef.sceneelements;

import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.util.Position;

public class LoaderSceneElement extends SceneElement {

	public static final String ID = "#engine.loader";

	public LoaderSceneElement() {
		super(new Image("@drawable/loader.png"));
		this.setId(ID);
		setPosition(Position.Corner.CENTER, 30, 30);
		setScale(0.5f);
		SceneElementEv event = new SceneElementEv();
		MathOp rotate = new MathOp("[0] + 0.5*33");
		rotate.addOperation(getField(SceneElement.VAR_ROTATION));
		event.addEffect(SceneElementEvType.ALWAYS, new ChangeFieldEf(
				getField(SceneElement.VAR_ROTATION), rotate));
		addEvent(event);
		this.setVisible(false);
	}
}
