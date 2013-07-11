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

package ead.editor.view.panel.asset;

import java.awt.BorderLayout;

import javax.swing.SpinnerNumberModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.params.text.EAdString;
import ead.editor.model.nodes.asset.CaptionAssetNode;
import ead.editor.view.generic.BooleanOption;
import ead.editor.view.generic.IntegerOption;
import ead.editor.view.generic.OptionPanel;
import ead.editor.view.generic.PanelImpl;
import ead.editor.view.generic.TextOption;
import ead.editor.view.generic.accessors.MapAccessor;
import ead.editor.view.panel.AbstractElementPanel;

/**
 * A panel that displays all assets, by type. A preview is available
 * on the left-hand side.
 *
 * @author mfreire
 */
public class CaptionAssetPanel extends AbstractElementPanel<CaptionAssetNode> {

	private static final Logger logger = LoggerFactory
			.getLogger("ImageAssetPanel");
	private Caption caption;

	public CaptionAssetPanel() {
		setLayout(new BorderLayout());

		OptionPanel panel = new PanelImpl("CaptionAsset",
				OptionPanel.LayoutPolicy.VerticalBlocks, 4);
		panel.add(new TextOption("Label", "Internationalized label",
				new MapAccessor<EAdString, String>(controller.getModel()
						.getStringHandler(), "strings", caption.getLabel()),
				target));
		panel.add(new BooleanOption("Bubbled",
				"Should the label be shown in a bubble?", caption, "hasBubble",
				target));
		panel
				.add(new IntegerOption("Padding", "Internal padding to apply",
						caption, "padding", target, new SpinnerNumberModel(0,
								0, 32, 1)));

		panels.add(panel);
	}

	@Override
	public void setTarget(CaptionAssetNode target) {
		caption = (Caption) target.getFirst().getContent();
		super.setTarget(target);
	}

	@Override
	protected void rebuild() {
	}
}