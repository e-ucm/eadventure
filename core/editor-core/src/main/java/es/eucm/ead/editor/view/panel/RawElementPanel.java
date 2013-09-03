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

import es.eucm.ead.editor.model.nodes.DependencyNode;
import es.eucm.ead.editor.view.components.CheapVerticalLayout;
import es.eucm.ead.editor.view.components.EditorLinkFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class RawElementPanel extends AbstractElementPanel<DependencyNode> {

	static private Logger logger = LoggerFactory
			.getLogger(RawElementPanel.class);

	private JPanel inner = new JPanel();

	private JPanel startRow(JPanel container) {
		logger.debug("   -- new row --");
		JPanel jp = new JPanel();
		//			jp.setBackground(container.getComponentCount()%2 == 0? Color.lightGray : Color.lightGray.brighter());
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT, 0, 0);
		fl.setAlignOnBaseline(true);
		jp.setLayout(fl);
		container.add(jp);
		return jp;
	}

	private void addLabelToRow(JPanel row, String text) {
		logger.debug("   appending label: " + text);
		JLabel jl = new JLabel(text);
		//			jl.setBackground(Math.random() > 0.5?new Color(0xbbffbbff):new Color(0xffbbbbff));
		//			jl.setOpaque(true);
		Dimension d = new Dimension(jl.getPreferredSize());
		FontMetrics fm = jl.getFontMetrics(jl.getFont());
		d.setSize(d.width, fm.getMaxAscent() + fm.getMaxDescent()
				+ fm.getLeading());
		jl.setPreferredSize(d);
		jl.setMinimumSize(d);
		jl.setMaximumSize(d);
		row.add(jl);
	}

	@Override
	protected void rebuild() {
		removeAll();
		setLayout(new BorderLayout());
		inner = new JPanel();
		inner.setLayout(new CheapVerticalLayout());
		JScrollPane jsp = new JScrollPane(inner,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.getVerticalScrollBar().setUnitIncrement(16);
		add(jsp, BorderLayout.CENTER);

		String st = target.getTextualDescription(controller.getModel());
		logger.debug("preparing to render\n" + st);

		Pattern p = Pattern.compile("[(]([0-9]+)[)]|([\n]+)");
		Matcher m = p.matcher(st);
		int offset = 0;
		JPanel row = startRow(inner);
		while (m.find()) {
			if (m.group(1) == null) {
				addLabelToRow(row, st.substring(offset, m.start()));
				row = startRow(inner);
			} else {
				addLabelToRow(row, st.substring(offset, m.start()));
				String id = st.substring(m.start(1), m.end(1));
				row.add(EditorLinkFactory.createLink(Integer.parseInt(id),
						controller));
			}
			offset = m.end();
		}
		addLabelToRow(row, st.substring(offset));

		revalidate();
	}
}