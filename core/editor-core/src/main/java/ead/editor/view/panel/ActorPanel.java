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

package ead.editor.view.panel;

import ead.common.model.elements.scenes.SceneElementDef;
import ead.editor.model.ActorNode;
import ead.editor.model.DependencyNode;
import ead.editor.view.EditorWindow;
import ead.editor.view.dock.ElementPanel;
import ead.editor.view.impl.CheapVerticalLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An elementPanel that can display anything, in a non-editable fashion.
 *
 * @author mfreire
 */
public class ActorPanel extends JPanel implements ElementPanel<ActorNode> {

	private static final Logger logger = LoggerFactory.getLogger("ActorPanel");

	private ActorNode target;
	private EditorWindow ew;
    private SceneElementDef actor;

	@Override
	public void setTarget(ActorNode target) {
		this.target = target;
        this.actor = (SceneElementDef)target.getContents().iterator().next().getContent();
		rebuild();
	}

	@Override
	public ActorNode getTarget() {
		return target;
	}

	private void rebuild() {
		removeAll();
		setLayout(new FlowLayout());
        add(new JLabel("This is an actor panel for ID " + actor.getId()));
        add(new JLabel("This actor has desc= " + actor.getDesc()));
        add(new JLabel("This actor has detailDesc= " + actor.getDetailDesc()));
        add(new JLabel("This actor has " + actor.getActions().size() + " actions"));

		revalidate();
	}

	private String htmlize(String s) {
		return "<html>" + s + "</html>";
	}

	@Override
	public void setEditor(EditorWindow ew) {
		this.ew = ew;
	}
}