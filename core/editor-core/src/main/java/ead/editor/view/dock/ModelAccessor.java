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

package ead.editor.view.dock;

import ead.editor.model.DependencyNode;
import java.util.NoSuchElementException;

/**
 * Abstracted model access for editing purposes. Allows element access,
 * element creation, and shallow copies.
 *
 * @author mfreire
 */
public interface ModelAccessor {

    /**
     * Gets the model element with id 'id'.
     * @throws NoSuchElementException if not found.
     * @param id of element (assigned by editor when project is imported)
     * @return element with id as its editor-id
     */
    DependencyNode getElement(String id);

    /**
     * Creates a new empty model element of type class).
     * @param type of element
     * @return brand new, unattached element of correct type, with a unique
     * editor-id
     */
    DependencyNode createElement(Class<? extends DependencyNode> type);

    /**
     * Creates a new shallow copy of an element. The copy will be unattached,
     * will reference the same references as the original (not copies), and
     * will have a unique editor-id.
     * @param e element to copy
     * @return unattached shallow copy of element, with a unique id
     */
    DependencyNode copyElement(DependencyNode e);
}
