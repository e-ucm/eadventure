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

package ead.engine.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import ead.common.importer.EAdventure1XImporter;

public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = -2805883406261079968L;
	
	private JProgressBar progressBar;
	
	private JLabel progressText;
	
	private EAdventure1XImporter importer;
	
	private Runnable runnable = new Runnable( ){

		@Override
		public void run() {
			if ( importer.getProgress() < 100 ){
				progressBar.setValue(importer.getProgress());
				progressText.setText(importer.getProgressText());
				invalidate();
				repaint();
				new Thread( runnable ).start();
			}
			else {
				ProgressDialog.this.setVisible(false);
			}
		}
		
	};
	
	public ProgressDialog(JFrame frame, EAdventure1XImporter importer){
		super(frame, "Loading...");
		this.importer = importer;
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(importer.getProgress());
		progressText = new JLabel( "Loading..." );
		JPanel p =new JPanel(new GridBagLayout( ));
		GridBagConstraints c = new GridBagConstraints( );
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets( 5, 5, 5, 5 );
		p.add(progressText, c);
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(progressBar, c);
		this.add(p);
		this.setUndecorated(true);
		pack();
		setLocationRelativeTo(null);
	}

	
	public void setVisible(boolean v ){
		super.setVisible(v);
		if ( v ){
			progressBar.setValue(0);
			progressText.setText("Loading...");
			new Thread( runnable ).start();
		}
		
	}
}
