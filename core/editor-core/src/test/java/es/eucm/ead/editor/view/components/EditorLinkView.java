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

import es.eucm.ead.editor.view.components.EditorLink;
import es.eucm.ead.editor.view.components.EditorLinkFactory;
import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.model.assets.drawable.basics.shapes.BezierShape;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.assets.multimedia.Video;
import es.eucm.ead.editor.model.nodes.EngineNode;
import es.eucm.ead.editor.model.nodes.QueryNode;
import es.eucm.ead.editor.model.nodes.asset.AssetNode;
import es.eucm.ead.editor.model.nodes.asset.ImageAssetNode;
import es.eucm.ead.editor.model.nodes.asset.VideoAssetNode;
import es.eucm.ead.tools.java.utils.Log4jConfig;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A test for EditorLink icon views.
 * @author mfreire
 */
public class EditorLinkView extends JPanel {

	public static void main(String[] args) {

		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Info,
				new Object[] {});

		ArrayList<EditorLink> els = new ArrayList<EditorLink>();

		AssetDescriptor[] descriptors = new AssetDescriptor[] {
				new FramesAnimation(), new RectangleShape(), new BezierShape(),
				new Caption("Hi there!"), };
		for (AssetDescriptor a : descriptors) {
			AssetNode an = new AssetNode(els.size());
			an.addChild(new EngineNode<AssetDescriptor>(100 + els.size(), a));
			els.add(EditorLinkFactory.createLink(an, null));
		}
		{
			ImageAssetNode an = new ImageAssetNode(els.size());
			an.addChild(new EngineNode<AssetDescriptor>(100 + els.size(),
					new Image("@drawable/assets_image_vervideo.png")));
			els.add(EditorLinkFactory.createLink(an, null));
		}
		{
			VideoAssetNode an = new VideoAssetNode(els.size());
			an.addChild(new EngineNode<AssetDescriptor>(100 + els.size(),
					new Video("@binary/assets_video_IniciarTos.AVIXVID.webm")));
			els.add(EditorLinkFactory.createLink(an, null));
		}
		els.add(EditorLinkFactory.createLink(new QueryNode(els.size()), null));
		els.add(EditorLinkFactory.createLink(new EngineNode<Object>(els.size(),
				"guess not!"), null));

		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4,
						4, 4, 4), 0, 0);
		for (EditorLink el : els) {
			gbc.gridy++;
			jf.add(el, gbc);
		}
		jf.setSize(150, 300);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
}