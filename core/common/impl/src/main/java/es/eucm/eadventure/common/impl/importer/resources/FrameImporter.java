package es.eucm.eadventure.common.impl.importer.resources;

import com.google.inject.Inject;

import es.eucm.eadventure.common.GenericImporter;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.Frame;

public class FrameImporter implements GenericImporter<es.eucm.eadventure.common.data.animation.Frame, Frame>{

	private ResourceImporter resourceImporter;
	
	@Inject
	public FrameImporter( ResourceImporter resourceImporter ){
		this.resourceImporter = resourceImporter;
	}
	
	@Override
	public Frame convert(
			es.eucm.eadventure.common.data.animation.Frame oldObject, Object newElement) {
		// FIXME Frame sounds not imported
		long time = oldObject.getTime();
		String oldURI = oldObject.getUri();
		String newURI = resourceImporter.getURI(oldURI);
		Frame frame = new Frame( newURI, (int) time );
		return frame;
	}
	
	@Override
	public Frame init(es.eucm.eadventure.common.data.animation.Frame oldObject) {
		return null;
	}

}
