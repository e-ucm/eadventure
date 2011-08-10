package es.eucm.eadventure.common.elmentfactories.scenedemos;

import es.eucm.eadventure.common.elmentfactories.EAdElementsFactory;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.impl.extra.EAdSceneElementVars;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;

public class MoleGame extends EmptyScene {

	private Drawable holeImage = new ImageImpl("@drawable/hole.png");

	private FramesAnimation mole;

	public MoleGame() {
		setBackgroundFill(new EAdLinearGradient(EAdColor.RED, EAdColor.BROWN,
				true));
		mole = new FramesAnimation();
		mole.addFrame(new Frame("@drawable/mole1.png", 5000));
		mole.addFrame(new Frame("@drawable/mole2.png", 500));

		int margin = 60;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 2; j++) {
				EAdSceneElement hole = getHole(200 * i + margin, 200 * j
						+ margin);
				this.getSceneElements().add(hole);
				this.getSceneElements().add(
						getMole(200 * i + margin, 200 * j + margin));

			}
	}

	private EAdSceneElement getHole(int x, int y) {
		EAdBasicSceneElement hole = new EAdBasicSceneElement("hole");
		hole.getResources().addAsset(hole.getInitialBundle(),
				EAdBasicSceneElement.appearance, holeImage);
		hole.setPosition(x, y);
		return hole;
	}

	private EAdSceneElement getMole(int x, int y) {
		EAdBasicSceneElement mole = new EAdBasicSceneElement("mole");
		mole.getResources().addAsset(mole.getInitialBundle(),
				EAdBasicSceneElement.appearance, this.mole);
		mole.setPosition(x, y);
		int initTime = (int) Math.round(Math.random() * 5500);
		mole.getVars().getVar(EAdSceneElementVars.VAR_TIME_DISPLAYED)
				.setInitialValue(initTime);
		

		EAdEffect effect = EAdElementsFactory
				.getInstance()
				.getEffectFactory()
				.getChangeVarValueEffect(
						mole.getVars().getVar(EAdSceneElementVars.VAR_VISIBLE),
						BooleanOperation.FALSE_OP);
		
		mole.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, effect);
		
		return mole;

	}

}
