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

import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class SharingEffectsScene extends EmptyScene {

	public SharingEffectsScene( ){
		EAdBasicSceneElement b = new EAdBasicSceneElement(  new RectangleShape( 50, 50, EAdColor.RED ) );
		b.setId("button");
		
		EAdField<Float> field = new EAdFieldImpl<Float>( SystemFields.ACTIVE_ELEMENT, EAdBasicSceneElement.VAR_ROTATION );
		EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect( field, new MathOperation("[0] + 0.1", field) );
		effect.setId("change");
		b.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);
		
		EAdChangeFieldValueEffect changeAlpha1 = new EAdChangeFieldValueEffect();
		changeAlpha1.setId("changeAlpha");
		changeAlpha1.setParentVar(EAdBasicSceneElement.VAR_ALPHA);
		changeAlpha1.setOperation(new ValueOperation(new Float(0.5f)));
		
		EAdChangeFieldValueEffect changeAlpha2 = new EAdChangeFieldValueEffect();
		changeAlpha2.setId("changeAlpha");
		changeAlpha2.setParentVar(EAdBasicSceneElement.VAR_ALPHA);
		changeAlpha2.setOperation(new ValueOperation(new Float(1.0f)));
		
		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
//		EAdVarInterpolationEffect rotate = new EAdVarInterpolationEffect( "rotate", )
		
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);
		
		b.setPosition(20, 20);
		this.getComponents().add(b);
		
		for ( int i = 0; i < 4; i++)
			for ( int j = 0; j < 4; j++ ){
				EAdBasicSceneElement e = new EAdBasicSceneElement( new RectangleShape( 30, 30, EAdColor.BLUE ) );
				e.setId("e" + i + "" + j);
				e.setPosition(new EAdPositionImpl( Corner.CENTER, i * 60 + 40, j * 60 + 100));
				EAdMakeActiveElementEffect ef = new EAdMakeActiveElementEffect( e );
				e.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, ef);
				e.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, changeAlpha1);
				e.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, changeAlpha2);
				getComponents().add(e);
			}
				
	}
	
	@Override
	public String getSceneDescription() {
		return "A scene in which elements share effects";
	}

	public String getDemoName() {
		return "Sharing Effects Scene";
	}
}
