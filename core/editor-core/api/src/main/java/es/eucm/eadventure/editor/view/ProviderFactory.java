package es.eucm.eadventure.editor.view;

import es.eucm.eadventure.editor.view.generics.InterfaceElement;

public interface ProviderFactory<T> {

	<S extends InterfaceElement> ComponentProvider<S, T> getProvider(S option);
	
}
