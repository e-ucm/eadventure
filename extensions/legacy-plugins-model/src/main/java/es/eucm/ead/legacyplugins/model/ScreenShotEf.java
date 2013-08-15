package es.eucm.ead.legacyplugins.model;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.elements.effects.AbstractEffect;
import es.eucm.ead.model.params.text.EAdString;

/**
 * @author anserran
 *         Date: 20/05/13
 *         Time: 11:28
 */
@Element
public class ScreenShotEf extends AbstractEffect {

	@Param
	private EAdString dialogTitle;

	@Param
	private EAdString fileName;

	public ScreenShotEf() {

	}

	public ScreenShotEf(EAdString dialogTitle, EAdString fileName) {
		this.dialogTitle = dialogTitle;
		this.fileName = fileName;
	}

	public EAdString getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(EAdString dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public EAdString getFileName() {
		return fileName;
	}

	public void setFileName(EAdString fileName) {
		this.fileName = fileName;
	}
}
