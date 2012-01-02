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

package ead.common.model.predef.effects;

import ead.common.model.EAdElement;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scene.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDefImpl;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.variables.FieldImpl;
import ead.common.params.text.EAdString;

public class SpeakSceneElementEf extends SpeakEf {
	
	public SpeakSceneElementEf(){
		super( );
	}
	
	public SpeakSceneElementEf(EAdString string){
		super( string );
	}

	public SpeakSceneElementEf(EAdElement element) {
		super();
		setId("speakSceneElement");
		this.setElement(element);
	}
	
	public void setElement( EAdElement element ){
		if ( element instanceof EAdSceneElement )
			setOrigin(element);
		else if ( element instanceof EAdSceneElementDef )
			setOrigin( new FieldImpl<EAdSceneElement>( element, SceneElementDefImpl.VAR_SCENE_ELEMENT));
		else
			setOrigin( element );
	}

	private void setOrigin(EAdElement element) {
		FieldImpl<Integer> centerX = new FieldImpl<Integer>(element,
				SceneElementImpl.VAR_CENTER_X);
		FieldImpl<Integer> centerY = new FieldImpl<Integer>(element,
				SceneElementImpl.VAR_CENTER_Y);
		
		setPosition(centerX, centerY);

	}

}
