/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

package es.eucm.ead.editor.view.components;

import com.google.inject.Guice;
import com.google.inject.Injector;
import es.eucm.ead.editor.EditorGuiceModule;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.model.EditorModelImpl;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.editor.model.nodes.EditorNode;
import es.eucm.ead.editor.model.nodes.asset.ImageAssetNode;
import es.eucm.ead.engine.assets.AssetHandlerImpl;
import es.eucm.ead.editor.util.Log4jConfig;
import es.eucm.ead.engine.desktop.platform.DesktopModule;
import es.eucm.ead.engine.desktop.utils.assetviewer.AssetViewer;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.tools.java.JavaToolsModule;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * A panel that displays thumbnails for a number of elements.
 * Element thumbnails can be selected (all or some), and have, as titles, the
 * EditorLinks.
 * @author mfreire
 */
public class ThumbnailPanelTest extends JPanel {

	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Info);

		// initialize launcher & reflection
		Injector injector = Guice.createInjector(new DesktopModule(),
				new EditorGuiceModule(), new JavaToolsModule());
		ReflectionClassLoader.init(injector
				.getInstance(ReflectionClassLoader.class));
		Controller c = injector.getInstance(Controller.class);
		EditorModelImpl mi = (EditorModelImpl) c.getModel();
		mi.getLoader().setSaveDir(new File("src/main/resources/"));

		final AssetViewer rootAssetViewer = c.createAssetViewer();

		final ArrayList<EditorNode> ians = new ArrayList<EditorNode>();
		Identified root = new BasicElement();
		root.setId("fakeRoot");
		mi.addNode(null, null, root, false);
		for (int i = 0; i < 100; i++) {
			ImageAssetNode ian = new ImageAssetNode(i);
			Image image = new Image("@drawable/EditorIcon128x128.png");
			image.setId("id" + i);
			mi.addNode(mi.getNodeFor(root), "mock", image, false);
			ian.addChild(mi.getNodeFor(image));
			ian.setBase(new File("src/main/resources/",
					AssetHandlerImpl.PROJECT_INTERNAL_PATH));
			ians.add(ian);
			mi.registerEditorNodeWithGraph(ian);
		}
		final ThumbnailPanel tnp = new ThumbnailPanel();

		JButton jb = new JButton("click to add nodes");
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tnp.setNodes(ians);
			}
		});
		JButton jbg = new JButton("grab a capture");
		jbg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Image i = (Image) ians.get(0).getFirst().getContent();
				System.err.println("Setting drawable to something: " + i + " "
						+ i.getUri());
				rootAssetViewer.setDrawable(i);
			}
		});

		PropertiesTablePanel ptp = new PropertiesTablePanel();
		ptp.setNodes(ians);

		ptp.setController(c);

		JTabbedPane jtp = new JTabbedPane();
		jtp.add("Icons", tnp);
		jtp.add("Table", ptp);

		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(jtp);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(jb);
		buttonPanel.add(jbg);
		jf.add(buttonPanel, BorderLayout.SOUTH);
		jf.setSize(800, 600);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		jtp.add("assetViewer", rootAssetViewer.getCanvas());

	}
}