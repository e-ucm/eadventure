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

package es.eucm.eadventure.common.elementfactories.demos.scenes;

import es.eucm.eadventure.common.elementfactories.demos.SceneDemo;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneImpl;
import es.eucm.eadventure.common.params.fills.EAdColor;
import es.eucm.eadventure.common.params.fills.EAdLinearGradient;
import es.eucm.eadventure.common.params.paint.EAdFill;
import es.eucm.eadventure.common.params.text.EAdString;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.RectangleShape;

/**
 * An empty scene
 * 
 */
public class EmptyScene extends SceneImpl implements SceneDemo {

	private RectangleShape rectangle;

	public EmptyScene() {
		super();
		setId("EmptyScene");
		((SceneElementDefImpl) this.getDefinition()).setName(EAdString.newEAdString("name"));
		((SceneElementDefImpl) this.getDefinition()).setDesc(EAdString.newEAdString("desc"));
		((SceneElementDefImpl) this.getDefinition()).setDetailDesc(EAdString.newEAdString("detailDesc"));
		((SceneElementDefImpl) this.getDefinition()).setDoc(EAdString.newEAdString("doc"));
		
		
		rectangle = new RectangleShape(800, 600);
		rectangle.setPaint(new EAdLinearGradient(new EAdColor(240, 240, 240), EAdColor.WHITE, 800, 600));
		getBackground().getDefinition().getResources().addAsset(
				getBackground().getDefinition().getInitialBundle(),
				SceneElementDefImpl.appearance, rectangle);
	}
	
	public void setBackgroundFill( EAdFill fill ){
		rectangle.setPaint(fill);
	}

	@Override
	public String getSceneDescription() {
		return "An empty scene. Not much to do here.";
	}

	public String getDemoName() {
		return "Empty Scene";
	}
	
	public String toString(){
		return getDemoName() + " - " + getSceneDescription();
	}

}
