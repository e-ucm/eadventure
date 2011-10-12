package es.eucm.eadventure.editor.view.swing;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;

import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.ProviderFactory;
import es.eucm.eadventure.editor.view.generics.InterfaceElement;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.gui.EAdBorderedPanel;

public class PanelComponentProvider implements ComponentProvider<Panel, JPanel> {

	private static final Logger logger = Logger.getLogger("PanelComponentProvider");
	
	private Panel element;
	
	private JPanel panel;
	
	private ProviderFactory<JComponent> providerFactory;
	
	public PanelComponentProvider(ProviderFactory<JComponent> providerFactory) {
		this.providerFactory = providerFactory;
	}
	
	@Override
	public JPanel getComponent(Panel element2) {
		this.element = element2;
		if (element.getTitle() != null)
			panel = new EAdBorderedPanel(element.getTitle());
		else
			panel = new JPanel();
		panel.setLayout(new java.awt.FlowLayout());
		//panel.setLayout(new java.awt.GridLayout(0,1));
		for (InterfaceElement newElement : element.getElements()) {
			ComponentProvider<InterfaceElement, JComponent> cp = providerFactory.getProvider(newElement);
			if (cp == null)
				logger.log(Level.SEVERE, "No provider for " + newElement.getClass());
			JComponent component = cp.getComponent(newElement);
			if (component == null)
				logger.log(Level.SEVERE, "No component for " + newElement.getClass() + " with provider " + cp.getClass());
			panel.add(component);
		}

		return panel;
	}

}
