package es.eucm.eadventure.editor.view;

public interface ComponentProvider<ElementType, ComponentType> {
	
	ComponentType getComponent(ElementType element);
	
}
