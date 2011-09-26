package es.eucm.eadventure.editor.view.swing;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.ProviderFactory;
import es.eucm.eadventure.editor.view.generics.InterfaceElement;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.gui.EAdTitlePanel;

public class PanelComponentProvider implements ComponentProvider<Panel, JPanel> {

	private Panel element;
	
	private JPanel panel;
	
	private ProviderFactory<JComponent> providerFactory;
	
	public PanelComponentProvider(ProviderFactory<JComponent> providerFactory) {
		this.providerFactory = providerFactory;
	}
	
	@Override
	public void setElement(Panel element2) {
		this.element = element2;
		if (element.getTitle() != null)
			panel = new EAdTitlePanel(element.getTitle());
		else
			panel = new JPanel();
		panel.setLayout(new FlowLayout());
		for (InterfaceElement newElement : element.getElements()) {
			ComponentProvider<InterfaceElement, JComponent> cp = providerFactory.getProvider(newElement);
			cp.setElement(newElement);
			panel.add(cp.getComponent());
		}
	}

	@Override
	public JPanel getComponent() {
		return panel;
	}

}
