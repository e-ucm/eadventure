package ead.plugins.engine.screenshot;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.effects.AbstractEffect;
import ead.common.model.params.text.EAdString;

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
