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

package ead.elementfactories.demos.scenes;

import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEventType;
import ead.common.model.elements.guievents.EAdMouseEvent;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdFieldImpl;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.MathOp;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.model.predef.effects.MakeActiveElementEf;
import ead.common.params.fills.EAdColor;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;

public class SharingEffectsScene extends EmptyScene {

	public SharingEffectsScene( ){
		SceneElementImpl b = new SceneElementImpl(  new RectangleShape( 50, 50, EAdColor.RED ) );
		b.setId("button");
		
		EAdField<Float> field = new EAdFieldImpl<Float>( SystemFields.ACTIVE_ELEMENT, SceneElementImpl.VAR_ROTATION );
		ChangeFieldEf effect = new ChangeFieldEf( field, new MathOp("[0] + 0.1", field) );
		effect.setId("change");
		b.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, effect);
		
		ChangeFieldEf changeAlpha1 = new ChangeFieldEf();
		changeAlpha1.setId("changeAlpha");
		changeAlpha1.setParentVar(SceneElementImpl.VAR_ALPHA);
		changeAlpha1.setOperation(new ValueOp(new Float(0.5f)));
		
		ChangeFieldEf changeAlpha2 = new ChangeFieldEf();
		changeAlpha2.setId("changeAlpha");
		changeAlpha2.setParentVar(SceneElementImpl.VAR_ALPHA);
		changeAlpha2.setOperation(new ValueOp(new Float(1.0f)));
		
		SceneElementEv event = new SceneElementEv();
//		EAdVarInterpolationEffect rotate = new EAdVarInterpolationEffect( "rotate", )
		
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE, effect);
		
		b.setPosition(20, 20);
		this.getComponents().add(b);
		
		for ( int i = 0; i < 4; i++)
			for ( int j = 0; j < 4; j++ ){
				SceneElementImpl e = new SceneElementImpl( new RectangleShape( 30, 30, EAdColor.BLUE ) );
				e.setId("e" + i + "" + j);
				e.setPosition(new EAdPosition( Corner.CENTER, i * 60 + 40, j * 60 + 100));
				MakeActiveElementEf ef = new MakeActiveElementEf( e );
				e.addBehavior(EAdMouseEvent.MOUSE_LEFT_CLICK, ef);
				e.addBehavior(EAdMouseEvent.MOUSE_ENTERED, changeAlpha1);
				e.addBehavior(EAdMouseEvent.MOUSE_EXITED, changeAlpha2);
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
