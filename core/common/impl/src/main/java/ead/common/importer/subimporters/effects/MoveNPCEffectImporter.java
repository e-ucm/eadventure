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

package ead.common.importer.subimporters.effects;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.EAdElementFactory;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.TriggerMacroEf;
import ead.common.model.elements.effects.enums.MovementSpeed;
import ead.common.model.elements.effects.sceneelements.MoveSceneElementEf;
import ead.common.model.elements.scene.EAdSceneElementDef;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.MoveNPCEffect;

public class MoveNPCEffectImporter extends EffectImporter<MoveNPCEffect, TriggerMacroEf>{
	
	private EAdElementFactory factory;
	
	@Inject
	public MoveNPCEffectImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter,
			EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public TriggerMacroEf init(MoveNPCEffect oldObject) {
		TriggerMacroEf effect =  new TriggerMacroEf();
		effect.setId("moveNPC" + oldObject.getTargetId());
		return effect;
	}

	@Override
	public TriggerMacroEf convert(MoveNPCEffect oldObject, Object object) {
		TriggerMacroEf effect = super.convert(oldObject, object);	
		EffectsMacro macro = new EffectsMacro();
		macro.setId("macro");
		effect.putMacro(macro, EmptyCond.TRUE_EMPTY_CONDITION);
		

		macro.getEffects().add(new MoveSceneElementEf((EAdSceneElementDef) factory.getElementById(oldObject.getTargetId()), 
				oldObject.getX(), oldObject.getY(), MovementSpeed.NORMAL));

		return effect;
	}

}
