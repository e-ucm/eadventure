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

package es.eucm.eadventure.editor.impl;

import junit.framework.TestCase;

import org.junit.Test;

import es.eucm.eadventure.common.impl.writer.EAdAdventureModelWriter;
import es.eucm.eadventure.common.model.EAdAdventureModel;
import es.eucm.eadventure.common.model.EAdChapterModel;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScreen;
import es.eucm.eadventure.common.model.effects.impl.actorreference.EAdMoveActorReference;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.effects.impl.text.extra.TextAnswer;
import es.eucm.eadventure.common.model.elements.EAdScreen;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.elements.impl.EAdSpace;
import es.eucm.eadventure.common.model.elements.impl.EAdTextImpl;
import es.eucm.eadventure.common.model.impl.EAdAdventureModelImpl;
import es.eucm.eadventure.common.model.impl.EAdChapterModelImpl;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdFont;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.assets.animation.frameanimation.Frame;
import es.eucm.eadventure.common.resources.assets.animation.frameanimation.FramesAnimation;
import es.eucm.eadventure.common.resources.assets.impl.ImageImpl;

public class WriterTest extends TestCase {

	@Test
	public void testWriter() {
		EAdAdventureModelWriter writer = new EAdAdventureModelWriter();
		writer.write(createModel(), System.out);
	}
	
	
	private EAdAdventureModel createModel() {
		EAdAdventureModel model = new EAdAdventureModelImpl();
		EAdChapterModel chapter = new EAdChapterModelImpl(model, "chapter");
		model.getChapters().add(chapter);
		
		EAdScreen screen = new EAdSpace(chapter, "screen");
		chapter.getScenes().add(screen);
		
		screen.getResources( ).addAsset( screen.getInitialBundle( ), EAdSpace.appearance, new ImageImpl( "@drawable/loading.png") );

		EAdSpace space2 = new EAdSpace( screen.getParent(), "LoadingScreen2" );
		space2.getResources( ).addAsset( space2.getInitialBundle( ), EAdSpace.appearance, new ImageImpl( "@drawable/Creditos.jpg") );

		EAdBasicActor actor = new EAdBasicActor( screen.getParent(), "StartGame" );
		actor.getResources( ).addAsset( actor.getInitialBundle( ), EAdBasicActor.appearance, new ImageImpl( "@drawable/start.png" ) );
//		actor.getResources( ).addAsset( actor.getInitialBundle( ), EAdBasicActor.appearance, new CaptionImpl("TEST_CAPTION") );

		
		EAdBasicAction action = new EAdBasicAction( actor, "id2" );
		action.getResources( ).addAsset( EAdBasicAction.appearance, new ImageImpl( "@drawable/grab.png") );

		actor.getActions( ).add( action );

		EAdChangeScreen changeScreen = new EAdChangeScreen( action, "changscree" );
		changeScreen.setNextScreen( space2 );
		//action.getEffects().add(changeScreen);

		EAdShowText effect = new EAdShowText( action, "id3" );
		EAdTextImpl eadTextImpl = new EAdTextImpl(effect, "idText");
		eadTextImpl.setText(new EAdString("stringId"));
		//effect.getResources( ).addAsset( EAdShowText.text, new EAdString( "Esta línea forma parte de un efecto de texto y es demasiado larga como para estar en una única línea, así que quizá habría que pensar algo para partirla o..." ) );

		eadTextImpl.setFont( EAdFont.REGULAR );
		eadTextImpl.setTextColor( EAdBorderedColor.BLACK_ON_WHITE );
		eadTextImpl.setPosition(new EAdPosition( 30, 50 ));
		effect.setText(eadTextImpl);
		
		action.getEffects( ).add( effect );

		action.setName(new EAdString("stringId"));
		//action.getResources( ).addAsset( EAdBasicAction.name, new EAdString( "ActionName" ) );

		EAdShowQuestion effect2 = new EAdShowQuestion( action, "question234" );
		EAdTextImpl eadTextImpl2 = new EAdTextImpl(effect2, "idText2");
		eadTextImpl2.setTextColor(EAdBorderedColor.BLACK_ON_WHITE);
		
		TextAnswer answer = new TextAnswer(effect2);
		answer.setText(new EAdString("stringId"));
		answer.getEffects().add(effect);
		effect2.getAnswers().add(answer);

		TextAnswer answer2 = new TextAnswer(effect2);
		answer2.setText(new EAdString("stringId"));
		answer2.getEffects().add(effect);
		effect2.getAnswers().add(answer2);

		TextAnswer answer3 = new TextAnswer(effect2);
		answer3.setText(new EAdString("stringId"));
		answer3.getEffects().add(effect);
		effect2.getAnswers().add(answer3);

		/*
		effect2.getResources( ).addAssets( EAdShowQuestion.answersList,
				new EAdString( "Sí, podría ser, estoy interesado" ),
				new EAdString( "No, no me gustaría" ),
				new EAdString( "Puede ser que en algún momento..." ) );
		List<EAdEffect> list = new ArrayList<EAdEffect>();
		list.add(effect);
		
		effect2.getAnswerEffects().add(list);
		effect2.getAnswerEffects().add(list);
		effect2.getAnswerEffects().add(list);
		*/
		
		//effect2.getResources( ).addAssets( EAdShowQuestion.text, new EAdString( "¿Qué gran preguntar por hacer?" ) );
		eadTextImpl2.setText(new EAdString("Stringid"));
		eadTextImpl2.setTextColor(EAdBorderedColor.BLACK_ON_WHITE);
		eadTextImpl2.setFont( new EAdFont( "Arial", 20.0f, EAdFont.Style.BOLD ) );
		eadTextImpl2.setPosition(new EAdPosition( 60, 100 ));
		effect2.setText(eadTextImpl2);
		action.getEffects( ).add( effect2 );
		
		EAdActorReferenceImpl ref = new EAdActorReferenceImpl( screen, "id4", actor );
		ref.setPosition(new EAdPosition( EAdPosition.Corner.BOTTOM_CENTER, 200, 200 ));
		ref.setScale( 0.8f );
		
		
		FramesAnimation animation = new FramesAnimation( );

		for ( int i = 1; i <= 8; i++ )
			animation.addFrame(new Frame(new ImageImpl("@drawable/paniel_wlr_0" + i + ".png")));

		
		EAdBasicActor actor2 = new EAdBasicActor( screen.getParent(), "Paniel" );
		//TODO Add frames to the animation
		actor2.getResources( ).addAsset( actor2.getInitialBundle( ), EAdBasicActor.appearance, animation );
		
		EAdActorReferenceImpl ref2 = new EAdActorReferenceImpl( screen, "id5", actor2 );
		ref2.setPosition(new EAdPosition( EAdPosition.Corner.BOTTOM_CENTER, 400, 200 ));
		ref2.setScale( 0.5f );
		
		EAdMoveActorReference move = new EAdMoveActorReference( action, "move");
		move.setActorReference(ref2);
		move.setTargetCoordiantes(0, 0);
		action.getEffects().add(move);

		screen.getActorReferences( ).add( ref );
		screen.getActorReferences( ).add( ref2 );
		return model;

	}
}
