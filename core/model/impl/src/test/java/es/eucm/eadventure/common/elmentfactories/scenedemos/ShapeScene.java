package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.elmentfactories.assets.ShapeFactory;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
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
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(rectangleAsset1, rectangleAsset2, x, margin));
		x+= margin + size;
		
		// Circle
		AssetDescriptor asset1 = shapeFactory.getElement(ShapeFactory.ShapeType.CIRCLE_SHAPE, size, size, new EAdBorderedColor( EAdColor.YELLOW, EAdColor.BROWN ));
		AssetDescriptor asset2 = shapeFactory.getElement(ShapeFactory.ShapeType.CIRCLE_SHAPE, size, size, new EAdBorderedColor( EAdColor.ORANGE, EAdColor.BROWN ));
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset1, asset2, x, margin));
		x+= margin + size;
		
		// Triangle
		AssetDescriptor asset31 = shapeFactory.getElement(ShapeFactory.ShapeType.TRIANGLE_SHAPE, size, size, new EAdBorderedColor( EAdColor.BLUE, EAdColor.BROWN ));
		AssetDescriptor asset32 = shapeFactory.getElement(ShapeFactory.ShapeType.TRIANGLE_SHAPE, size, size, new EAdBorderedColor( EAdColor.CYAN, EAdColor.BLACK ));
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset31, asset32, x, margin));
		x+= margin + size;
		
		// Irregular shape 1
		AssetDescriptor asset51 = shapeFactory.getElement(ShapeFactory.ShapeType.IRREGULAR_SHAPE_1, size, size, new EAdBorderedColor( EAdColor.MAGENTA, EAdColor.YELLOW ));
		AssetDescriptor asset52 = shapeFactory.getElement(ShapeFactory.ShapeType.IRREGULAR_SHAPE_1, size, size, new EAdBorderedColor( EAdColor.RED, EAdColor.ORANGE ));
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset51, asset52, x, margin));
		x+= margin + size;
		
		// Random shape
		AssetDescriptor asset41 = shapeFactory.getElement(ShapeFactory.ShapeType.IRREGULAR_RANDOM_SHAPE, size, size, new EAdBorderedColor( EAdColor.GRAY, EAdColor.BROWN ));
		AssetDescriptor asset42 = shapeFactory.getElement(ShapeFactory.ShapeType.IRREGULAR_RANDOM_SHAPE, size, size, new EAdBorderedColor( EAdColor.DARK_GRAY, EAdColor.BLACK ));
		getSceneElements().add(EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset41, asset42, x, margin));
		x+= margin + size;
		
		// Rectangle rotating
		AssetDescriptor asset21 = shapeFactory.getElement(ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size * 3, new EAdBorderedColor( EAdColor.RED, EAdColor.BROWN ));
		AssetDescriptor asset22 = shapeFactory.getElement(ShapeFactory.ShapeType.RECTANGULAR_SHAPE, size, size * 3, new EAdBorderedColor( EAdColor.LIGHT_BROWN, EAdColor.DARK_BROWN));
		EAdSceneElement rotatingRectangle = EAdElementsFactory.getInstance().getSceneElementFactory().createSceneElement(asset21, asset22, 330, 200);
		getSceneElements().add(rotatingRectangle);
		EAdVarInterpolationEffect interpolation = EAdElementsFactory.getInstance().getEffectFactory().getInterpolationEffect(rotatingRectangle.rotationVar(), 0, (float) (2.0f * Math.PI), 60000, LoopType.RESTART); 
		EAdSceneElementEvent event = EAdElementsFactory.getInstance().getEventsFactory().getEvent(SceneElementEvent.ADDED_TO_SCENE, interpolation);
		rotatingRectangle.getEvents().add(event);
		getSceneElements().add(rotatingRectangle);
		
	}

}
