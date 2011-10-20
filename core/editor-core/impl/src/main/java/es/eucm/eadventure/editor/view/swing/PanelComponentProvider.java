package es.eucm.eadventure.editor.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

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

		JPanel panel = new ScrollablePanel();
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
				c.weighty = component.getMinimumSize().getHeight();
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
	
	private class ScrollablePanel extends JPanel implements Scrollable {

		private static final long serialVersionUID = -8779328786327371343L;

		public Dimension getPreferredSize() {
			Dimension preferred = super.getPreferredSize();
			Dimension container = super.getParent().getParent().getSize();
			return new Dimension((int) Math.max(preferred.getWidth(), container.getWidth()),
					(int) Math.max(preferred.getHeight(), container.getHeight()));
		}
		
		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return super.getPreferredSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			//TODO check
			return 10;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			//TODO check
			return 1;
		}
		
	}

}
