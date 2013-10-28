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

package es.eucm.ead.editor.view.panel;

import es.eucm.ead.editor.model.nodes.SceneNode;
import es.eucm.ead.editor.view.generic.OptionPanel;
import es.eucm.ead.editor.view.generic.OptionPanel.LayoutPolicy;
import es.eucm.ead.editor.view.generic.PanelImpl;
import es.eucm.ead.editor.view.generic.TextOption;
import es.eucm.ead.editor.view.generic.table.ListOption;
import es.eucm.ead.editor.view.generic.table.MapOption;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import java.awt.BorderLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An elementPanel that can display anything, in a non-editable fashion.
 *
 * @author mfreire
 */
public class ScenePanel extends AbstractElementPanel<SceneNode> {

	static private Logger logger = LoggerFactory.getLogger(ScenePanel.class);

	private Scene scene;

	@Override
	protected void rebuild() {
		this.scene = (Scene) target.getFirst().getContent();
		removeAll();

		setLayout(new BorderLayout());

		OptionPanel op = new PanelImpl("Scene properties",
				LayoutPolicy.VerticalBlocks, 4);
		op.add(new TextOption("Scene ID", "The unique ID for the scene", scene,
				"id", TextOption.ExpectedLength.SHORT, target.getFirst()));

		op.add(new ListOption<SceneElement>("Elements",
				"Contained sceneElements", scene, "sceneElements",
				SceneElement.class, target.getFirst()) {

			// FIXME: add more cols here

		});
		op.add(new MapOption<Object>("Vars", "Available vars", scene, "vars",
				String.class, target.getFirst()) {

			// FIXME: add cols here
		});

		add(op.getComponent(controller.getCommandManager()),
				BorderLayout.CENTER);

		revalidate();
	}
}
