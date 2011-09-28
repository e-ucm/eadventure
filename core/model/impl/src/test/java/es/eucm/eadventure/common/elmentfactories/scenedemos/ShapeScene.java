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

package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elmentfactories.assets.ShapeFactory;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdBorderedColor;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

/**
 * Scene with shapes. Test containst for different types of shapes
 * 
 *
 */
public class ShapeScene extends EmptyScene {
	public ShapeScene( ) {
		int margin = 10;
		int size = 140;
		int x = margin;
		
		ShapeFactory shapeFactory = EAdElementsFactory.getInstance().getShapeFactory();
		
		// Rectangle	
		AssetDescriptor rectangleAsset1 = shapeFactory.getElement(ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size, EAdBorderedColor.WHITE_ON_BLACK);
		AssetDescriptor rectangleAsset2 = shapeFactory.getElement(ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size, EAdBorderedColor.BLACK_ON_WHITE);
		getElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(rectangleAsset1, rectangleAsset2, x, margin));
		x+= margin + size;
		
		// Circle
		AssetDescriptor asset1 = shapeFactory.getElement(ShapeFactory.ShapeType.CIRCLE_SHAPE, size, size, new EAdBorderedColor( EAdColor.YELLOW, EAdColor.BROWN ));
		AssetDescriptor asset2 = shapeFactory.getElement(ShapeFactory.ShapeType.CIRCLE_SHAPE, size, size, new EAdBorderedColor( EAdColor.ORANGE, EAdColor.BROWN ));
		getElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset1, asset2, x, margin));
		x+= margin + size;
		
		// Triangle
		AssetDescriptor asset31 = shapeFactory.getElement(ShapeFactory.ShapeType.TRIANGLE_SHAPE, size, size, new EAdBorderedColor( EAdColor.BLUE, EAdColor.BROWN ));
		AssetDescriptor asset32 = shapeFactory.getElement(ShapeFactory.ShapeType.TRIANGLE_SHAPE, size, size, new EAdBorderedColor( EAdColor.CYAN, EAdColor.BLACK ));
		getElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset31, asset32, x, margin));
		x+= margin + size;
		
		// Irregular shape 1
		AssetDescriptor asset51 = shapeFactory.getElement(ShapeFactory.ShapeType.IRREGULAR_SHAPE_1, size, size, new EAdBorderedColor( EAdColor.MAGENTA, EAdColor.YELLOW ));
		AssetDescriptor asset52 = shapeFactory.getElement(ShapeFactory.ShapeType.IRREGULAR_SHAPE_1, size, size, new EAdBorderedColor( EAdColor.RED, EAdColor.ORANGE ));
		getElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset51, asset52, x, margin));
		x+= margin + size;
		
		// Random shape
		AssetDescriptor asset41 = shapeFactory.getElement(ShapeFactory.ShapeType.IRREGULAR_RANDOM_SHAPE, size, size, new EAdBorderedColor( EAdColor.GRAY, EAdColor.BROWN ));
		AssetDescriptor asset42 = shapeFactory.getElement(ShapeFactory.ShapeType.IRREGULAR_RANDOM_SHAPE, size, size, new EAdBorderedColor( EAdColor.DARK_GRAY, EAdColor.BLACK ));
		getElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset41, asset42, x, margin));
		x = margin;
		
		// Drop shape
		AssetDescriptor asset61 = shapeFactory.getElement(ShapeFactory.ShapeType.DROP_SHAPE, size, size, new EAdBorderedColor( EAdColor.GRAY, EAdColor.BROWN ));
		AssetDescriptor asset62 = shapeFactory.getElement(ShapeFactory.ShapeType.DROP_SHAPE, size, size, new EAdBorderedColor( EAdColor.DARK_GRAY, EAdColor.BLACK ));
		getElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset61, asset62, x, margin * 2 + size));
		x+= margin + size;
		
		// Rectangle rotating
		AssetDescriptor asset21 = shapeFactory.getElement(ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size * 3, new EAdBorderedColor( EAdColor.RED, EAdColor.BROWN ));
		AssetDescriptor asset22 = shapeFactory.getElement(ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size * 3, new EAdBorderedColor( EAdColor.LIGHT_BROWN, EAdColor.DARK_BROWN));
		EAdBasicSceneElement rotatingRectangle = EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset21, asset22, 330, 200);
		getElements().add(rotatingRectangle);
		EAdVarInterpolationEffect interpolation = EAdElementsFactory.getInstance().getEffectFactory().getInterpolationEffect(new EAdFieldImpl<Float>(rotatingRectangle, EAdBasicSceneElement.VAR_ROTATION), 0, (float) (Math.PI * 2.0), 2000, LoopType.RESTART, InterpolationType.LINEAR); 
		EAdSceneElementEvent event = EAdElementsFactory.getInstance().getEventsFactory().getEvent(SceneElementEvent.ADDED_TO_SCENE, interpolation);
		rotatingRectangle.getEvents().add(event);
		getElements().add(rotatingRectangle);
		
	}
	
	@Override
	public String getDescription() {
		return "A scene with some shapes";
	}
	
	public String getDemoName(){
		return "Shape Scene";
	}

}
