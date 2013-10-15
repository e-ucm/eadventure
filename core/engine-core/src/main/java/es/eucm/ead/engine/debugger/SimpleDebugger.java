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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameLoader;

public class SimpleDebugger extends Group {

	private final Label result;

	private final TextField interpreter;

	private final Button button;

	private final GameLoader gameLoader;

	private Array<String> history;

	private int historyPointer = -1;

	public SimpleDebugger(Game game, GameLoader gameLoader) {
		this.history = new Array<String>();
		this.gameLoader = gameLoader;
		final CommandInterpreter commandInterpreter = new CommandInterpreter(
				game, gameLoader);
		BitmapFont font = new BitmapFont(
				Gdx.files
						.internal("es/eucm/ead/engine/resources/binary/fonts/ubuntu-16-bold.fnt"),
				true);
		this.setX(0);
		this.setY(580);

		Pixmap p = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
		p.setColor(Color.BLACK);
		p.fill();
		TextureRegionDrawable background = new TextureRegionDrawable(
				new TextureRegion(new Texture(p)));
		p.dispose();
		p = new Pixmap(2, 30, Pixmap.Format.RGBA8888);
		p.setColor(Color.WHITE);
		p.fill();
		TextureRegionDrawable cursor = new TextureRegionDrawable(
				new TextureRegion(new Texture(p)));
		p.dispose();

		Label.LabelStyle style = new Label.LabelStyle();
		SpriteDrawable backgroundSprite = new SpriteDrawable(new Sprite(
				background.getRegion()));
		backgroundSprite.getSprite().setColor(new Color(0, 0, 0, 0.5f));
		style.background = backgroundSprite;
		style.font = font;
		style.fontColor = Color.WHITE;
		result = new Label("", style);
		result.setWrap(true);
		int y = -100;

		result.setBounds(0, y, 800, -y);
		this.addActor(result);

		TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
		tfStyle.font = font;
		tfStyle.background = background;
		tfStyle.fontColor = Color.WHITE;
		tfStyle.cursor = cursor;
		interpreter = new TextField("", tfStyle);
		interpreter.setBounds(10, 0, 800, 20);
		interpreter.getStyle().font = font;
		interpreter.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
				case Input.Keys.ENTER:
					String command = interpreter.getText();
					if (history.size == 0 || !history.peek().equals(command)) {
						history.add(command);
						historyPointer = history.size;
					}
					result.setText(commandInterpreter.interpret(command));
					interpreter.setText("");
					break;
				case Input.Keys.ESCAPE:
					setVisible(false);
					break;
				case Input.Keys.UP:
					previousCommand();
					break;
				case Input.Keys.DOWN:
					nextCommand();
					break;
				}
				return true;
			}
		});

		Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
		Label label = new Label(">", style);
		buttonStyle.up = background;
		button = new Button(label, buttonStyle);
		button.setHeight(20);
		button.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				setVisible(true);
				return false;
			}
		});

		this.addActor(interpreter);
		setVisible(false);
		this.addActor(button);
		gameLoader.getEngine().getStage().addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
				case Input.Keys.F12:
					setVisible(true);
					break;
				}
				return false;
			}
		});
	}

	private void updateCommand() {
		if (historyPointer == history.size) {
			interpreter.setText("");
		} else {
			interpreter.setText(history.get(historyPointer));
		}
	}

	private void nextCommand() {
		historyPointer = Math.min(historyPointer + 1, history.size);
		updateCommand();
	}

	private void previousCommand() {
		historyPointer = Math.max(historyPointer - 1, 0);
		updateCommand();
	}

	public void setVisible(boolean visible) {
		result.setVisible(visible);
		interpreter.setVisible(visible);
		if (visible) {
			gameLoader.getEngine().getStage().setKeyboardFocus(interpreter);
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
