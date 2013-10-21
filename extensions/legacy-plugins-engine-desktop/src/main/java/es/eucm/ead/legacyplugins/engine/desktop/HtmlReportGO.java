package es.eucm.ead.legacyplugins.engine.desktop;

import com.google.inject.Inject;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.gameobjects.effects.AbstractEffectGO;
import es.eucm.ead.legacyplugins.model.HtmlReportEf;
import es.eucm.ead.model.elements.operations.Operation;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.tools.StringHandler;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlReportGO extends AbstractEffectGO<HtmlReportEf> {

	private static JFileChooser fileChooser;

	private StringHandler stringHandler;

	@Inject
	public HtmlReportGO(Game game, StringHandler stringHandler) {
		super(game);
		this.stringHandler = stringHandler;
	}

	public void initialize() {

		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser
					.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}

		fileChooser.setDialogTitle(stringHandler.getString(new EAdString(
				"engine.FinalReport")));

		File f = new File(fileChooser.getCurrentDirectory(), "report.html");
		fileChooser.setSelectedFile(f);

		new Thread(new Runnable() {

			@Override
			public void run() {
				File f = null;
				boolean goOn = false;
				boolean save = false;

				while (!goOn) {
					int option = fileChooser.showSaveDialog(null);
					if (option == JFileChooser.APPROVE_OPTION) {
						save = true;
						try {
							f = fileChooser.getSelectedFile();
							if (!f.getCanonicalPath().endsWith(".html")) {
								f = new File(f.getParent(), f.getName()
										+ ".html");
							}

							if (f.exists()) {
								goOn = (JOptionPane
										.showConfirmDialog(
												null,
												stringHandler
														.getString(new EAdString(
																"engine.FileExists.Overwrite")),
												stringHandler
														.getString(new EAdString(
																"engine.Overwrite")),
												JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
							} else {
								goOn = true;
							}
						} catch (IOException e) {
						}
					} else {
						goOn = true;
					}
				}
				if (save && f != null) {
					FileWriter writer = null;
					try {
						writer = new FileWriter(f);
						writer
								.write("<!DOCTYPE html><html><head><meta charset='utf-8'/> </head><style>div {\tmargin: auto;\twidth: 800px;}h1 {\tfont-size: 16px;    font-family: 'Lucida Sans Unicode','Lucida Grande',Sans-Serif;}#gradient-style {    border-collapse: collapse;    font-family: 'Lucida Sans Unicode','Lucida Grande',Sans-Serif;    font-size: 12px;    margin: 20px;    text-align: left;    width: 480px;}#gradient-style th {    background: url('http://media.smashingmagazine.com/images/express-css-table-design/table-images/gradhead.png') repeat-x scroll 0 0 #B9C9FE;    border-bottom: 1px solid #FFFFFF;    border-top: 2px solid #D3DDFF;    color: #003399;    font-size: 13px;    font-weight: normal;    padding: 8px;}#gradient-style td {    background: url('http://media.smashingmagazine.com/images/express-css-table-design/table-images/gradback.png') repeat-x scroll 0 0 #E8EDFF;    border-bottom: 1px solid #FFFFFF;    border-top: 1px solid #FFFFFF;    color: #666699;    padding: 8px;}#gradient-style tfoot tr td {    background: none repeat scroll 0 0 #E8EDFF;    color: #9999CC;    font-size: 12px;}#gradient-style tbody tr:hover td {    background: url('http://media.smashingmagazine.com/images/express-css-table-design/table-images/gradhover.png') repeat-x scroll 0 0 #D0DAFD;    color: #333399;}</style><body><div><h1>Informe final:</h1><table id='gradient-style'><thead><tr><th>Variable</th><th>Valor</td></tr></thead><tbody>");
						int i = 0;
						for (Operation operation : effect.getOperations()) {
							String value = game.getGameState().operate(
									operation)
									+ "";
							String label = stringHandler.getString(effect
									.getLabels().get(i));
							writer.write("<tr><td>" + label + "</td><td>"
									+ value + "</td></tr>");
							i++;
						}
						writer.write("</tbody></table></div></body></html>");
						writer.close();

					} catch (Exception e) {
						if (writer != null) {
							try {
								writer.close();
							} catch (IOException e1) {

							}
						}
					}
				}
			}

		}).start();
	}
}
