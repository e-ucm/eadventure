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

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import ead.common.model.EAdElement;
import java.awt.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A multi-dockable factory that wraps an EAdElement within an EAdElement.
 * This factory is used by the main dockable-controller to wrap elements, and 
 * to save/restore views when required.
 * 
 * @author mfreire
 */
public class ClassDockableFactory implements
        MultipleCDockableFactory<DefaultMultipleCDockable, ElementLayout> {
    Logger logger = LoggerFactory.getLogger("cdf");

    private Class<? extends ElementPanel> controlClass;
    private Class<? extends EAdElement> modelClass;
    private ModelAccessor model;

    public ClassDockableFactory(
            Class<? extends ElementPanel> controlClass,
            Class<? extends EAdElement> modelClass, ModelAccessor model) {
        this.controlClass = controlClass;
        this.modelClass = modelClass;
        this.model = model;
    }

	@SuppressWarnings("unchecked")
    public ElementPanel getPanelFor(String id) {
        EAdElement e = (id != null) ?
                model.getElement(id) :
                model.createElement(modelClass);
        ElementPanel ep;
        try {
            ep = (ElementPanel)controlClass.newInstance();
        } catch (Exception ex) {
            logger.error("could not instantiate", ex);
            return null;
        }
        ep.setTarget(e);
        return ep;
    }

    public DefaultMultipleCDockable createDockable(String id ) {
        ElementPanel ep = getPanelFor(id);
        DefaultMultipleCDockable d = new DefaultMultipleCDockable(this);
        d.setCloseable(true);
        d.setTitleText(modelClass.getSimpleName() + ep.getTarget().getId());
        d.setRemoveOnClose(true);
        d.add((Component)ep);
        return d;
    }

    public String getDockableId(DefaultMultipleCDockable d) {
        return ((ElementPanel)d.getContentPane().getComponent(0))
                .getTarget().getId();
    }

    @Override
    public DefaultMultipleCDockable read( ElementLayout layout ) {
        return createDockable(layout.getId());
    }

    @Override
    public ElementLayout write( DefaultMultipleCDockable dockable ) {
        ElementLayout el = create();
        el.setId(getDockableId(dockable));
        return el;
    }

    @Override
    public boolean match(DefaultMultipleCDockable dockable, ElementLayout layout ){
    	return layout.getId().equals(getDockableId(dockable));
    }

    @Override
    public ElementLayout create() {
        return new ElementLayout();
    }
}
