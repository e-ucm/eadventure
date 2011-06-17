package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.GenericImporter;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.OptionConversationNode;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;
import es.eucm.eadventure.common.model.effects.impl.text.extra.Answer;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;

public class OptionConversationImporter implements EAdElementImporter<OptionConversationNode, EAdShowQuestion>{
	
	@Inject
	private GenericImporter<ConversationLine, Caption> captionImporter;

	public EAdShowQuestion init(OptionConversationNode oldObject) {
		return new EAdShowQuestion();
	}

	@Override
	public EAdShowQuestion convert(OptionConversationNode oldObject, Object object) {
		EAdShowQuestion effect = (EAdShowQuestion ) object;
		
		for ( int i = 0; i < oldObject.getLineCount(); i++ ){
			Answer a = new Answer( "answer" );
			Caption caption = captionImporter.init(oldObject.getLine(i));
			caption = captionImporter.convert(oldObject.getLine(i), caption);
			a.getResources().addAsset(a.getInitialBundle(), EAdBasicSceneElement.appearance, caption);
			effect.getAnswers().add(a);
			// FIXME Conditions
		}
		
		effect.setUpNewInstance();
		return effect;
	}


}
