package ead.engine.core.gameobjects.effects;

import java.util.Map.Entry;

import com.google.inject.Inject;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.RandomEf;
import ead.common.util.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;

public class RandomGO extends AbstractEffectGO<RandomEf>{

	@Inject
	public RandomGO(AssetHandler assetHandler, StringHandler stringsReader,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
	}
	
	public void initilize( ){
		super.initilize();
		
		float totalProbability = 0.0f;
		for ( Float f: element.getEffects().values()){
			totalProbability += f;
		}
		
		float random = (float) (Math.random() * totalProbability);
		totalProbability = 0.0f;
		for ( Entry<EAdEffect, Float> entry: element.getEffects().entrySet()){
			if ( totalProbability < random && random < totalProbability + entry.getValue() ){
				gameState.addEffect(entry.getKey());
				return;
			}
		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
