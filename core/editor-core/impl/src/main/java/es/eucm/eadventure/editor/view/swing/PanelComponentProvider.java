package es.eucm.eadventure.editor.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.ProviderFactory;
import es.eucm.eadventure.editor.view.generics.InterfaceElement;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.gui.EAdBorderedPanel;
import es.eucm.eadventure.gui.EAdScrollPane;

public class PanelComponentProvider implements ComponentProvider<Panel, JPanel> {

	private static final Logger logger = Logger.getLogger("PanelComponentProvider");
	
	private ProviderFactory<JComponent> providerFactory;
	
	public PanelComponentProvider(ProviderFactory<JComponent> providerFactory) {
		this.providerFactory = providerFactory;
	}
	
	//TODO Should support different element positioning policies
	@Override
	public JPanel getComponent(Panel element) {
		JPanel mainPanel;
		if (element.getTitle() != null)
			mainPanel = new EAdBorderedPanel(element.getTitle());
		else
			mainPanel = new JPanel();

		JPanel panel = new JPanel();
		EAdScrollPane scrollPane = new EAdScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		java.awt.GridBagConstraints c = null;
		
		if (element.getLayoutPolicy() == Panel.LayoutPolicy.HORIZONTAL ||
				element.getLayoutPolicy() == Panel.LayoutPolicy.VERTICAL) {
			panel.setLayout(new java.awt.GridBagLayout());
			c = new java.awt.GridBagConstraints();
			c.fill = java.awt.GridBagConstraints.BOTH;
			c.gridx = 0;
			c.weightx = 1.0;
			c.gridy = 0;
			c.weighty = 1.0;
		} else if (element.getLayoutPolicy() == Panel.LayoutPolicy.FLOW)
			panel.setLayout(new java.awt.FlowLayout());
		else
			panel.setLayout(new java.awt.GridLayout(0,1));
		
		for (InterfaceElement newElement : element.getElements()) {
			ComponentProvider<InterfaceElement, JComponent> cp = providerFactory.getProvider(newElement);
			if (cp == null)
				logger.log(Level.SEVERE, "No provider for " + newElement.getClass());
			JComponent component = cp.getComponent(newElement);
			if (component == null)
				logger.log(Level.SEVERE, "No component for " + newElement.getClass() + " with provider " + cp.getClass());
			if (element.getLayoutPolicy() == Panel.LayoutPolicy.VERTICAL) {
				panel.add(component, c);
				c.gridy++;
			} else if (element.getLayoutPolicy() == Panel.LayoutPolicy.HORIZONTAL) {
				panel.add(component, c);
				c.gridx++;
			} 
		}
		
		//mainPanel.doLayout();

		return mainPanel;
	}

}
