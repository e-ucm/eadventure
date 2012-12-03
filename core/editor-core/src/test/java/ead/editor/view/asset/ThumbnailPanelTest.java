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

package ead.editor.view.asset;

import ead.common.resources.assets.drawable.basics.Image;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.EditorNode;
import ead.editor.model.nodes.EngineNode;
import ead.editor.model.nodes.asset.ImageAssetNode;
import ead.editor.view.components.PropertiesTablePanel;
import ead.editor.view.components.ThumbnailPanel;
import ead.engine.core.gdx.assets.GdxAssetHandler;
import ead.utils.Log4jConfig;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Action;
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

		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Info,
				new Object[] {});

		final ArrayList<EditorNode> ians = new ArrayList<EditorNode>();
		for (int i = 0; i < 100; i++) {
			ImageAssetNode ian = new ImageAssetNode(i);
			ian.addChild(new EngineNode<Image>(i + 100, new Image(
					"@drawable/assets_animation_telefono.png")));
			ian.setBase(new File(
					"../../demos/firstaidgame/src/main/resources/",
					GdxAssetHandler.PROJECT_INTERNAL_PATH));
			ians.add(ian);
		}
		final ThumbnailPanel tnp = new ThumbnailPanel();

		JButton jb = new JButton("click to add nodes");
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tnp.setNodes(ians);
			}
		});

		PropertiesTablePanel ptp = new PropertiesTablePanel();
		ptp.setNodes(ians);

		JTabbedPane jtp = new JTabbedPane();
		jtp.add("Icons", tnp);
		jtp.add("Table", ptp);

		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(jtp);
		jf.add(jb, BorderLayout.SOUTH);
		jf.setSize(800, 600);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
}