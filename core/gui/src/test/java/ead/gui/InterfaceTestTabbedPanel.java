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

package ead.gui;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import ead.gui.EAdButton;
import ead.gui.EAdFrame;
import ead.gui.EAdTabbedPane;
import ead.gui.EAdTextField;

public class InterfaceTestTabbedPanel extends EAdFrame {

	private static final long serialVersionUID = -9031332616177811227L;

	public static void main(String[] args) {
		new InterfaceTestTabbedPanel();
	}
	
	public InterfaceTestTabbedPanel() {
        setSize( 400,400 );

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1));
        
        JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP);
        
        JPanel tab1 = new JPanel();
        pane.addTab("tab1", tab1);
        fillPanel(tab1);
        
        JPanel tab2 = new JPanel();
        pane.addTab("tab2", tab2);

        panel.add(pane);
        
        EAdTabbedPane pane2 = new EAdTabbedPane(JTabbedPane.LEFT);
        JPanel tab12 = new JPanel();
        pane2.addTab("tab1", tab12);
        fillPanel(tab12);
        
        JPanel tab22 = new JPanel();
        pane2.addTab("tab2", tab22);
        
        panel.add(pane2);
        
        add(panel);
        
        setVisible( true );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
	}
	
	private void fillPanel(JPanel panel) {
        EAdButton button = new EAdButton("button");
        panel.add(button);
        EAdButton button2 = new EAdButton("button2");
        button2.setEnabled(false);
        panel.add(button2);
        JCheckBox checkBox = new JCheckBox("checkbox");
        panel.add(checkBox);
        JCheckBox checkBox2 = new JCheckBox("checkbox2");
        checkBox2.setEnabled(false);
        panel.add(checkBox2);
        JRadioButton radioButton = new JRadioButton("radiobutton");
        panel.add(radioButton);
        JRadioButton radioButton2 = new JRadioButton("radiobutton2");
        radioButton2.setEnabled(false);
        panel.add(radioButton2);

        EAdTextField textField = new EAdTextField(15);
        panel.add(textField);
        EAdTextField textField2 = new EAdTextField(15);
        textField2.setEnabled(false);
        panel.add(textField2);
        EAdTextField textField3 = new EAdTextField(15);
        textField3.setToolTipText("test text");
        panel.add(textField3);
        EAdTextField textField4 = new EAdTextField(15);
        textField4.setToolTipText("test text");
        textField4.setEnabled(false);
        panel.add(textField4);

	}
}
