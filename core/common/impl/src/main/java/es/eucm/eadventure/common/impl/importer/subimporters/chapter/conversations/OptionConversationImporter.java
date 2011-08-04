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

package es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.GenericImporter;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.OptionConversationNode;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;
import es.eucm.eadventure.common.model.effects.impl.text.extra.Answer;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;

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
