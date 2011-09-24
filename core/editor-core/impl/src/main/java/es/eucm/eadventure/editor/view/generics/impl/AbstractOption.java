package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.editor.view.generics.Option;

public class AbstractOption implements Option {

	private String title;
	
	private String toolTipText;
	
	public AbstractOption(String title, String toolTipText) {
		this.title = title;
		this.toolTipText = toolTipText;
	}
	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getToolTipText() {
		return toolTipText;
	}

}
