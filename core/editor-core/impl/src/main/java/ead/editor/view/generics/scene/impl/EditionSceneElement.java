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

package ead.editor.view.generics.scene.impl;

import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.ResourcedElement;
import ead.common.model.elements.conditions.ANDCond;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdComplexSceneElement;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.VarDef;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.model.predef.effects.ChangeAppearanceEf;
import ead.common.model.predef.effects.MakeActiveElementEf;
import ead.common.params.fills.PaintFill;
import ead.common.resources.EAdBundleId;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;

public class EditionSceneElement extends ComplexSceneElement {

	private EAdSceneElement proxy = null; 
	
	private EAdList<EAdEffect> unselectEffects;
	
	public EditionSceneElement(EAdSceneElement element, float scale) {
		setId(element.getId() + "_edition");
		
		if (element instanceof SceneElement) {
			proxy = new BasicSceneElementProxy(element);
		} else if (element instanceof EAdComplexSceneElement){
			proxy = new ComplexSceneElementProxy(element);
		}
		
		this.sceneElements.add(proxy);
		this.setVarInitialValue(SceneElement.VAR_X, (int) (scale * (Integer) proxy.getVars().get(SceneElement.VAR_X)));
		this.setVarInitialValue(SceneElement.VAR_Y, (int) (scale * (Integer) proxy.getVars().get(SceneElement.VAR_Y)));
		proxy.setVarInitialValue(SceneElement.VAR_X, 0);
		proxy.setVarInitialValue(SceneElement.VAR_Y, 0);
		this.setInitialScale(scale);
		
		proxy.setVarInitialValue(SceneElement.VAR_ALPHA, 0.3f);
		
		ChangeFieldEf makeActiveElement = new MakeActiveElementEf(proxy);

		proxy.addBehavior(MouseGEv.MOUSE_ENTERED, changeAlphaEffect(proxy, 0.6f, new OperationCond(SystemFields.ACTIVE_ELEMENT, proxy, Comparator.DIFFERENT)));
		proxy.addBehavior(MouseGEv.MOUSE_EXITED, changeAlphaEffect(proxy, 0.3f, new OperationCond(SystemFields.ACTIVE_ELEMENT, proxy, Comparator.DIFFERENT)));
		proxy.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, makeActiveElement);
		proxy.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, changeAlphaEffect(proxy, 1.0f, null));

		ConditionedEv conditionedEvent = new ConditionedEv(new OperationCond(SystemFields.ACTIVE_ELEMENT, proxy, Comparator.EQUAL));
		conditionedEvent.addEffect(ConditionedEvType.CONDITIONS_UNMET, changeAlphaEffect(proxy, 0.3f, null));
		unselectEffects = conditionedEvent.getEffects().get(ConditionedEvType.CONDITIONS_UNMET);
		this.getEvents().add(conditionedEvent);

		addResizeSquare();
		
		addChangeAppearance();
		
	}
	
	private void addChangeAppearance() {
		SceneElementDef squareDef = new SceneElementDef();
		SceneElement square = new SceneElement(squareDef);
		square.setDragCond(EmptyCond.FALSE_EMPTY_CONDITION);
		square.setVarInitialValue(SceneElement.VAR_X, 12);
		square.setVarInitialValue(SceneElement.VAR_Y, 12);

		squareDef.getResources().addAsset(squareDef.getInitialBundle(), SceneElementDef.appearance, new RectangleShape(10, 10, PaintFill.BLACK_ON_WHITE));
		square.setVarInitialValue(SceneElement.VAR_VISIBLE, Boolean.FALSE);
		this.sceneElements.add(square);
		
		unselectEffects.add(new ChangeFieldEf(
				new BasicField<Boolean>(square, SceneElement.VAR_VISIBLE),
				new ValueOp(Boolean.FALSE)));
		
		proxy.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, new ChangeFieldEf(
				new BasicField<Boolean>(square, SceneElement.VAR_VISIBLE),
				new ValueOp(Boolean.TRUE)));
		
		BasicField<EAdBundleId> appearanceField = new BasicField<EAdBundleId>(proxy, ResourcedElement.VAR_BUNDLE_ID);
		BasicField<Boolean> changedAppearance = new BasicField<Boolean>(proxy, new VarDef<Boolean>("changed", Boolean.class, Boolean.FALSE));

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
				square.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, effect);
				
				prev = bundleID;
			}
		}

		EAdEffect effect = new ChangeAppearanceEf(proxy, proxy.getDefinition().getResources().getInitialBundle());
		ANDCond cond = new ANDCond(new OperationCond(appearanceField, prev, Comparator.EQUAL), 
				new NOTCond(new OperationCond(changedAppearance)));
		effect.setCondition(cond);
		square.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, effect);
		
		square.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, new ChangeFieldEf(changedAppearance, new ValueOp(Boolean.FALSE)));

	}

	private void addResizeSquare() {
		SceneElementDef squareDef = new SceneElementDef();
		SceneElement square = new SceneElement(squareDef);
		square.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
		squareDef.getResources().addAsset(squareDef.getInitialBundle(), SceneElementDef.appearance, new RectangleShape(10, 10, PaintFill.BLACK_ON_WHITE));
		square.setVarInitialValue(SceneElement.VAR_VISIBLE, Boolean.FALSE);
		this.sceneElements.add(square);
		
		unselectEffects.add(new ChangeFieldEf(
				new BasicField<Boolean>(square, SceneElement.VAR_VISIBLE),
				new ValueOp(Boolean.FALSE)));
		
		proxy.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, new ChangeFieldEf(
				new BasicField<Boolean>(square, SceneElement.VAR_VISIBLE),
				new ValueOp(Boolean.TRUE)));
		proxy.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, new ChangeFieldEf(
				new BasicField<Integer>(square, SceneElement.VAR_X),
				new ValueOp(new BasicField<Integer>(proxy, SceneElement.VAR_WIDTH))));

	}
	
	
	
	private ChangeFieldEf changeAlphaEffect(EAdSceneElement proxy, float alpha, EAdCondition cond) {
		EAdField<Float> alphaField = new BasicField<Float>(proxy,
				SceneElement.VAR_ALPHA);
		ChangeFieldEf effect = new ChangeFieldEf(
				 alphaField, new ValueOp(alpha));
		if (cond != null)
			effect.setCondition(cond);
		return effect;
	}
	

}
