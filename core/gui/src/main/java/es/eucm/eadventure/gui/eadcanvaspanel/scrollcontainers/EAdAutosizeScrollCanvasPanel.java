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

package es.eucm.eadventure.gui.eadcanvaspanel.scrollcontainers;

import java.awt.Rectangle;

import javax.swing.JViewport;

import es.eucm.eadventure.gui.EAdScrollPane;
import es.eucm.eadventure.gui.eadcanvaspanel.EAdCanvasPanel;
import es.eucm.eadventure.gui.extra.EAdBorder;

/**
 * Scroll pane which contains an {@link EAdCanvasPanel} with autoresize
 * 
 * 
 */
public class EAdAutosizeScrollCanvasPanel extends EAdScrollPane implements
		ScrollCanvasPanel {

	private static final long serialVersionUID = 8494120422754011903L;
	
	private EAdCanvasPanel canvas;

	/**
	 * Constructor with a customized {@link EAdCanvasPanel}
	 * @param canvas 
	 */
	public EAdAutosizeScrollCanvasPanel(EAdCanvasPanel canvas) {
		super(canvas);
		this.canvas = canvas;
		this.setBorder(new EAdBorder());
		canvas.setScrollCanvasPanel(this);
	}
	
	/**
	 * Constructor with the default {@link EAdCanvasPanel}
	 */
	public EAdAutosizeScrollCanvasPanel( ) {
		this( new EAdCanvasPanel( ));
	}

	@Override
	public void updateBounds(Rectangle r) {
		JViewport viewport = this.getViewport();
		viewport.scrollRectToVisible(r);
		viewport.invalidate();
		viewport.updateUI();
		viewport.repaint();
	}

	@Override
	public EAdCanvasPanel getCanvas() {
		return canvas;
	}

}
