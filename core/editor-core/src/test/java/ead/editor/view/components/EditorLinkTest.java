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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.view.components;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import org.junit.BeforeClass;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.editor.EditorGuiceModule;
import ead.engine.core.gdx.desktop.platform.GdxDesktopModule;
import ead.importer.BaseImporterModule;
import es.eucm.ead.tools.java.JavaToolsModule;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.java.utils.Log4jConfig;

/**
 *
 * @author mfreire
 */
public class EditorLinkTest {

	public EditorLinkTest() {
	}

	@BeforeClass
	public static void setUpClass() {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Info, new Object[] {
				"ModelVisitorDriver", Log4jConfig.Slf4jLevel.Info,
				"EditorModel", Log4jConfig.Slf4jLevel.Debug, "EditorAnnotator",
				Log4jConfig.Slf4jLevel.Debug, "EAdventureImporter",
				Log4jConfig.Slf4jLevel.Debug, "ActorFactory",
				Log4jConfig.Slf4jLevel.Debug, });

		Injector injector = Guice.createInjector(new BaseImporterModule(),
				new GdxDesktopModule(), new EditorGuiceModule(),
				new JavaToolsModule());

		// init reflection
		ReflectionClassLoader.init(injector
				.getInstance(ReflectionClassLoader.class));

	}

	public static void main(String[] args) {

		Component[] c = new Component[] {
		//new EditorLink("hi", "hullo", )
		};

		JFrame jf = new JFrame();
		jf.setLayout(new FlowLayout());
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(800, 600);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
}
