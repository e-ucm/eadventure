package es.eucm.eadventure.editor.control.elements.impl;

import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.editor.view.generics.Panel;
import es.eucm.eadventure.editor.view.generics.impl.EAdConditionOption;
import es.eucm.eadventure.editor.view.generics.impl.EAdStringOption;
import es.eucm.eadventure.editor.view.generics.impl.FieldDescriptorImpl;
import es.eucm.eadventure.editor.view.generics.impl.PanelImpl;

public class EAdSceneElementDefController extends AbstractElementController<EAdSceneElementDef> {

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
							new FieldDescriptorImpl<EAdString>(element, "name"),
							EAdStringOption.ExpectedLength.SHORT));

			panel.addElement(
					new EAdStringOption("description",
							"this is the description",
							new FieldDescriptorImpl<EAdString>(element,
									"desc")));

			panel.addElement(
					new EAdStringOption("detailed description",
							"this is the detailed description",
							new FieldDescriptorImpl<EAdString>(element,
									"detailedDesc"),
							EAdStringOption.ExpectedLength.LONG));

			panel.addElement(
					new EAdStringOption(Messages.documentation,
							"this is the documentation",
							new FieldDescriptorImpl<EAdString>(element,
									"doc"),
							EAdStringOption.ExpectedLength.LONG));
			
			panel.addElement(
					new EAdConditionOption("draggable condition",
							"edit draggable condition",
							new FieldDescriptorImpl<EAdCondition>(element,
									"dragCond"),
							EAdConditionOption.View.DETAILED));
			
		
			break;
		}

		return panel;
	}

}
