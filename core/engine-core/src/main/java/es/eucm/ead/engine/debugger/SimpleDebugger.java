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
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameLoader;

public class SimpleDebugger extends Group {

	private final Label result;

	private final TextField interpreter;

	private final Button button;

	public SimpleDebugger(Game game, GameLoader gameLoader) {
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
					result.setText(commandInterpreter.interpret(command));
					interpreter.setText("");
					break;
				case Input.Keys.ESCAPE:
					setVisible(false);
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

	public void setVisible(boolean visible) {
		result.setVisible(visible);
		interpreter.setVisible(visible);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
