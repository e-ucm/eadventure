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

package ead.engine.core.gameobjects.effects;

import com.google.inject.Inject;
import ead.common.model.assets.AssetDescriptor;
import ead.common.model.assets.multimedia.EAdVideo;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.LoadGameEf;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.huds.MouseHud;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.SystemFields;
import ead.common.model.elements.predef.LoadingScreen;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.reader.AdventureReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class LoadGameGO extends AbstractEffectGO<LoadGameEf> {

    private static final Logger logger = LoggerFactory.getLogger("LoadGame");

    private SceneElementGOFactory sceneElementFactory;

    private GUI gui;

    private Game game;

    private AssetHandler assetHandler;

    private AdventureReader reader;

    private Stack<AssetDescriptor> assetsToLoad;

    private boolean readingXML;

    private boolean readingAssets;

    private int assetsToLoadNumber;

    private boolean done;

    @Inject
    public LoadGameGO(GameState gameState, AdventureReader adventureReader,
                      SceneElementGOFactory sceneElementFactory,
                      AssetHandler assetHandler, Game game, GUI gui) {
        super(gameState);
        this.reader = adventureReader;
        this.sceneElementFactory = sceneElementFactory;
        this.assetHandler = assetHandler;
        this.game = game;
        this.gui = gui;
        assetsToLoad = new Stack<AssetDescriptor>();
    }

    public void initialize() {
        if (!assetHandler.fileExists("@data.xml")) {
            done = true;
        } else {
            super.initialize();
            if (effect.isReloadAssets()) {
                assetHandler.clean();
            }
            done = false;
            EAdScene loadingScreen = effect.getLoadingScene();
            if (loadingScreen == null) {
                loadingScreen = new LoadingScreen();
            }
            gui.setScene((SceneGO) sceneElementFactory.get(loadingScreen));
            gameState.addEffect(new ChangeFieldEf(
                    new BasicField<Boolean>(new BasicElement(MouseHud.CURSOR_ID),
                            SceneElement.VAR_VISIBLE), EmptyCond.FALSE));
            reader.readXML(assetHandler.getTextFile("@data.xml"), game);
            readingXML = true;
        }
    }

    public void act(float delta) {
        if (readingXML) {
            for (int i = 0; i < 200; i++) {
                if (reader.step()) {
                    break;
                }
            }

            if (reader.step()) {
                gameState.setValue(SystemFields.LOADING, 30);
                readingAssets = true;
                readingXML = false;
                assetsToLoad.addAll(reader.getAssets());
                logger.info("Loading {} assets", reader.getAssets().size());
                assetsToLoadNumber = reader.getAssets().size();
            }
        } else if (readingAssets) {
            for (int i = 0; i < 10; i++) {
                if (assetsToLoad.isEmpty()) {
                    readingAssets = false;
                    done = true;
                    break;
                }
                AssetDescriptor a = assetsToLoad.pop();
                if (!(a instanceof EAdVideo)) {
                    assetHandler.getRuntimeAsset(a);
                } else {
                    assetHandler.addVideo((EAdVideo) a);
                }
            }
            if (assetsToLoadNumber > 0) {
                gameState.setValue(SystemFields.LOADING, (int) (30.0f + 70.f
                        * (float) (assetsToLoadNumber - assetsToLoad.size())
                        / (float) assetsToLoadNumber));
            } else {
                gameState.setValue(SystemFields.LOADING, 100);
            }
        }
    }

    public boolean isFinished() {
        return done;
    }

    public void finish() {
        super.finish();
        game.startGame();
        gameState.addEffect(new ChangeFieldEf(
                new BasicField<Boolean>(new BasicElement(MouseHud.CURSOR_ID),
                        SceneElement.VAR_VISIBLE), EmptyCond.TRUE));
    }

    public boolean isQueueable() {
        return true;
    }

}
