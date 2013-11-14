package es.eucm.ead.engine.gameobjects.effects;

import com.google.inject.Inject;
import es.eucm.ead.engine.debugger.CommandInterpreter;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.model.elements.effects.CommandEf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandGO extends AbstractEffectGO<CommandEf> {

	private static final Logger logger = LoggerFactory
			.getLogger(CommandGO.class);

	private CommandInterpreter interpreter;

	@Inject
	public CommandGO(Game game, CommandInterpreter interpreter) {
		super(game);
		this.interpreter = interpreter;
	}

	@Override
	public void initialize() {
		super.initialize();
		String result = interpreter.interpret(effect.getCommand());
		logger.debug("{}:{}", effect.getCommand(), result);
	}
}
