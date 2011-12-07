package es.eucm.eadventure.editor.control.elements.impl;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.editor.view.generics.impl.ElementOption;
import es.eucm.eadventure.editor.view.generics.impl.FieldDescriptorImpl;
import es.eucm.eadventure.editor.view.generics.impl.PanelImpl;
import es.eucm.eadventure.editor.view.generics.scene.impl.PreviewPanelImpl;

public class EAdSceneController extends AbstractElementController<EAdScene> {

	@Override
	public Panel getPanel(View view) {
		Panel panel = new PanelImpl(null, Panel.LayoutPolicy.VERTICAL);

		switch (view) {
		case ADVANCED:

			break;
		case SIMPLE:

			break;
		case EXPERT:
		default:
			panel.addElement(new EAdStringOption(
							Messages.name,
							"this is the name",
							new FieldDescriptorImpl<EAdString>(element.getDefinition(), "name"),
							EAdStringOption.ExpectedLength.SHORT));
			
			panel.addElement(
					new EAdStringOption(Messages.documentation,
							"this is the documentation",
							new FieldDescriptorImpl<EAdString>(element.getDefinition(),
									"doc"),
							EAdStringOption.ExpectedLength.LONG));
			
			panel.addElement(
					new ElementOption<EAdSceneElement>(Messages.background,
							"this is the scene background",
							new FieldDescriptorImpl<EAdSceneElement>(element,
									"background")));
		
			panel.addElement(
					new PreviewPanelImpl(element));
			break;
		}

		return panel;
	}

}
