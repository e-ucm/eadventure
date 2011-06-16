package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;

public class LineImporterToShowText implements Importer<ConversationLine, EAdShowText>{
	
	@Inject
	private Importer<Conditions, EAdCondition> conditionsImporter;
	
	@Inject
	private Importer<ConversationLine, Caption> captionImporter;

	@Override
	public EAdShowText convert(ConversationLine line) {
		EAdShowText effect = new EAdShowText();

		// Set conditions

		if (line.getConditions() != null) {
			EAdCondition condition = conditionsImporter.convert(line
					.getConditions());
			if (condition != null) {
				effect.setCondition(condition);
			}
		}
		
		Caption caption = captionImporter.convert(line);		
		effect.setCaption(caption, 300, 300);
		return effect;
	}

	@Override
	public boolean equals(ConversationLine oldObject, EAdShowText newObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
