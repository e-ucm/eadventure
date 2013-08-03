package ead.guitools.exportergui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.Value;

import javax.swing.*;
import java.util.Properties;

public class UIUtils {

	private final AssetManager assetManager;
	private final Texture bgTexture;
	private Skin skin;
	private Label.LabelStyle titleStyle;
	private float generalPadding = 7.0f;
	private JFileChooser fileChooser;

	public UIUtils(Skin skin, AssetManager assetManager) {
		this.skin = skin;
		this.assetManager = assetManager;
		this.bgTexture = assetManager.get("whitebg.png");
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		titleStyle = skin.get("title", Label.LabelStyle.class);
	}

	public Table createTitle(String icon, String title) {
		Table t = new Table();
		t.debug();
		t.left().row().pad(10);
		t.add(
				new Image(new TextureRegionDrawable(new TextureRegion(
						new Texture(icon))))).left();
		t.add(new Label(title, titleStyle)).padLeft(0);
		Color c = skin.getColor("lightgray");
		t.setBackground(new ColorDrawable(c.a, c.r, c.g, c.b));
		return t;
	}

	public Table createSelectfile(String title, String tooltip,
			final String property, final Properties properties) {
		Table t = new Table();
		t.left();
		t.debug();
		t.pad(generalPadding, generalPadding, generalPadding, generalPadding);
		t.add(new Label(title, skin)).left().width(Value.percentWidth(0.3f));

		String initialValue = properties.getProperty(property, "");
		final TextField tf = new TextField(initialValue, skin);
		t.add(tf).left().width(Value.percentWidth(0.60f));
		TextButton tb = new TextButton("...", skin);
		tb.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					tf.setText(fileChooser.getSelectedFile().getAbsolutePath());
					properties.setProperty(property, fileChooser
							.getSelectedFile().getAbsolutePath());
				}
				return true;
			}
		});
		t.add(tb).left().width(Value.percentWidth(0.05f));
		return t;
	}

	public Table createTextfield(String title, String tooltip,
			final String property, final Properties properties) {
		Table t = new Table();
		t.debug();
		t.left();
		t.pad(generalPadding, generalPadding, generalPadding, generalPadding);
		t.add(new Label(title, skin)).left().width(Value.percentWidth(0.3f));
		final TextField tf = new TextField(
				properties.getProperty(property, ""), skin);
		tf.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				properties.setProperty(property, tf.getText());
				return true;
			}
		});
		t.add(tf).left().width(Value.percentWidth(0.65f));
		return t;
	}

	public Table createCheckbox(String title, String tooltip,
			final String property, final Properties properties) {
		Table t = new Table();
		t.left();
		t.debug();
		t.pad(generalPadding, generalPadding, generalPadding, generalPadding);
		t.add(new Label(title, skin)).left().width(Value.percentWidth(0.3f));
		final CheckBox cb = new CheckBox("", skin);
		cb
				.setChecked(properties.getProperty(property, "false").equals(
						"true") ? true : false);
		cb.addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// Yes, !cb.isChecked(), because it is checked after this touch down
				properties.setProperty(property,
						event.getButton() == Input.Buttons.LEFT
								&& !cb.isChecked() ? "true" : "false");
				return true;
			}
		});
		t.add(cb).left();
		return t;
	}

	public Separator createSeparator() {
		return new Separator();
	}

	public class Separator extends Widget {
		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);
			batch.setColor(1, 1, 1, 1);
			batch.draw(bgTexture, this.getX(), this.getY(), this.getWidth(),
					this.getHeight());
		}

		@Override
		public float getMinHeight() {
			return 1;
		}
	}

	private class ColorDrawable extends BaseDrawable {

		private float a;
		private float r;
		private float g;
		private float b;

		public ColorDrawable(float a, float r, float g, float b) {
			this.a = a;
			this.r = r;
			this.g = g;
			this.b = b;
		}

		@Override
		public void draw(SpriteBatch batch, float x, float y, float width,
				float height) {
			batch.setColor(r, g, b, a);
			batch.draw(bgTexture, x, y, width, height);
			super.draw(batch, x, y, width, height);
		}
	}
}
