package es.eucm.eadventure.editor.view.swing;

import javax.swing.JComponent;

import com.google.inject.Inject;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.BooleanOption;
import es.eucm.eadventure.editor.view.generics.impl.EAdListOption;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.editor.view.generics.impl.ElementOption;
import es.eucm.eadventure.editor.view.generics.impl.PanelImpl;
import es.eucm.eadventure.editor.view.generics.impl.TextOption;
import es.eucm.eadventure.editor.view.impl.AbstractProviderFactory;

/**
 * Java Swing {@link ProviderFactory}
 */
public class SwingProviderFactory extends AbstractProviderFactory<JComponent> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Inject
	public SwingProviderFactory(StringHandler stringHandler, CommandManager commandManager) {
		super();
		FieldValueReader fieldValueReader = new SwingFieldValueReader();

		this.addToMap(TextOption.class,
				(ComponentProvider) new TextComponentProvider(fieldValueReader));
		this.addToMap(EAdStringOption.class,
				(ComponentProvider) new EAdStringComponentProvider(
						stringHandler, fieldValueReader, commandManager));
		this.addToMap(BooleanOption.class,
				(ComponentProvider) new BooleanComponentProvider(
						fieldValueReader));
		this.addToMap(PanelImpl.class,
				(ComponentProvider) new PanelComponentProvider(this));
		this.addToMap(ElementOption.class,
				(ComponentProvider) new ElementComponentProvider());
		this.addToMap(
				EAdListOption.class,
				(ComponentProvider) new EAdListComponentProvider(commandManager));
	}

}
