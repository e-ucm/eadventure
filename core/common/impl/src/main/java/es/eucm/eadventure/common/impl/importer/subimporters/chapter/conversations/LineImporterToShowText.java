package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.GenericImporter;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;

public class LineImporterToShowText implements EAdElementImporter<ConversationLine, EAdShowText>{
	
	@Inject
	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;
	
	@Inject
	private GenericImporter<ConversationLine, Caption> captionImporter;

	public EAdShowText init(ConversationLine line) {
		return new EAdShowText();
	}
	
	@Override
	public EAdShowText convert(ConversationLine line, Object object) {
		EAdShowText effect = (EAdShowText) object;

		// Set conditions

		if (line.getConditions() != null) {
			EAdCondition condition = conditionsImporter.init(line
					.getConditions());
			condition = conditionsImporter.convert(line
					.getConditions(), condition);
			if (condition != null) {
				effect.setCondition(condition);
			}
		}
		
		Caption caption = captionImporter.init(line);		
		caption = captionImporter.convert(line, caption);		
		effect.setCaption(caption, 300, 300);
		return effect;
	}

}
