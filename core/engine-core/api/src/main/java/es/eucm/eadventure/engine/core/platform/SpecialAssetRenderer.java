package es.eucm.eadventure.engine.core.platform;

import es.eucm.eadventure.common.resources.assets.multimedia.Video;

public interface SpecialAssetRenderer<S, T> {

	T getComponent(Video asset);

	boolean isFinished();

	boolean start();

}
