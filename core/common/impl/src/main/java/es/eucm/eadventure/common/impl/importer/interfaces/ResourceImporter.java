package es.eucm.eadventure.common.impl.importer.interfaces;

import java.util.List;
import java.util.Map;

import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.model.EAdElement;

public interface ResourceImporter {

	/**
	 * Returns the new URI for an old resource situated in oldURI
	 * 
	 * @param oldURI
	 *            the old URI
	 * @return the new URI for the resource. {@code null} it there was any
	 *         problem with the import
	 */
	String getURI(String oldURI);
	
	/**
	 * Set the paths for the resources importer
	 * 
	 * @param oldAventurePath
	 *            Absolute path where the old adventure is placed
	 * @param newAdventurePath
	 *            Absolute path where the new adventure must be placed
	 */
	public void setPaths(String oldAventurePath, String newAdventurePath);

	void importResources(EAdElement element, List<Resources> resources,
			Map<String, String> resourcesStrings,
			Map<String, Class<?>> resourcesClasses);
}
