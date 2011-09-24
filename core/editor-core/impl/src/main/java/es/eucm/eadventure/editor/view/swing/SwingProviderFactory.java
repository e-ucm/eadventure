package es.eucm.eadventure.editor.view.swing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;

import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.InterfaceElement;
import es.eucm.eadventure.editor.view.generics.impl.TextOption;
import es.eucm.eadventure.editor.view.impl.AbstractProviderFactory;

public class SwingProviderFactory extends AbstractProviderFactory<JComponent> {

	@SuppressWarnings("unchecked")
	public SwingProviderFactory() {
		super();
		this.addToMap(TextOption.class, (Class<? extends ComponentProvider<? extends InterfaceElement, JComponent>>) TextComponentProvider.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <S extends ComponentProvider<? extends InterfaceElement, JComponent>> S getInstance(
			Class<? extends ComponentProvider<? extends InterfaceElement, JComponent>> clazz) {
		Constructor<?> ctorlist[]  = clazz.getDeclaredConstructors();
		try {
			return (S) ctorlist[0].newInstance();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}
