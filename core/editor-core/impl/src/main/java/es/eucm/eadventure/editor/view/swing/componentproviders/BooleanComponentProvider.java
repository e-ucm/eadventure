package es.eucm.eadventure.editor.view.swing.componentproviders;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.control.commands.impl.ChangeFieldValueCommand;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.BooleanOption;

/**
 * Swing {@link ComponentProvider} for field with {@link Boolean} elements.
 * <p>
 * This provider directly shows and manipulates the value of the boolean field
 * using a JCheckBox.
 * 
 */
public class BooleanComponentProvider implements
		ComponentProvider<BooleanOption, JCheckBox> {

	/**
	 * The {@link BooleanOption} option element
	 */
	private BooleanOption element;

	/**
	 * The JCheckBox where the value is shown and through which it is changed
	 */
	private JCheckBox checkBox;

	/**
	 * The system dependent {@link FieldValueReader}
	 */
	private FieldValueReader fieldValueReader;
	
	/**
	 * The command manager {@link CommandManager}
	 */
	private CommandManager commandManager;

	/**
	 * Constructor.
	 * 
	 * @param fieldValueReader
	 *            the {@link FieldValueReader}
	 * @param commandManager
	 * 			  the {@link CommandManager}
	 */
	public BooleanComponentProvider(FieldValueReader fieldValueReader,
			CommandManager commandManager) {
		this.fieldValueReader = fieldValueReader;
		this.commandManager = commandManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.editor.view.ComponentProvider#getComponent(es.eucm
	 * .eadventure.editor.view.generics.InterfaceElement)
	 */
	@Override
	public JCheckBox getComponent(BooleanOption element2) {
		this.element = element2;
		checkBox = new JCheckBox(element.getTitle());
		checkBox.setToolTipText(element.getToolTipText());
		checkBox.setSelected(fieldValueReader.readValue(element
				.getFieldDescriptor()));
		checkBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				ChangeFieldValueCommand<Boolean> changeFieldValueCommand;
				changeFieldValueCommand = new ChangeFieldValueCommand<Boolean>(new Boolean(checkBox.isSelected()), element
						.getFieldDescriptor());
				commandManager.performCommand(changeFieldValueCommand);
			}
			
		});
		return checkBox;
	}

}
