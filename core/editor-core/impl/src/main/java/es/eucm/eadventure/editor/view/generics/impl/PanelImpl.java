package es.eucm.eadventure.editor.view.generics.impl;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.editor.view.generics.InterfaceElement;
import es.eucm.eadventure.editor.view.generics.Panel;

public class PanelImpl implements Panel {

	private List<InterfaceElement> elements;
	
	private String title;
	
	public PanelImpl(String title) {
		elements = new ArrayList<InterfaceElement>();
		this.title = title;
	}

	@Override
	public List<InterfaceElement> getElements() {
		return elements;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void addElement(InterfaceElement element) {
		elements.add(element);
	}

}
