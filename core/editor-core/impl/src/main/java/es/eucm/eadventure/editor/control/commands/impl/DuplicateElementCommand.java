package es.eucm.eadventure.editor.control.commands.impl;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.editor.control.Command;

public class DuplicateElementCommand<P extends EAdElement> extends Command {
	
	/**
	 * The list in which the duplicated elements will be placed.
	 */
	private EAdElementList<P> elementList;
	/**
	 * The element to be duplicated
	 */
	private P anElement;
	
	/**
	 * The duplicated element.
	 */
	private P duplicatedElement;
	
	/**
     * Constructor for the AddElementCommand class.
     * 
     * @param list
     *            The EAdElementList in which the command is to be applied 
     * @param e
     *            The P element to be added to a list by the command
     *
     */
	public DuplicateElementCommand(EAdElementList<P> list, P e) {
		this.elementList = list;
		this.anElement = e;		
	}


	@Override
	public boolean performCommand() {
		
		if (elementList.contains(anElement)){
			duplicatedElement = (P) anElement.copy();
			elementList.add(duplicatedElement);
			return true;
			}
		return false;
	}

	@Override
	public boolean canUndo() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean undoCommand() {
		// TODO Auto-generated method stub
		if (elementList.contains(duplicatedElement)){
			elementList.remove(duplicatedElement);
			return true;
		}
		return false;
	}

	@Override
	public boolean canRedo() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean redoCommand() {

		if (elementList.contains(anElement)){
			duplicatedElement = (P) anElement.copy();
			elementList.add(duplicatedElement);
			return true;
			}
		return false;
	}

	@Override
	public boolean combine(Command other) {
		// TODO Auto-generated method stub
		return false;
	}

}
