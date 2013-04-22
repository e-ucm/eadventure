package ead.converter.subconverters.effects;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.WaitUntilEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.converter.ModelQuerier;
import ead.converter.subconverters.ConversationsConverter;
import ead.converter.subconverters.effects.EffectsConverter.EffectConverter;
import es.eucm.eadventure.common.data.chapter.effects.TriggerConversationEffect;

public class TriggerConversationConverter implements
		EffectConverter<TriggerConversationEffect> {

	private ModelQuerier modelQuerier;

	public TriggerConversationConverter(ModelQuerier modelQuerier) {
		this.modelQuerier = modelQuerier;
	}

	@Override
	public List<EAdEffect> convert(TriggerConversationEffect e) {
		ArrayList<EAdEffect> effects = new ArrayList<EAdEffect>();

		EAdEffect root = modelQuerier.getConversation(e.getTargetId());

		EAdField<Boolean> inConversation = new BasicField<Boolean>(root,
				ConversationsConverter.IN_CONVERSATION);

		ChangeFieldEf startConversation = new ChangeFieldEf(inConversation,
				EmptyCond.TRUE);
		// Next effects of this waituntil will not be executed until the
		// conversation ends
		WaitUntilEf waitUntil = new WaitUntilEf(new NOTCond(new OperationCond(
				inConversation)));
		waitUntil.addSimultaneousEffect(startConversation);
		waitUntil.addSimultaneousEffect(root);

		effects.add(waitUntil);
		return effects;
	}

}
