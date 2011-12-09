package es.eucm.eadventure.editor.view.generics.scene.impl;

import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.conditions.impl.enums.Comparator;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdComplexElement;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.predef.model.effects.EAdMakeActiveElementEffect;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.RectangleShape;

public class EditionSceneElement extends EAdComplexElementImpl {

	public EditionSceneElement(EAdSceneElement element, float scale) {
		
		EAdSceneElement proxy = null; 
		if (element instanceof EAdBasicSceneElement) {
			proxy = new BasicSceneElementReplica(element);
		} else if (element instanceof EAdComplexElement){
			proxy = new ComplexSceneElementReplica(element);
		}
		
		this.components.add(proxy);
		this.setVarInitialValue(EAdBasicSceneElement.VAR_X, (int) (scale * (Integer) proxy.getVars().get(EAdBasicSceneElement.VAR_X)));
		this.setVarInitialValue(EAdBasicSceneElement.VAR_Y, (int) (scale * (Integer) proxy.getVars().get(EAdBasicSceneElement.VAR_Y)));
		
		proxy.setVarInitialValue(EAdBasicSceneElement.VAR_X, 0);
		proxy.setVarInitialValue(EAdBasicSceneElement.VAR_Y, 0);
		this.setVarInitialValue(EAdBasicSceneElement.VAR_SCALE, scale);
		
		proxy.setVarInitialValue(EAdBasicSceneElement.VAR_ALPHA, 0.3f);
		
		EAdChangeFieldValueEffect makeActiveElement = new EAdMakeActiveElementEffect(proxy);

		proxy.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, changeAlphaEffect(proxy, 0.6f, new OperationCondition(SystemFields.ACTIVE_ELEMENT, proxy, Comparator.DIFFERENT)));
		proxy.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, changeAlphaEffect(proxy, 0.3f, new OperationCondition(SystemFields.ACTIVE_ELEMENT, proxy, Comparator.DIFFERENT)));
		proxy.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, makeActiveElement);
		proxy.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, changeAlphaEffect(proxy, 1.0f, null));
		
		EAdSceneElementDefImpl squareDef = new EAdSceneElementDefImpl();
		EAdBasicSceneElement square = new EAdBasicSceneElement(squareDef);
		square.setDragCond(EmptyCondition.TRUE_EMPTY_CONDITION);
		squareDef.getResources().addAsset(squareDef.getInitialBundle(), EAdSceneElementDefImpl.appearance, new RectangleShape(10, 10, EAdPaintImpl.BLACK_ON_WHITE));
		square.setVarInitialValue(EAdBasicSceneElement.VAR_VISIBLE, Boolean.FALSE);
		this.components.add(square);
		
		proxy.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdChangeFieldValueEffect(
				new EAdFieldImpl<Boolean>(square, EAdBasicSceneElement.VAR_VISIBLE),
				new ValueOperation(Boolean.TRUE)));

		
		EAdConditionEventImpl conditionedEvent = new EAdConditionEventImpl(new OperationCondition(SystemFields.ACTIVE_ELEMENT, proxy, Comparator.DIFFERENT));
		conditionedEvent.addEffect(ConditionedEventType.CONDITIONS_MET, changeAlphaEffect(proxy, 0.3f, null));
		
	}
	
	private EAdChangeFieldValueEffect changeAlphaEffect(EAdSceneElement proxy, float alpha, EAdCondition cond) {
		EAdField<Float> alphaField = new EAdFieldImpl<Float>(proxy,
				EAdBasicSceneElement.VAR_ALPHA);
		EAdChangeFieldValueEffect effect = new EAdChangeFieldValueEffect(
				 alphaField, new ValueOperation(alpha));
		if (cond != null)
			effect.setCondition(cond);
		return effect;
	}
	

}
