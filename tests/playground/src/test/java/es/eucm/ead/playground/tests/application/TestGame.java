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

package es.eucm.ead.playground.tests.application;

import com.badlogic.gdx.ApplicationListener;
import es.eucm.ead.engine.EAdEngine;
import es.eucm.ead.engine.desktop.DesktopGame;
import es.eucm.ead.engine.desktop.platform.DesktopGUI;
import es.eucm.ead.engine.factories.EffectFactory;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.model.elements.effects.text.SpeakEf;
import es.eucm.ead.playground.tests.application.gameobjects.TestSpeakGO;
import es.eucm.ead.tools.java.reflection.JavaReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;

import javax.swing.*;

public class TestGame extends DesktopGame {
	public JFrame start() {
		initInjector();
		// Init class loader
		ReflectionClassLoader.init(new JavaReflectionClassLoader());
		// Prepare Gdx configuration
		int width = windowWidth;
		int height = windowHeight;
		DesktopGUI gui = (DesktopGUI) injector.getInstance(GUI.class);
		final EAdEngine engine = (EAdEngine) injector
				.getInstance(ApplicationListener.class);
		engine.setDebug(debug);
		EffectFactory effectFactory = this.injector
				.getInstance(EffectFactory.class);
		effectFactory.put(SpeakEf.class, TestSpeakGO.class);

		TestApplication test = new TestApplication(engine, width, height);
		test.run();

		return null;
	}
}
