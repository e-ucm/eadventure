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

package es.eucm.ead.engine.gameobjects.sceneelements;

import com.google.inject.Inject;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.SpecialAssetRenderer;
import es.eucm.ead.engine.factories.EventFactory;
import es.eucm.ead.engine.factories.SceneElementFactory;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.model.assets.multimedia.EAdVideo;
import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.model.elements.scenes.VideoScene;
import es.eucm.ead.model.elements.widgets.Label;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.util.Position.Corner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoSceneGO extends SceneGO {

	private static final Logger logger = LoggerFactory.getLogger("VideoScene");

	private SpecialAssetRenderer<EAdVideo, ?> specialAssetRenderer;

	private Object component;

	private boolean error;

	private boolean toStart = false;

	private VideoScene videoScene;

	private EAdVideo video;

	@Inject
	public VideoSceneGO(AssetHandler assetHandler,
			SceneElementFactory gameObjectFactory, Game game,
			EventFactory eventFactory) {
		super(assetHandler, gameObjectFactory, game, eventFactory);
		this.component = null;
		this.error = false;
	}

	public void setElement(EAdSceneElement element) {
		super.setElement(element);
		Label label = new Label("Loading...");
		label.setColor(ColorFill.WHITE);
		label.setPosition(Corner.CENTER, 400, 300);
		this.addSceneElement(label);
		this.videoScene = (VideoScene) element;
		video = (EAdVideo) element.getDefinition().getAsset(currentBundle,
				VideoScene.video);
		this.toStart = false;
		this.error = false;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		specialAssetRenderer = assetHandler.getSpecialAssetRenderer(video);
		// Check for the renderer
		if (specialAssetRenderer == null && !assetHandler.isPreloadingVideos()) {
			error = true;
		}

		// We'll wait
		if (!error && specialAssetRenderer == null) {
			return;
		}

		if (error || specialAssetRenderer.isFinished()) {
			gui.showSpecialResource(null, 0, 0, true);
			removeVideoComponent();
		} else if (component == null) {
			try {
				component = specialAssetRenderer.getComponent(video);
				if (component != null) {
					gui.showSpecialResource(component, 0, 0, true);
					toStart = true;
				}
			} catch (Exception e) {
				logger.warn("Exception creating video component", e);
				error = true;
			} catch (Error e) {
				logger.warn("Error creating video component", e);
				error = true;
			}
		} else if (toStart) {
			toStart = false;
			specialAssetRenderer.start();
		}
	}

	private void removeVideoComponent() {
		component = null;
		specialAssetRenderer.reset();
		if (videoScene.getFinalEffects().size() == 0) {
			ChangeSceneEf ef = new ChangeSceneEf();
			game.addEffect(ef);

		} else {
			for (EAdEffect e : videoScene.getFinalEffects()) {
				game.addEffect(e);
			}
		}

	}

}
