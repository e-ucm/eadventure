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

package es.eucm.eadventure.editor.view.swing;

import javax.swing.JComponent;

import com.google.inject.Inject;

import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.editor.control.CommandManager;
import es.eucm.eadventure.editor.control.FieldValueReader;
import es.eucm.eadventure.editor.view.ComponentProvider;
import es.eucm.eadventure.editor.view.generics.impl.BooleanOption;
import es.eucm.eadventure.editor.view.generics.impl.EAdConditionOption;
import es.eucm.eadventure.editor.view.generics.impl.EAdListOption;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.editor.view.generics.impl.ElementOption;
import es.eucm.eadventure.editor.view.generics.impl.PanelImpl;
import es.eucm.eadventure.editor.view.generics.impl.SceneInterfaceElement;
import es.eucm.eadventure.editor.view.generics.impl.TextOption;
import es.eucm.eadventure.editor.view.generics.scene.PreviewPanel;
import es.eucm.eadventure.editor.view.impl.AbstractProviderFactory;
import es.eucm.eadventure.editor.view.swing.componentproviders.BooleanComponentProvider;
import es.eucm.eadventure.editor.view.swing.componentproviders.EAdConditionComponentProvider;
import es.eucm.eadventure.editor.view.swing.componentproviders.EAdListComponentProvider;
import es.eucm.eadventure.editor.view.swing.componentproviders.EAdStringComponentProvider;
import es.eucm.eadventure.editor.view.swing.componentproviders.ElementComponentProvider;
import es.eucm.eadventure.editor.view.swing.componentproviders.PanelComponentProvider;
import es.eucm.eadventure.editor.view.swing.componentproviders.TextComponentProvider;
import es.eucm.eadventure.editor.view.swing.scene.PreviewPanelComponentProvider;
import es.eucm.eadventure.editor.view.swing.scene.SceneEditionComponentProvider;

/**
 * Java Swing {@link ProviderFactory}
 */
public class SwingProviderFactory extends AbstractProviderFactory<JComponent> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Inject
	public SwingProviderFactory(StringHandler stringHandler,
			CommandManager commandManager) {
		super();
		FieldValueReader fieldValueReader = new SwingFieldValueReader();

		this.addToMap(TextOption.class,
				(ComponentProvider) new TextComponentProvider(fieldValueReader, commandManager));
		this.addToMap(EAdStringOption.class,
				(ComponentProvider) new EAdStringComponentProvider(
						stringHandler, fieldValueReader, commandManager));
		this.addToMap(BooleanOption.class,
				(ComponentProvider) new BooleanComponentProvider(
						fieldValueReader, commandManager));
		this.addToMap(PanelImpl.class,
				(ComponentProvider) new PanelComponentProvider(this));
		this.addToMap(ElementOption.class,
				(ComponentProvider) new ElementComponentProvider());
		this.addToMap(
				EAdListOption.class,
				(ComponentProvider) new EAdListComponentProvider(commandManager));
		this.addToMap(EAdConditionOption.class,
				(ComponentProvider) new EAdConditionComponentProvider(
						fieldValueReader));
		this.addToMap(SceneInterfaceElement.class,
				(ComponentProvider) new SceneEditionComponentProvider(
						commandManager));
		this.addToMap(PreviewPanel.class,
				(ComponentProvider) new PreviewPanelComponentProvider(
						commandManager));

	}

}
