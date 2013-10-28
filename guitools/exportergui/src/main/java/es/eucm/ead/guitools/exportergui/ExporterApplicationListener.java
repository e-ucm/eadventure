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

package es.eucm.ead.guitools.exportergui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.esotericsoftware.tablelayout.Value;
import es.eucm.ead.exporter.AndroidExporter;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class ExporterApplicationListener implements ApplicationListener {

	private AndroidExporterGUI.ExportListener exportListener;
	private AssetManager assetManager;

	private Stage stage;
	private Skin uiSkin;
	private TextField iconEdit;
	private CheckBox signCb;
	private TextField keystoreEdit;
	private TextButton exportButton;
	private Properties properties;
	private JFrame frame;

	public ExporterApplicationListener(JFrame frame,
			AndroidExporterGUI.ExportListener listener) {
		this.properties = new Properties();
		this.exportListener = listener;
		this.frame = frame;
		loadProperties();
	}

	public void create() {
		assetManager = new AssetManager();
		assetManager.load("android.png", Texture.class);
		assetManager.load("whitebg.png", Texture.class);
		assetManager.finishLoading();

		Gdx.gl.glClearColor(0.25f, 0.26f, 0.27f, 1.0f);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Table table = new Table();
		table.debug();
		table.setFillParent(true);
		table.left().top();
		stage.addActor(table);
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		UIUtils uiUtils = new UIUtils(uiSkin, assetManager);

		table.add(uiUtils.createTitle("android.png", "Android Export")).width(
				Value.percentWidth(1));
		table.row();
		table.add(
				uiUtils.createSelectfile("SDK Folder", "SDK folder",
						AndroidExporter.SDK_HOME, properties)).width(
				Value.percentWidth(1));
		table.row();
		table.add(
				uiUtils.createTextfield("Package name", "Package name",
						AndroidExporter.PACKAGE_NAME, properties)).width(
				Value.percentWidth(1));
		table.row();
		table.add(
				uiUtils.createTextfield("Title", "Package name",
						AndroidExporter.TITLE, properties)).width(
				Value.percentWidth(1));
		table.row();
		table.add(
				uiUtils.createSelectfile("Icon", "Package name",
						AndroidExporter.ICON, properties)).width(
				Value.percentWidth(1));
		table.row();
		table.add(
				uiUtils.createCheckbox("Sign apk", "Sign apk", "sign_apk",
						properties)).width(Value.percentWidth(1));
		table.row();
		table.add(
				uiUtils.createSelectfile("Key store", "Key store", "key_store",
						properties)).width(Value.percentWidth(1));
		table.row();
		table.add(
				uiUtils.createTextfield("Key alias", "Key alias", "key_alias",
						properties)).width(Value.percentWidth(1));
		table.row();
		table.add(uiUtils.createSeparator()).width(Value.percentWidth(1));
		table.row();
		TextButton tb = new TextButton("Export", uiSkin);
		tb.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				frame.setVisible(false);
				exportListener.export(properties);
				return true;
			}
		});
		table.add(tb).right().pad(10);

	}

	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		//Table.drawDebug(stage); // This is optional, but enables debug lines for tables.
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public void dispose() {
		stage.dispose();
		assetManager.dispose();
	}

	private void loadProperties() {
		File f = new File("exporter.android.properties");
		InputStream is = null;
		try {
			f.createNewFile();
			is = new FileInputStream(f);
			properties.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void saveProperties() {
		File f = new File("exporter.android.properties");
		OutputStream is = null;
		try {
			is = new FileOutputStream(f);
			properties.store(is, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
