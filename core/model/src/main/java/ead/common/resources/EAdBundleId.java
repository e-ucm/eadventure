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

package ead.common.resources;

/**
 * EAdBundleIds are used to uniquely identify each AssetBundle of
 * a EAdResource.
 */
public class EAdBundleId implements Comparable<EAdBundleId>{

	/**
	 * Id of the bundle
	 */
	private String bundleId;
	
	/**
	 * Construct a bundle id from a string id.
	 * 
	 * @param bundleId the string of the id.
	 */
	public EAdBundleId(String bundleId) {
		if (bundleId == null) {
			throw new IllegalArgumentException("Attempting to set null bundle-id");
		}
		this.bundleId = bundleId;
	}

	/**
	 * Gets the bundle id.
	 * 
	 * @return the bundleId
	 */
	public String getBundleId() {
		return bundleId;
	}

	/**
	 * Sets the bundle id.
	 * 
	 * @param bundleId the bundleId to set
	 */
	public void setBundleId(String bundleId) {
		if (bundleId == null) {
			throw new IllegalArgumentException("Attempting to set null bundle-id");
		}
		this.bundleId = bundleId;
	}

	@Override
	public int compareTo(EAdBundleId o) {
		return this.bundleId.compareTo(o.getBundleId());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof EAdBundleId) {
			return bundleId.equals(((EAdBundleId) o).bundleId);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return bundleId;
	}
	
	@Override
	public int hashCode(){
		return bundleId.hashCode();
	}
	
}
