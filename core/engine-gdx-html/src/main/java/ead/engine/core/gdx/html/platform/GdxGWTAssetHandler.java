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

package ead.engine.core.gdx.html.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.assets.GdxAssetHandler;
import ead.tools.GenericInjector;
import ead.tools.SceneGraph;

@Singleton
public class GdxGWTAssetHandler extends GdxAssetHandler {

	@Inject
	public GdxGWTAssetHandler(GenericInjector injector,
			FontHandler fontHandler, SceneGraph sceneGraph) {
		super(injector, fontHandler, sceneGraph);
	}

	@Override
	public void initialize() {
		super.initialize();
		setLoaded(true);
	}

	@Override
	public void getTextfileAsync(String path, TextHandler textHandler) {
		try {
			new RequestBuilder(RequestBuilder.GET, path).sendRequest("",
					new TextRequestCallback(textHandler));
		} catch (RequestException e) {
			textHandler.handle(null);
		}
	}

	private class TextRequestCallback implements RequestCallback {

		private TextHandler textHandler;

		public TextRequestCallback(TextHandler textHandler) {
			super();
			this.textHandler = textHandler;
		}

		@Override
		public void onResponseReceived(Request req, Response resp) {
			String text = resp.getText().replace("\n\r", "\n");
			text = text.replace("\r\n", "\n");
			text = text.replace("\r", "\n");
			textHandler.handle(text);
		}

		@Override
		public void onError(Request res, Throwable throwable) {
			textHandler.handle(null);
		}

	}

	public FileHandle getProjectFileHandle(String uri) {
		return Gdx.files.internal(this.resourcesUri + "/" + uri);
	}

}
