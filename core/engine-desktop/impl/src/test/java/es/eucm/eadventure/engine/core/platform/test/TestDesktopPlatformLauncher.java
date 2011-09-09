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

package es.eucm.eadventure.engine.core.platform.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdButton;
import es.eucm.eadventure.common.model.elements.test.ActorReferenceFactoryTest;
import es.eucm.eadventure.common.model.elements.test.BasicActorFactoryTest;
import es.eucm.eadventure.common.model.elements.test.BasicSceneFactoryTest;
import es.eucm.eadventure.common.model.elements.test.VideoSceneFactoryTest;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.transitions.EAdTransition;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.EAdURI;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.DesktopPlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetRendererModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;

@Singleton
public class TestDesktopPlatformLauncher  {
	
	public static void main(String[] args) {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "eAdventure");

		Injector injector = Guice.createInjector(new DesktopAssetHandlerModule(), new DesktopAssetRendererModule(null), new DesktopModule(), new BasicGameModule());

		PlatformLauncher launcher = injector.getInstance(PlatformLauncher.class);
		
		//TODO set language
		StringHandler sh = injector.getInstance(StringHandler.class);

		sh.setString(new EAdString("question"), "¿Qué pregunta te podría hacer?");
		sh.setString(new EAdString("answer1"), "Que como estoy, a lo mejor");
		sh.setString(new EAdString("answer2"), "No sé, lo que veas.");
		sh.setString(new EAdString("answer3"), "A lo mejor algo relacionado con el mundo e las preguntas.");
		sh.setString(new EAdString("stringName"), "Start game");
		sh.setString(new EAdString("panielName"), "Paniel");
		sh.setString(new EAdString("panielDescription"), "Es Paniel. Parece ser que le gusta hacer el moonwalker todo el rato. #f");
		sh.setString(new EAdString("handAction"), "¡Chócala!");
		sh.setString(new EAdString("orientedName"), "Oriented");
		sh.setString(new EAdString("stringId"), "Esto es un string no ya largo, sino larguíiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiisimo sólo para probar si todo ese código que hay por ahí para e incluso le pongo variables como mi nombre que es #nombre e incluso un número que es #n cortar las líneas funciona más o menos bien, que estaría bien, vamos, que tampoco...");

		sh.setString(new EAdString("default"), "default");

		LoadingScreen loadingScreen = injector.getInstance(LoadingScreen.class);
		
		EAdScene scene = BasicSceneFactoryTest.getBasicScene();
		
		EAdActorReference actorReference = ActorReferenceFactoryTest.getActorReference(BasicActorFactoryTest.getActor());
		scene.getSceneElements().add(actorReference);

		EAdActorReference actorReference2 = ActorReferenceFactoryTest.getActorReference(BasicActorFactoryTest.getActor());
		scene.getSceneElements().add(actorReference2);

		EAdActorReference actorReference3 = ActorReferenceFactoryTest.getActorReference(BasicActorFactoryTest.getActor());
		scene.getSceneElements().add(actorReference3);

		EAdActorReference actorReference4 = ActorReferenceFactoryTest.getActorReference(BasicActorFactoryTest.getActor());
		scene.getSceneElements().add(actorReference4);

		EAdActorReference actorReference5 = ActorReferenceFactoryTest.getActorReference(BasicActorFactoryTest.getActor());
		scene.getSceneElements().add(actorReference5);

		EAdActorReference actorReference6 = ActorReferenceFactoryTest.getActorReference(BasicActorFactoryTest.getActor());
		scene.getSceneElements().add(actorReference6);

		EAdActorReference actorReference7 = ActorReferenceFactoryTest.getActorReference(BasicActorFactoryTest.getActor());
		scene.getSceneElements().add(actorReference7);

		EAdButton button = new EAdButton("button");
		button.setUpNewInstance();
		button.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdChangeScene("id", VideoSceneFactoryTest.getVideoScene(), EAdTransition.BASIC));
		button.setPosition(new EAdPositionImpl(200, 200));
		scene.getSceneElements().add(button);
		
		EAdButton button2 = new EAdButton("button");
		button2.setUpNewInstance();
		button2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdModifyActorState("id", actorReference.getReferencedActor(), EAdModifyActorState.Modification.PLACE_IN_INVENTORY));
		button2.setPosition(new EAdPositionImpl(200, 300));
		scene.getSceneElements().add(button2);

		button2 = new EAdButton("button");
		button2.setUpNewInstance();
		button2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdModifyActorState("id", actorReference2.getReferencedActor(), EAdModifyActorState.Modification.PLACE_IN_INVENTORY));
		button2.setPosition(new EAdPositionImpl(220, 320));
		scene.getSceneElements().add(button2);

		button2 = new EAdButton("button");
		button2.setUpNewInstance();
		button2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdModifyActorState("id", actorReference3.getReferencedActor(), EAdModifyActorState.Modification.PLACE_IN_INVENTORY));
		button2.setPosition(new EAdPositionImpl(240, 340));
		scene.getSceneElements().add(button2);

		button2 = new EAdButton("button");
		button2.setUpNewInstance();
		button2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdModifyActorState("id", actorReference4.getReferencedActor(), EAdModifyActorState.Modification.PLACE_IN_INVENTORY));
		button2.setPosition(new EAdPositionImpl(260, 360));
		scene.getSceneElements().add(button2);

		button2 = new EAdButton("button");
		button2.setUpNewInstance();
		button2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdModifyActorState("id", actorReference5.getReferencedActor(), EAdModifyActorState.Modification.PLACE_IN_INVENTORY));
		button2.setPosition(new EAdPositionImpl(280, 380));
		scene.getSceneElements().add(button2);

		button2 = new EAdButton("button");
		button2.setUpNewInstance();
		button2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdModifyActorState("id", actorReference6.getReferencedActor(), EAdModifyActorState.Modification.PLACE_IN_INVENTORY));
		button2.setPosition(new EAdPositionImpl(300, 400));
		scene.getSceneElements().add(button2);

		button2 = new EAdButton("button");
		button2.setUpNewInstance();
		button2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdModifyActorState("id", actorReference7.getReferencedActor(), EAdModifyActorState.Modification.PLACE_IN_INVENTORY));
		button2.setPosition(new EAdPositionImpl(320, 420));
		scene.getSceneElements().add(button2);

		
		EAdButton button3 = new EAdButton("button");
		button3.setUpNewInstance();
		button3.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, new EAdModifyActorState("id", actorReference.getReferencedActor(), EAdModifyActorState.Modification.PLACE_IN_SCENE));
		button3.setPosition(new EAdPositionImpl(200, 400));
		scene.getSceneElements().add(button3);

		
		loadingScreen.setInitialScreen(scene);
		
		//for (EAdTimer timer : scene.timers)
		//	game.getAdventureModel().getChapters().get(0).getTimers().add(timer);

		
		//TODO extract file from args or use default?
		EAdURI file = null;
		//File file = new File("/ProyectoJuegoFINAL.ead");
		((DesktopPlatformLauncher) launcher).launch(file);
	}

}
