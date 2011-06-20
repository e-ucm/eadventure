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

package es.eucm.eadventure.plugin.box2d;

import java.io.File;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdMacroImpl;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.test.BasicSceneFactoryTest;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.params.EAdPosition.Corner;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;
import es.eucm.eadventure.engine.core.impl.LoadingScreen;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.GameObjectFactoryMapProvider;
import es.eucm.eadventure.engine.core.impl.modules.BasicGameModule;
import es.eucm.eadventure.engine.core.platform.PlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.DesktopPlatformLauncher;
import es.eucm.eadventure.engine.core.platform.impl.DesktopStringLoader;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetHandlerModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopAssetRendererModule;
import es.eucm.eadventure.engine.core.platform.impl.extra.DesktopModule;

@Singleton
public class TestBox2DPlatformLauncher {

	public static void main(String[] args) {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
				"eAdventure");
		GameObjectFactoryMapProvider.add(Box2DEffect.class, Box2DGO.class);

		Injector injector = Guice.createInjector(
				new DesktopAssetHandlerModule(),
				new DesktopAssetRendererModule(null), new DesktopModule(),
				new BasicGameModule());

		PlatformLauncher launcher = injector
				.getInstance(PlatformLauncher.class);

		// TODO set language
		StringHandler sh = injector.getInstance(StringHandler.class);
		DesktopStringLoader.loadStrings(sh, ClassLoader
				.getSystemResourceAsStream("values/strings.properties"));

		sh.addString(new EAdString("question"),
				"¿Qué pregunta te podría hacer?");
		sh.addString(new EAdString("answer1"), "Que como estoy, a lo mejor");
		sh.addString(new EAdString("answer2"), "No sé, lo que veas.");
		sh.addString(new EAdString("answer3"),
				"A lo mejor algo relacionado con el mundo e las preguntas.");
		sh.addString(new EAdString("stringName"), "Start game");
		sh.addString(new EAdString("panielName"), "Paniel");
		sh.addString(new EAdString("panielDescription"),
				"Es Paniel. Parece ser que le gusta hacer el moonwalker todo el rato. #f");
		sh.addString(new EAdString("handAction"), "¡Chócala!");
		sh.addString(new EAdString("orientedName"), "Oriented");
		sh.addString(
				new EAdString("stringId"),
				"Esto es un string no ya largo, sino larguíiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiisimo sólo para probar si todo ese código que hay por ahí para e incluso le pongo variables como mi nombre que es #nombre e incluso un número que es #n cortar las líneas funciona más o menos bien, que estaría bien, vamos, que tampoco...");

		LoadingScreen loadingScreen = injector.getInstance(LoadingScreen.class);

		EAdScene scene = BasicSceneFactoryTest.getBasicScene();

		EAdBasicSceneElement ground = new EAdBasicSceneElement("ground");

		RectangleShape rectangle = new RectangleShape(600, 100,
				EAdBorderedColor.BLACK_ON_WHITE);
		ground.getResources().addAsset(ground.getInitialBundle(),
				EAdBasicSceneElement.appearance, rectangle);
		
		EAdBundleId bundle = new EAdBundleId("bundle");
		ground.getResources().addBundle(bundle);
		RectangleShape rectangle3 = new RectangleShape(600, 100,
				EAdBorderedColor.WHITE_ON_BLACK);
		ground.getResources().addAsset(bundle,
				EAdBasicSceneElement.appearance, rectangle3);

		ground.setPosition(new EAdPosition(Corner.CENTER, 400, 550));

		RectangleShape rectangle2 = new RectangleShape(15, 25,
				EAdBorderedColor.BLACK_ON_WHITE);

		scene.getSceneElements().add(ground);

		Box2DEffect effect = new Box2DEffect("box2D");

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 3; j++) {
				EAdBasicSceneElement box = new EAdBasicSceneElement("box");
				box.getResources().addAsset(box.getInitialBundle(),
						EAdBasicSceneElement.appearance, rectangle2);
				box.setPosition(new EAdPosition(Corner.CENTER, 100 + i * 50, i * 50 + j * 50));
				scene.getSceneElements().add(box);
				effect.box.add(box);
			}
		}
		
		EAdChangeAppearance change = new EAdChangeAppearance( "change" );
		change.setElement(ground);
		change.setBundleId(bundle);
		
		EAdChangeAppearance change2 = new EAdChangeAppearance( "change" );
		change2.setElement(ground);
		change2.setBundleId(ground.getInitialBundle());
		
		ground.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, change);
		ground.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, change2);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl("added ");
		EAdMacro macro = new EAdMacroImpl( "macro");

		effect.ground = ground;
		
		macro.getEffects().add(effect);

		event.addEffect(SceneElementEvent.ADDED_TO_SCENE, new EAdTriggerMacro( macro ));
		ground.getEvents().add(event);

		loadingScreen.setInitialScreen(scene);

		// for (EAdTimer timer : scene.timers)
		// game.getAdventureModel().getChapters().get(0).getTimers().add(timer);

		// TODO extract file from args or use default?
		File file = null;
		// File file = new File("/ProyectoJuegoFINAL.ead");
		((DesktopPlatformLauncher) launcher).launch(file);
	}

}
