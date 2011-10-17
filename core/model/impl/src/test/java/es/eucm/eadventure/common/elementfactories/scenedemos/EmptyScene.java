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

package es.eucm.eadventure.common.elementfactories.scenedemos;

import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

/**
 * An empty scene
 * 
 */
public class EmptyScene extends EAdSceneImpl implements SceneDemo {

	private RectangleShape rectangle;

	public EmptyScene() {
		super("EmptyScene");
		rectangle = new RectangleShape(800, 600);
		rectangle.setPaint(new EAdLinearGradient(new EAdColor(240, 240, 240), EAdColor.WHITE, 800, 600));
		getBackground().getResources().addAsset(
				getBackground().getInitialBundle(),
				EAdBasicSceneElement.appearance, rectangle);
	}
	
	public void setBackgroundFill( EAdFill fill ){
		rectangle.setPaint(fill);
	}

	@Override
	public String getSceneDescription() {
		return "An empty scene";
	}

	public String getDemoName() {
		return "Empty Scene";
	}
	
	public String toString(){
		return getDemoName() + " - " + getSceneDescription();
	}

}
