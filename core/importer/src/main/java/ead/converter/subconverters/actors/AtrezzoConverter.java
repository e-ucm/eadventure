package ead.converter.subconverters.actors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.converter.UtilsConverter;
import ead.converter.resources.ResourceConverter;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;

@Singleton
public class AtrezzoConverter extends ElementConverter {

	@Inject
	public AtrezzoConverter(ResourceConverter resourceConverter,
			UtilsConverter utilsConverter) {
		super(resourceConverter, utilsConverter);
	}

	@Override
	public String getResourceType() {
		return Atrezzo.RESOURCE_TYPE_IMAGE;
	}
}
