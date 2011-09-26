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

package es.eucm.eadventure.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import es.eucm.eadventure.gui.extra.EAdModalPanel;

public class InterfaceTest extends EAdFrame {

	private static final long serialVersionUID = -6461650044958496901L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings", "on");
	     new InterfaceTest();
	}
	
    public InterfaceTest() {
        setSize( 400,400 );

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        
        EAdBorderedPanel panel = new EAdBorderedPanel("panel");
        panel.setLayout(new FlowLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        
        JButton button = new JButton("BIG");
        panel.add(button);
        JButton buttonsmall = new JButton("small");
        panel.add(buttonsmall);

        JButton modal = new JButton("modal");
        modal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addModalPanel(new ModalPanelTest(InterfaceTest.this));
			}
        	
        });
        panel.add(modal);

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
        radioButton2.setBorderPainted(true);
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

        EAdTextField textField5 = new EAdTextField("name", 15);
        panel.add(textField5);
        EAdTextField textField6 = new EAdTextField("description", 15);
        textField6.setEnabled(false);
        panel.add(textField6);

        
        JToggleButton toggleButton = new JToggleButton("toggle button");
        panel.add(toggleButton);
        JToggleButton toggleButton2 = new JToggleButton("toggle button");
        toggleButton2.setEnabled(false);
        panel.add(toggleButton2);
        
        JComboBox comboBox = new JComboBox(new String[] {"value1","value2","value3","value4","value5","value6","value7","value8","value9","value10","value11","value12"});
        panel.add(comboBox);

        JComboBox comboBox1 = new JComboBox(new String[] {"value1","value2","value3","value4","value5","value6","value7","value8","value9","value10","value11","value12"});
        comboBox1.setEnabled(false);
        panel.add(comboBox1);

        JComboBox comboBox2 = new JComboBox();
        panel.add(comboBox2);
        
        JComboBox comboBox3 = new JComboBox();
        comboBox3.setToolTipText("test");
        comboBox3.setEditable(true);
        panel.add(comboBox3);

        EAdSimpleButton simpleButton = new EAdSimpleButton(EAdSimpleButton.SimpleButton.REDO);
        panel.add(simpleButton);

        SpinnerModel model = new SpinnerNumberModel(0, 0, 10, 1);
        JSpinner spinner = new JSpinner(model);
        panel.add(spinner);
        
        
        EAdTabbedPane pane = new EAdTabbedPane();
        JPanel tab1 = new JPanel();
        pane.addTab("tab1", tab1);
        
        JPanel tab2 = new JPanel();
        pane.addTab("tab2", tab2);

        mainPanel.add(pane, BorderLayout.SOUTH);
                
        EAdMenu menu = new EAdMenu("File");
        EAdMenuItem menuItem = new EAdMenuItem("test");
        menu.add(menuItem);
        EAdMenuBar menuBar = new EAdMenuBar();
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        setVisible( true );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
    }

    
    private class ModalPanelTest extends EAdModalPanel {
    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = -2738854052077187121L;
		
		private JButton b;
    	
    	public ModalPanelTest(final EAdFrame frame) {
    		super();
    		
    		JPanel test = new JPanel();
    		b = new JButton("hola");
    		test.add(b);
    		b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.addModalPanel(new ModalPanelTest2(frame));
				}
    		});
    		EAdButton b = new EAdButton("adios");
    		b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.removeModalPanel();
				}
    		});
    		test.add(b);
    	       JComboBox emptyComboTest = new JComboBox(new String[]{"test1", "test2"});
    	       emptyComboTest.setEditable(false);
    	       test.add( emptyComboTest );

    	       EAdTextField temp = new EAdTextField(10);
    	       
    		temp.setToolTipText("Test");
    		test.add(temp);
    		temp.setPreferredSize(new Dimension(200,200));
    		
    		EAdTabbedPane tabbedPane = new EAdTabbedPane();
    		tabbedPane.addTab("TEST", test);
    		add(tabbedPane);
    	}
    	
		@Override
		public Component getFocusComponent() {
			return b;
		}

    }

    private class ModalPanelTest2 extends EAdModalPanel {
    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = -2738854052077187121L;
		
		private JButton b;
    	
    	public ModalPanelTest2(final EAdFrame frame) {
    		super();
    		
    		JPanel test = new JPanel();
    		b = new JButton("hola");
    		test.add(b);
    		b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.removeModalPanel();
				}
    		});
    		test.add(b);
    		
    		add(test);
    	}
    	
		@Override
		public Component getFocusComponent() {
			return b;
		}

    }

}
