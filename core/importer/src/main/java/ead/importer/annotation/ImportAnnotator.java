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

package ead.importer.annotation;

import ead.common.model.EAdElement;

/**
 * Used to annotate a group of imported elements with a key-value properties.
 * Allows structure to be preserved, instead of being "lost in translation"
 * because it is irrelevant to game execution. This structure makes editing
 * a lot easier.
 *
 * @author mfreire
 */
public interface ImportAnnotator {

    public enum Type {
        /**
		 * Entry. A generic key-value entry. The first value is interpreted as
         * the key, and the second as the actual value
         * Key capitalization and whitespace is important.
		 */
        Entry,

        /**
		 * Comment. Ignored, but may be useful for human-readable debugging.
         * Values should be the corresponding comments.
		 */
        Comment,

        /**
		 * Opens a context.
		 * All annotations until the close() of this object imply that
         * whatever is being annotated is part of this context.
         * If element != null, value will be ignored. Otherwise, value should
         * be a unique ID string.
		 */
        Open,

        /**
		 * Closes a context.
         * Indicates that new annotations no longer belong to this context.
		 * Element and value should match those of the corresponding 'open'
		 */
        Close
    }

    /**
     * Annotate an element created by an importer.
     * @param element element to annotate
     * @param key key to annotate it with
     * @param value value to set this key to (optional for some key types)
     */
    public void annotate(EAdElement element, Type key, Object ... values);
}
