package es.eucm.eadventure.editor.view.generics.scene.impl;

import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdEffect;
import es.eucm.eadventure.common.model.elements.ResourcedElementImpl;
import es.eucm.eadventure.common.model.elements.conditions.ANDCond;
import es.eucm.eadventure.common.model.elements.conditions.EmptyCond;
import es.eucm.eadventure.common.model.elements.conditions.NOTCond;
import es.eucm.eadventure.common.model.elements.conditions.OperationCond;
import es.eucm.eadventure.common.model.elements.conditions.enums.Comparator;
import es.eucm.eadventure.common.model.elements.effects.EffectsMacro;
import es.eucm.eadventure.common.model.elements.effects.TriggerMacroEf;
import es.eucm.eadventure.common.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.eadventure.common.model.elements.events.ConditionedEv;
import es.eucm.eadventure.common.model.elements.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.elements.extra.EAdList;
import es.eucm.eadventure.common.model.elements.guievents.MouseEventImpl;
import es.eucm.eadventure.common.model.elements.scene.EAdComplexSceneElement;
import es.eucm.eadventure.common.model.elements.scene.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.scenes.ComplexSceneElementImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementImpl;
import es.eucm.eadventure.common.model.elements.variables.EAdField;
import es.eucm.eadventure.common.model.elements.variables.FieldImpl;
import es.eucm.eadventure.common.model.elements.variables.SystemFields;
import es.eucm.eadventure.common.model.elements.variables.VarDefImpl;
import es.eucm.eadventure.common.model.elements.variables.operations.ValueOp;
import es.eucm.eadventure.common.model.predef.effects.ChangeAppearanceEf;
import es.eucm.eadventure.common.model.predef.effects.MakeActiveElementEf;
import es.eucm.eadventure.common.params.fills.EAdPaintImpl;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.drawable.basics.shapes.RectangleShape;

public class EditionSceneElement extends ComplexSceneElementImpl {

	private EAdSceneElement proxy = null; 
	
	private EAdList<EAdEffect> unselectEffects;
	
	public EditionSceneElement(EAdSceneElement element, float scale) {
		setId(element.getId() + "_edition");
		
		if (element instanceof SceneElementImpl) {
			proxy = new BasicSceneElementProxy(element);
		} else if (element instanceof EAdComplexSceneElement){
			proxy = new ComplexSceneElementProxy(element);
		}
		
		this.components.add(proxy);
		this.setVarInitialValue(SceneElementImpl.VAR_X, (int) (scale * (Integer) proxy.getVars().get(SceneElementImpl.VAR_X)));
		this.setVarInitialValue(SceneElementImpl.VAR_Y, (int) (scale * (Integer) proxy.getVars().get(SceneElementImpl.VAR_Y)));
		proxy.setVarInitialValue(SceneElementImpl.VAR_X, 0);
		proxy.setVarInitialValue(SceneElementImpl.VAR_Y, 0);
		this.setVarInitialValue(SceneElementImpl.VAR_SCALE, scale);
		
		proxy.setVarInitialValue(SceneElementImpl.VAR_ALPHA, 0.3f);
		
		ChangeFieldEf makeActiveElement = new MakeActiveElementEf(proxy);

		proxy.addBehavior(MouseEventImpl.MOUSE_ENTERED, changeAlphaEffect(proxy, 0.6f, new OperationCond(SystemFields.ACTIVE_ELEMENT, proxy, Comparator.DIFFERENT)));
		proxy.addBehavior(MouseEventImpl.MOUSE_EXITED, changeAlphaEffect(proxy, 0.3f, new OperationCond(SystemFields.ACTIVE_ELEMENT, proxy, Comparator.DIFFERENT)));
		proxy.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK, makeActiveElement);
		proxy.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK, changeAlphaEffect(proxy, 1.0f, null));

		ConditionedEv conditionedEvent = new ConditionedEv(new OperationCond(SystemFields.ACTIVE_ELEMENT, proxy, Comparator.EQUAL));
		conditionedEvent.addEffect(ConditionedEventType.CONDITIONS_UNMET, changeAlphaEffect(proxy, 0.3f, null));
		unselectEffects = conditionedEvent.getEffects().get(ConditionedEventType.CONDITIONS_UNMET);
		this.getEvents().add(conditionedEvent);

		addResizeSquare();
		
		addChangeAppearance();
		
	}
	
	private void addChangeAppearance() {
		SceneElementDefImpl squareDef = new SceneElementDefImpl();
		SceneElementImpl square = new SceneElementImpl(squareDef);
		square.setDragCond(EmptyCond.FALSE_EMPTY_CONDITION);
		square.setVarInitialValue(SceneElementImpl.VAR_X, 12);
		square.setVarInitialValue(SceneElementImpl.VAR_Y, 12);

		squareDef.getResources().addAsset(squareDef.getInitialBundle(), SceneElementDefImpl.appearance, new RectangleShape(10, 10, EAdPaintImpl.BLACK_ON_WHITE));
		square.setVarInitialValue(SceneElementImpl.VAR_VISIBLE, Boolean.FALSE);
		this.components.add(square);
		
		unselectEffects.add(new ChangeFieldEf(
				new FieldImpl<Boolean>(square, SceneElementImpl.VAR_VISIBLE),
				new ValueOp(Boolean.FALSE)));
		
		proxy.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK, new ChangeFieldEf(
				new FieldImpl<Boolean>(square, SceneElementImpl.VAR_VISIBLE),
				new ValueOp(Boolean.TRUE)));
		
		FieldImpl<EAdBundleId> appearanceField = new FieldImpl<EAdBundleId>(proxy, ResourcedElementImpl.VAR_BUNDLE_ID);
		FieldImpl<Boolean> changedAppearance = new FieldImpl<Boolean>(proxy, new VarDefImpl<Boolean>("changed", Boolean.class, Boolean.FALSE));

		EAdBundleId prev = proxy.getDefinition().getResources().getInitialBundle();
		for (EAdBundleId bundleID : proxy.getDefinition().getResources().getBundles()) {
			if (bundleID != proxy.getDefinition().getResources().getInitialBundle()) {
				EffectsMacro macro = new EffectsMacro();
				macro.getEffects().add(new ChangeAppearanceEf(proxy, bundleID));
				macro.getEffects().add( new ChangeFieldEf(changedAppearance, new ValueOp(Boolean.TRUE)));
				
				ANDCond cond = new ANDCond(new OperationCond(appearanceField, prev, Comparator.EQUAL), 
						new NOTCond(new OperationCond(changedAppearance)));
				
				TriggerMacroEf effect = new TriggerMacroEf();
				effect.putMacro(macro, cond);
				square.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK, effect);
				
				prev = bundleID;
			}
		}

		EAdEffect effect = new ChangeAppearanceEf(proxy, proxy.getDefinition().getResources().getInitialBundle());
		ANDCond cond = new ANDCond(new OperationCond(appearanceField, prev, Comparator.EQUAL), 
				new NOTCond(new OperationCond(changedAppearance)));
		effect.setCondition(cond);
		square.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK, effect);
		
		square.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK, new ChangeFieldEf(changedAppearance, new ValueOp(Boolean.FALSE)));

	}

	private void addResizeSquare() {
		SceneElementDefImpl squareDef = new SceneElementDefImpl();
		SceneElementImpl square = new SceneElementImpl(squareDef);
		square.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		squareDef.getResources().addAsset(squareDef.getInitialBundle(), SceneElementDefImpl.appearance, new RectangleShape(10, 10, EAdPaintImpl.BLACK_ON_WHITE));
		square.setVarInitialValue(SceneElementImpl.VAR_VISIBLE, Boolean.FALSE);
		this.components.add(square);
		
		unselectEffects.add(new ChangeFieldEf(
				new FieldImpl<Boolean>(square, SceneElementImpl.VAR_VISIBLE),
				new ValueOp(Boolean.FALSE)));
		
		proxy.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK, new ChangeFieldEf(
				new FieldImpl<Boolean>(square, SceneElementImpl.VAR_VISIBLE),
				new ValueOp(Boolean.TRUE)));
		proxy.addBehavior(MouseEventImpl.MOUSE_LEFT_CLICK, new ChangeFieldEf(
				new FieldImpl<Integer>(square, SceneElementImpl.VAR_X),
				new ValueOp(new FieldImpl<Integer>(proxy, SceneElementImpl.VAR_WIDTH))));

	}
	
	
	
	private ChangeFieldEf changeAlphaEffect(EAdSceneElement proxy, float alpha, EAdCondition cond) {
		EAdField<Float> alphaField = new FieldImpl<Float>(proxy,
				SceneElementImpl.VAR_ALPHA);
		ChangeFieldEf effect = new ChangeFieldEf(
				 alphaField, new ValueOp(alpha));
		if (cond != null)
			effect.setCondition(cond);
		return effect;
	}
	

}
