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

package ead.engine.core.gameobjects.go;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.scenes.VideoScene;
import ead.common.resources.assets.multimedia.EAdVideo;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.input.InputAction;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.SpecialAssetRenderer;
import ead.engine.core.util.EAdTransformation;

public class VideoSceneGO extends ComplexSceneElementGOImpl<VideoScene>
		implements ComplexSceneElementGO<VideoScene> {

	private static final Logger logger = LoggerFactory
			.getLogger("VideoScreenGOImpl");

	private SpecialAssetRenderer<EAdVideo, ?> specialAssetRenderer;

	private Object component;

	private boolean error;

	public VideoSceneGO(AssetHandler assetHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			SpecialAssetRenderer<EAdVideo, ?> specialAssetRenderer) {
		super(assetHandler, gameObjectFactory, gui, gameState, eventFactory);
		logger.info("New instance");
		this.specialAssetRenderer = specialAssetRenderer;
		this.component = null;
		this.error = false;
	}

	public void doLayout(EAdTransformation transformation) {
		// Do nothing
	}

	boolean toStart = false;

	@Override
	public void update() {
		super.update();
		if (component == null) {
			try {
				EAdVideo v = (EAdVideo) element.getDefinition().getAsset(
						currentBundle, VideoScene.video);
				component = specialAssetRenderer.getComponent(v);
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
		} else if (error || specialAssetRenderer.isFinished()) {
			gui.showSpecialResource(null, 0, 0, true);
			removeVideoComponent();
		}
	}

	private void removeVideoComponent() {
		component = null;

		if (element.getFinalEffects().size() == 0) {
			ChangeSceneEf ef = new ChangeSceneEf();
			gameState.addEffect(ef);

		} else {
			for (EAdEffect e : element.getFinalEffects()) {
				gameState.addEffect(e);
			}
		}

	}

	@Override
	public boolean contains(int x, int y) {
		return false;
	}

	@Override
	public DrawableGO<?> processAction(InputAction<?> action) {
		return null;
	}

}
