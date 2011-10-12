package es.eucm.eadventure.editor.control.elements.impl;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.editor.view.generics.impl.ElementOption;
import es.eucm.eadventure.editor.view.generics.impl.FieldDescriptorImpl;
import es.eucm.eadventure.editor.view.generics.impl.EAdListFieldDescriptorImpl;
import es.eucm.eadventure.editor.view.generics.impl.EAdListOption;
import es.eucm.eadventure.editor.view.generics.impl.PanelImpl;

public class EAdSceneController extends AbstractElementController<EAdScene> {

	@Override
	public Panel getPanel(View view) {
		Panel panel = new PanelImpl(null);

		switch (view) {
		case ADVANCED:

			break;
		case SIMPLE:

			break;
		case EXPERT:
		default:
			panel.addElement(new EAdStringOption(
							"name",
							"this is the name",
							new FieldDescriptorImpl<EAdString>(element, "name"),
							EAdStringOption.ExpectedLength.SHORT));
			panel.addElement(
					new EAdStringOption("documentation",
							"this is the documentation",
							new FieldDescriptorImpl<EAdString>(element,
									"documentation")));
			panel.addElement(
					new ElementOption<EAdSceneElement>("background",
							"this is the scene background",
							new FieldDescriptorImpl<EAdSceneElement>(element,
									"background")));
			panel.addElement(
					new EAdListOption<EAdSceneElement>("component list",
							"this is the list of components of the scene",
							new EAdListFieldDescriptorImpl<EAdSceneElement>(element,
									"components",
									element.getElements())));
			break;
		}

		return panel;
	}

}
