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

package es.eucm.eadventure.common.impl.importer.subimporters.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.TriggerConversationEffect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.elements.EAdCondition;

public class TriggerConversationImporter extends EffectImporter<TriggerConversationEffect, EAdTriggerMacro>{
	
	private EAdElementFactory factory;
	
	@Inject
	public TriggerConversationImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionImporter, EAdElementFactory factory) {
		super(conditionImporter);
		this.factory = factory;
	}

	@Override
	public EAdTriggerMacro init(TriggerConversationEffect oldObject) {
		EAdMacro macro = new EAdMacroImpl();
		macro.setId("macroConversation");
		EAdTriggerMacro triggerMacro = new EAdTriggerMacro( macro );
		triggerMacro.setQueueable(true);
		return triggerMacro;
	}
	
	@Override
	public EAdTriggerMacro convert(TriggerConversationEffect oldObject, Object object) {
		EAdTriggerMacro triggerMacro = super.convert(oldObject, object);
		EAdMacro macro = triggerMacro.getMacro();
		
		EAdEffect effect = (EAdEffect) factory.getElementById(oldObject.getTargetId());
		if ( effect != null )
			macro.getEffects().add(effect);
		
		return triggerMacro;
	}

}
