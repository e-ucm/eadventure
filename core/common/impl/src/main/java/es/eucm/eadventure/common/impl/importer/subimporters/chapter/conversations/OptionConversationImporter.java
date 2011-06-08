package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.OptionConversationNode;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;
import es.eucm.eadventure.common.model.effects.impl.text.extra.Answer;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;

public class OptionConversationImporter implements Importer<OptionConversationNode, EAdShowQuestion>{
	
	@Inject
	private Importer<ConversationLine, Caption> captionImporter;

	@Override
	public EAdShowQuestion convert(OptionConversationNode oldObject) {
		EAdShowQuestion effect = new EAdShowQuestion( );
		
		for ( int i = 0; i < oldObject.getLineCount(); i++ ){
			Answer a = new Answer( "answer" );
			Caption caption = captionImporter.convert(oldObject.getLine(i));
			a.getResources().addAsset(a.getInitialBundle(), EAdBasicSceneElement.appearance, caption);
			effect.getAnswers().add(a);
			// FIXME Conditions
		}
		
		effect.setUpNewInstance();
		return effect;
	}

	@Override
	public boolean equals(OptionConversationNode oldObject, EAdShowQuestion newObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
