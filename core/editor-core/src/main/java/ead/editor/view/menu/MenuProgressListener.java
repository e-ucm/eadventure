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

package ead.editor.view.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.editor.control.Controller;
import ead.editor.model.EditorModel;
import ead.utils.swing.SwingUtilities;

/**
 *
 * @author mfreire
 */
public class MenuProgressListener extends JPanel implements EditorModel.ModelProgressListener, Runnable {

	private static final Logger logger = LoggerFactory.getLogger(
			MenuProgressListener.class);

	private JProgressBar jpb;
	private JLabel jl;
	private int oldProgress = 0;
	private Runnable r;
	private Controller controller;

	public MenuProgressListener(Controller controller, Runnable r) {
		this.r = r;
		this.controller = controller;
	}

	public void runInEDT() {
		SwingUtilities.doInEDTNow(this);
	}

	/**
	 * Should only be run in EDT
	 */
	@Override
	public void run() {
		setLayout(new BorderLayout());

		jpb = new JProgressBar(0, 100);
		jpb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(jpb, BorderLayout.CENTER);

		jl = new JLabel("hi there", JLabel.CENTER);
		jl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(jl, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(400, 100));

		controller.getViewController().addModalPanel(this);
		controller.getModel().addProgressListener(this);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// business
				try {
					r.run();
				} catch (Exception e) {
					SwingUtilities.showExceptionDialog(e);
				}
				// cleanup
				SwingUtilities.doInEDT(new Runnable() {
					@Override
					public void run() {
						controller.getModel().removeProgressListener(
								MenuProgressListener.this);
						controller.getViewController().removeModalPanel(true);
					}
				});
			}
		}).start();
	}

	@Override
	public void update(final int progress, final String text) {
		logger.info("FileMenu update: {} {} ", new Object[]{"" + progress, text});

		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				jl.setText(text);
				if (oldProgress > progress) {
					jpb.setIndeterminate(true);
				} else if (jpb.isIndeterminate()) {
					jpb.setIndeterminate(false);
					jpb.setValue(oldProgress);
					jpb.setValue(progress);
				} else {
					jpb.setValue(progress);
				}
				oldProgress = progress;
			}
		});
	}
}
