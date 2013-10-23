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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.control.commands;

import com.google.inject.Guice;
import com.google.inject.Injector;
import es.eucm.ead.editor.EditorGuiceModule;
import es.eucm.ead.editor.control.CommandManager;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.ControllerImpl;
import es.eucm.ead.editor.model.EditorModelImpl;
import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.util.Log4jConfig;
import es.eucm.ead.engine.desktop.platform.DesktopModule;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.tools.java.JavaToolsModule;
import java.util.ArrayList;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mfreire
 */
public class MapCommandTest {

	public MapCommandTest() {
	}

	private static EditorModelImpl em;
	private static CommandManager cm;
	private static Controller controller;
	private static EAdMap<String, String> m1;
	private static EAdMap<ArrayList<String>, ArrayList<String>> m2;
	private static ArrayList<ArrayList<String>> objects;
	private static DependencyNode root;

	@BeforeClass
	public static void setUpClass() {
		Injector injector = Guice.createInjector(new DesktopModule(),
				new EditorGuiceModule(), new JavaToolsModule());
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug);
		controller = injector.getInstance(ControllerImpl.class);
		controller.initialize();
		em = (EditorModelImpl) controller.getModel();
		cm = controller.getCommandManager();

		m1 = new EAdMap<String, String>();
		for (int i = 0; i < 10; i++) {
			m1.put("key" + i, "value" + i);
		}

		objects = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < 2 * 2; i++) {
			ArrayList<String> al = new ArrayList<String>();
			for (int j = 0; j < 2; j++) {
				al.add("o" + i + "_" + j);
			}
			objects.add(al);
		}
		m2 = new EAdMap<ArrayList<String>, ArrayList<String>>();
		for (int i = 0; i < objects.size(); i += 2) {
			m2.put(objects.get(i), objects.get(i + 1));
		}
		Identified root = new BasicElement("root");
		DependencyNode<String> rootNode = em.addNode(null, "root", root, false);

	}

	@After
	public void tearDown() {
		while (cm.canUndo()) {
			cm.undoCommand();
		}
	}

	/**
	 * Test of put method, of class MapCommand.
	 */
	@Test
	public void testPut() {
		System.out.println("put");
		MapCommand<String, String> a;
		assert (!cm.canUndo());
		assert (!cm.canRedo());

		String key = "key11";
		String value1 = "value11";
		a = new MapCommand.AddToMap<String, String>(m1, value1, key, root);
		cm.performCommand(a);
		assert (m1.get(key).equals(value1));

		System.out.println("put");
		String value2 = "value12";
		a = new MapCommand.AddToMap<String, String>(m1, value2, key, root);
		cm.performCommand(a);
		assert (m1.get(key).equals(value2));

		assert (cm.canUndo());
		cm.undoCommand();
		assert (cm.canRedo());
		assert (m1.get(key).equals(value1));
		cm.redoCommand();
		assert (m1.get(key).equals(value2));
		assert (!cm.canRedo());
	}

	/**
	 * Test of remove method, of class MapCommand.
	 */
	@Test
	public void testRemove() {
		System.out.println("remove");

		MapCommand<String, String> a;
		for (int i = 0; i < 4; i++) {
			a = new MapCommand.RemoveFromMap<String, String>(m1, "key" + i,
					root);
			cm.performCommand(a);
			assert (!m1.containsKey("key" + i));
		}
		for (int i = 0; i < 4; i++) {
			cm.undoCommand();
			assert (m1.containsKey("key" + (4 - i - 1)));
		}
		assert (!cm.canUndo());
		for (int i = 0; i < 4; i++) {
			cm.redoCommand();
			assert (!m1.containsKey("key" + i));
		}
	}
}
