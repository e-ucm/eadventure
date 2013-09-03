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

import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.editor.model.nodes.CharacterNode;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An elementPanel that can display anything, in a non-editable fashion.
 *
 * @author mfreire
 */
public class ActorPanel extends AbstractElementPanel<CharacterNode> {

	static private Logger logger = LoggerFactory.getLogger(ActorPanel.class);

	private SceneElementDef actor;

	@Override
	protected void rebuild() {
		this.actor = (SceneElementDef) target.getFirst().getContent();
		removeAll();
		setLayout(new FlowLayout());
		add(new JLabel("This is an actor panel for ID " + actor.getId()));
		add(new JSeparator(JSeparator.HORIZONTAL));
		add(new JLabel("This actor has desc= "
				+ actor.getVars().get(SceneElementDef.VAR_DOC_DESC)));
		add(new JSeparator(JSeparator.HORIZONTAL));
		add(new JLabel("This actor has detailDesc= "
				+ actor.getVars().get(SceneElementDef.VAR_DOC_DETAILED_DESC)));
		add(new JSeparator(JSeparator.HORIZONTAL));

		actor.getAppearance();

		revalidate();
	}
}