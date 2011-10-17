package es.eucm.eadventure.editor.view.swing.scene;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.SceneInterfaceElement;
import es.eucm.eadventure.gui.eadcanvaspanel.EAdCanvasPanel;
import es.eucm.eadventure.gui.eadcanvaspanel.listeners.DragListener;
import es.eucm.eadventure.gui.eadcanvaspanel.listeners.ResizeListener;
import es.eucm.eadventure.gui.eadcanvaspanel.scrollcontainers.EAdFixScrollCanvasPanel;

public class SceneEditionComponentProvider implements ComponentProvider<SceneInterfaceElement, JComponent> {

	private CommandManager commandManager;
	
	public SceneEditionComponentProvider(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public JComponent getComponent(SceneInterfaceElement element) {
		EAdFixScrollCanvasPanel scroll = new EAdFixScrollCanvasPanel();

		EAdCanvasPanel dragPanel = scroll.getCanvas();

		DragListener listener = new ResizeListener(dragPanel);
		
		@SuppressWarnings("serial")
		JComponent j = new JComponent() {
			protected void paintComponent(Graphics g) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}

		};
		j.setBounds(0, 0, 50, 50);
		j.addKeyListener(listener);
		j.addMouseListener(listener);
		j.addMouseMotionListener(listener);
		dragPanel.add(j);

		return scroll;
	}

	
	
	
}
