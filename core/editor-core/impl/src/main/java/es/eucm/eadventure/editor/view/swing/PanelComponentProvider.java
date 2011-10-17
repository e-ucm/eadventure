package es.eucm.eadventure.editor.view.swing;

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
		mainPanel.add(scrollPane);
		
		//panel.setLayout(new java.awt.FlowLayout());
		//panel.setLayout(new java.awt.GridLayout(0,1));
		panel.setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
		c.fill = java.awt.GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1.0;
		
		for (InterfaceElement newElement : element.getElements()) {
			ComponentProvider<InterfaceElement, JComponent> cp = providerFactory.getProvider(newElement);
			if (cp == null)
				logger.log(Level.SEVERE, "No provider for " + newElement.getClass());
			JComponent component = cp.getComponent(newElement);
			if (component == null)
				logger.log(Level.SEVERE, "No component for " + newElement.getClass() + " with provider " + cp.getClass());
			panel.add(component, c);
			c.gridy++;
		}
		
		mainPanel.doLayout();

		return mainPanel;
	}

}
