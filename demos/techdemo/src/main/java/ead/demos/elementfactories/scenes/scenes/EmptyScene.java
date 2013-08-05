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
import es.eucm.ead.model.elements.scenes.BasicScene;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.LinearGradientFill;
import es.eucm.ead.model.params.paint.EAdFill;

/**
 * An empty scene
 * 
 */
public class EmptyScene extends BasicScene {

	private RectangleShape rectangle;

	public EmptyScene() {
		super();
		this.setId("EmptyScene");
		rectangle = new RectangleShape(800, 600);
		rectangle.setPaint(new LinearGradientFill(new ColorFill(240, 240, 240),
				ColorFill.WHITE, 800, 600));
		getBackground().getDefinition().addAsset(SceneElementDef.appearance,
				rectangle);
		//		this.background.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
		//				new ChangeSceneEf(null, new MaskTransition(new Image(
		//						"@drawable/man_stand_e_1.png"), 5000)));
	}

	public void setBackgroundFill(EAdFill fill) {
		rectangle.setPaint(fill);
	}

	public String getSceneDescription() {
		return "An empty scene. Not much to do here.";
	}

	public String getDemoName() {
		return "Empty Scene";
	}

	public String toString() {
		return getDemoName() + " - " + getSceneDescription();
	}

}
