/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.engine.debugger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameLoader;
import es.eucm.ead.engine.game.interfaces.EffectsHandler.EffectsListener;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneGO;
import es.eucm.ead.model.Commands;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.effects.ChangeChapterEf;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.effects.ToggleSoundEf;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.reader.model.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@Singleton
public class CommandInterpreter implements EffectsListener {

	private static final Logger logger = LoggerFactory
			.getLogger(CommandInterpreter.class);

	private Game game;

	private GameLoader gameLoader;

	private Array<String> effects;

	private int waiting = 0;

	private Vector2 aux = new Vector2();

	@Inject
	public CommandInterpreter(Game game, GameLoader gameLoader) {
		this.game = game;
		game.getEffectsHandler().addEffectListener(this);
		this.gameLoader = gameLoader;
		this.effects = new Array<String>();
	}

	public boolean isWaiting() {
		return waiting > 0;
	}

	public void effectLaunched(Effect e, Event action, SceneElement parent) {
		if (effects.size > 0 && effects.get(0).equals(e.getId())) {
			effects.removeIndex(0);
		}
	}

	public String interpret(String command) {
		String result = "Invalid command.";
		try {
			command = command.trim();
			if (command.equals(Commands.NOTIFY)) {
				waiting--;
				result = "Notified.";
			} else if (command.equals(Commands.WAIT)) {
				waiting++;
				result = "Waiting.";
			} else if (command.equals(Commands.PASS)) {
				result = "OK.";
			} else if (command.equals("scene")) {
				result = game.getGUI().getScene().getElement().getId();
			} else if (command.startsWith(Commands.GO_CHAPTER)) {
				String[] parts = command.split(" ");
				game.addEffect(new ChangeChapterEf(parts[1]));
			} else if (command.equals(Commands.CHAPTER)) {
				result = gameLoader.getCurrentChapterId();
			} else if (command.startsWith(Commands.GO)) {
				String[] parts = command.split(" ");
				game.addEffect(new ChangeSceneEf(new BasicElement(parts[1])));
				result = "OK.";
			} else if (command.startsWith(Commands.LIST)) {
				String[] parts = command.split(" ");
				if (parts[1].equals("scenes")) {
					Manifest m = gameLoader.loadManifest();
					result = m.getChaptersScenes().get(
							gameLoader.getCurrentChapterId()).toString();
				} else if (parts[1].equals("chapters")) {
					Manifest m = gameLoader.loadManifest();
					result = m.getChaptersScenes().keySet().toString();
				} else if (parts[1].equals("elements")) {
					SceneGO scene = game.getGUI().getScene();
					result = addGroup(scene);
				} else if (parts[1].equals("variables")) {
					Map<String, Object> vars = game.getGameState()
							.getElementVars(parts[2]);
					result = vars == null ? "[]" : vars.toString();
				}
			} else if (command.startsWith(Commands.LOAD)) {
				String[] parts = command.split(" ");
				result = runCommands(parts[1]);
			} else if (command.startsWith(Commands.SET)) {
				String[] parts = command.split(" ");
				String[] field = parts[1].split("\\.");
				Object value = parseValue(parts[2]);
				game.getGameState().setValue(field[0], field[1], value);
				result = "OK.";
			} else if (command.startsWith(Commands.GET)) {
				String[] parts = command.split(" ");
				String[] field = parts[1].split("\\.");
				result = game.getGameState().getValue(field[0], field[1], null)
						+ "";
			} else if (command.startsWith(Commands.PING)) {
				String[] parts = command.split(" ");
				ping(parts[1]);
				result = "OK.";
			} else if (command.startsWith(Commands.SOUND)) {
				game.addEffect(new ToggleSoundEf());
				result = "OK.";
			} else if (command.startsWith(Commands.WATCHING)) {
				String[] parts = command.split(" ");
				result = game.getGameState().countWatchers(parts[1])
						+ " watchers.";
			} else if (command.startsWith(Commands.WHOIS)) {
				result = game.getGUI().getGameObjectUnderPointer() + "";
			} else if (command.startsWith("is")) {
				String[] parts = command.split(" ");
				result = game.getGUI().getScene().findActor(parts[1]) == null ? "false"
						: "true";
			} else if (command.startsWith(Commands.MOVE)) {
				String[] parts = command.split(" ");
				int x = Integer.parseInt(parts[1]);
				int y = Integer.parseInt(parts[2]);
				Gdx.input.setCursorPosition(x, Gdx.graphics.getHeight() - y);
				result = "OK.";
			} else if (command.startsWith(Commands.WHERE)) {
				result = "(" + Gdx.input.getX() + ", "
						+ (Gdx.graphics.getHeight() - Gdx.input.getY()) + ")";
			} else if (command.startsWith(Commands.CLICK)) {
				String[] parts = command.split(" ");
				int x = Integer.parseInt(parts[1]);
				int y = Integer.parseInt(parts[2]);
				Gdx.input.setCursorPosition(x, Gdx.graphics.getHeight() - y);
				Stage s = gameLoader.getEngine().getStage();
				aux.set(x, y);
				aux = s.screenToStageCoordinates(aux);
				Actor a = s.hit(aux.x, aux.y, true);
				gameLoader.getEngine().getStage().touchDown(x, y, 0,
						Input.Buttons.LEFT);
				result = a == null ? "" : a.getName();
			} else if (command.startsWith(Commands.EXIT)) {
				Gdx.app.exit();
			} else if (command.startsWith(Commands.LOG)) {
				logger.debug(command.substring(Math
						.max(0, command.indexOf(' '))));
				result = "OK.";
			} else if (command.startsWith(Commands.WAIT_EFFECTS)) {
				String[] parts = command.split(" ");
				for (int i = 1; i < parts.length; i++) {
					effects.add(parts[i]);
				}
				result = effects.size + " effects waiting";
			} else if (command.startsWith(Commands.CLEAR_EFFECTS)) {
				result = effects.size + " effects cleared.";
				effects.clear();
			} else if (command.startsWith(Commands.EFFECTS)) {
				result = effects.toString();
			}
		} catch (Exception e)

		{
			logger.error("{}", e);
			result = "Wrong parameters";
		}

		return result;
	}

	private void ping(String part) {
		Actor a = game.getGUI().getScene().findActor(part);
		if (a != null) {
			a.addAction(new PingAction());
		}
	}

	private String addGroup(Group g) {
		String result = "";
		for (Actor a : g.getChildren()) {
			if (a.getName() != null) {
				result += a.getName() + ",";
			}
			if (a instanceof Group) {
				result += addGroup((Group) a);
			}
		}
		return result;
	}

	private Object parseValue(String part) {
		if (part.equals("true")) {
			return true;
		} else if (part.equals("false")) {
			return false;
		} else if (part.contains("\"")) {
			return part.substring(1, part.length() - 1);
		} else {
			Object value = null;
			if (part.contains(".")) {
				try {
					value = Float.parseFloat(part);
				} catch (Exception e) {

				}
			}
			if (value == null) {
				try {
					value = Integer.parseInt(part);
				} catch (Exception e) {

				}
			}

			return value == null ? part : value;
		}
	}

	private String runCommands(String file) {
		String result = "";
		FileHandle fh = Gdx.files.internal(file);

		if (fh != null) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(fh.reader());
				String line;
				while ((line = reader.readLine()) != null) {
					// Ignore comments
					if (!line.startsWith("#")) {
						result = interpret(line);
					}
				}
			} catch (Exception e) {
				logger.error("Error reading text file {}", file, e);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					logger.error("Error reading text file {}", e);
				}
			}
		}
		return result;
	}

	public class PingAction extends Action {

		private boolean going;

		private float time = 10.0f;

		private Color prevColor;

		@Override
		public boolean act(float delta) {
			if (!going) {
				this.prevColor = this.actor.getColor().cpy();
				actor.setColor(Color.GREEN);
				time = 2000;
				going = true;
			}

			if (going) {
				time -= delta;
			}
			if (time < 0) {
				this.actor.setColor(prevColor);
			}
			return time < 0;
		}
	}
}
