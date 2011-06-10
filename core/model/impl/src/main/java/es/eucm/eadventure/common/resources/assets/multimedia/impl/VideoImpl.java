package es.eucm.eadventure.common.resources.assets.multimedia.impl;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;

public class VideoImpl implements Video {

	@Param("uri")
	private String uri;

	public VideoImpl() {
		this(null);
	}
	
	public VideoImpl(String uri) {
		this.uri = uri;
	}
	
	@Override
	public String getURI() {
		return uri;
	}

}
