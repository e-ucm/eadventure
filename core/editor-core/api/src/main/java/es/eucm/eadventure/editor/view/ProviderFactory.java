package es.eucm.eadventure.editor.view;

import es.eucm.eadventure.editor.view.generics.InterfaceElement;

/**
 * Factor for {@link ComponentProvider} based on 
 * @param <T>
 */
public interface ProviderFactory<T> {

	<S extends InterfaceElement> ComponentProvider<S, T> getProvider(S option);
	
}
