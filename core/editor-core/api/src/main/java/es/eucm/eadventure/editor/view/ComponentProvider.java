package es.eucm.eadventure.editor.view;

public interface ComponentProvider<ElementType, ComponentType> {
	
	void setElement(ElementType element);
	
	ComponentType getComponent();

}
