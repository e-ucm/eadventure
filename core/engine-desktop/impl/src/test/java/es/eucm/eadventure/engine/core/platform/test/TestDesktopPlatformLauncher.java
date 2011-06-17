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

import java.io.File;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.test.ActorReferenceFactoryTest;
import es.eucm.eadventure.common.model.elements.test.BasicActorFactoryTest;
import es.eucm.eadventure.common.model.elements.test.BasicSceneFactoryTest;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.Game;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.DesktopPlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.DesktopStringLoader;
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
		DesktopStringLoader.loadStrings(sh, ClassLoader.getSystemResourceAsStream("values/strings.properties"));

		sh.addString(new EAdString("question"), "¿Qué pregunta te podría hacer?");
		sh.addString(new EAdString("answer1"), "Que como estoy, a lo mejor");
		sh.addString(new EAdString("answer2"), "No sé, lo que veas.");
		sh.addString(new EAdString("answer3"), "A lo mejor algo relacionado con el mundo e las preguntas.");
		sh.addString(new EAdString("stringName"), "Start game");
		sh.addString(new EAdString("panielName"), "Paniel");
		sh.addString(new EAdString("panielDescription"), "Es Paniel. Parece ser que le gusta hacer el moonwalker todo el rato. #f");
		sh.addString(new EAdString("handAction"), "¡Chócala!");
		sh.addString(new EAdString("orientedName"), "Oriented");
		sh.addString(new EAdString("stringId"), "Esto es un string no ya largo, sino larguíiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiisimo sólo para probar si todo ese código que hay por ahí para e incluso le pongo variables como mi nombre que es #nombre e incluso un número que es #n cortar las líneas funciona más o menos bien, que estaría bien, vamos, que tampoco...");

		Game game = injector.getInstance(Game.class);
		LoadingScreen loadingScreen = injector.getInstance(LoadingScreen.class);
		
		EAdScene scene = BasicSceneFactoryTest.getBasicScene();
		
		EAdActorReference actorReference = ActorReferenceFactoryTest.getActorReference(BasicActorFactoryTest.getActor());
		scene.getSceneElements().add(actorReference);
		
		loadingScreen.setInitialScreen(scene);
		
		//for (EAdTimer timer : scene.timers)
		//	game.getAdventureModel().getChapters().get(0).getTimers().add(timer);

		
		//TODO extract file from args or use default?
		File file = null;
		//File file = new File("/ProyectoJuegoFINAL.ead");
		((DesktopPlatformLauncher) launcher).launch(file);
	}

}
