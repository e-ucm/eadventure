package es.eucm.ead.model.elements.effects;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;

@Element
public class CommandEf extends Effect {

	@Param
	private String command;

	public CommandEf() {

	}

	public CommandEf(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String toString() {
		return ">" + command;
	}
}
