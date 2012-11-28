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

import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.EngineNode;
import ead.editor.view.components.ThumbnailPanel;
import ead.utils.Log4jConfig;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

		ArrayList<DependencyNode> en = new ArrayList<DependencyNode>();
		for (int i = 0; i < 100; i++) {
			en.add(new EngineNode<String>(i, "" + i));
		}
		BufferedImage bi = (BufferedImage) en.get(0).getThumbnail();
		Graphics g = bi.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		g.setColor(Color.black);
		g.drawRect(1, 1, bi.getWidth() - 2, bi.getHeight() - 2);
		g.drawRect(6, 6, bi.getWidth() - 12, bi.getHeight() - 12);
		ThumbnailPanel tnp = new ThumbnailPanel();
		tnp.setNodes(en);

		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(tnp);
		jf.setSize(800, 600);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
}