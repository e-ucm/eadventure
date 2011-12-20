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

package es.eucm.eadventure.common.predef.model.effects;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;

public class EAdSpeakSceneElement extends EAdSpeakEffect {
	
	public EAdSpeakSceneElement(){
		super( );
	}

	public EAdSpeakSceneElement(EAdElement element) {
		super();
		setId("speakSceneElement");
		this.setElement(element);
	}
	
	public void setElement( EAdElement element ){
		if ( element instanceof EAdSceneElement )
			setOrigin(element);
		else if ( element instanceof EAdSceneElementDef )
			setOrigin( new EAdFieldImpl<EAdSceneElement>( element, EAdSceneElementDefImpl.VAR_SCENE_ELEMENT));
		else
			setOrigin( element );
	}

	private void setOrigin(EAdElement element) {
		EAdFieldImpl<Integer> centerX = new EAdFieldImpl<Integer>(element,
				EAdBasicSceneElement.VAR_CENTER_X);
		EAdFieldImpl<Integer> centerY = new EAdFieldImpl<Integer>(element,
				EAdBasicSceneElement.VAR_CENTER_Y);
		
		setPosition(centerX, centerY);

	}

}
