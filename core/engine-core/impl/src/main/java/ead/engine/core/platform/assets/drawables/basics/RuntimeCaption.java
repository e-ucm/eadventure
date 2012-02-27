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

package ead.engine.core.platform.assets.drawables.basics;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.elements.variables.SystemFields;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.basics.EAdCaption;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.EAdRectangle;
import ead.common.util.StringHandler;
import ead.engine.core.game.GameLoop;
import ead.engine.core.game.VariableMap;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.DrawableAsset;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.RuntimeFont;
import ead.engine.core.platform.assets.AbstractRuntimeAsset;
import ead.engine.core.platform.rendering.GenericCanvas;


public class RuntimeCaption<GraphicContext> extends
		AbstractRuntimeAsset<EAdCaption> implements
		DrawableAsset<EAdCaption, GraphicContext> {

	private static final Logger logger = LoggerFactory.getLogger("RuntimeCaption");

	private AssetHandler assetHandler;

	/**
	 * Average time used to read one word, in milliseconds
	 */
	private static final int TIME_FOR_WORD = 300;

	/**
	 * Minimum time a text will be shown
	 */
	private static final int MINIMUM_TIME_TEXT = 1400;

	protected String text;

	protected List<String> lines;

	protected List<Integer> widths;

	protected RuntimeFont font;

	protected EAdRectangle bounds;

	protected float alpha;

	protected int lineHeight;

	protected FontHandler fontCache;

	private int linesInPart;

	/**
	 * When some text is too long, it could be divided separate parts that will
	 * be shown one by one
	 * 
	 */
	protected int totalParts;

	protected int currentPart;

	/**
	 * Times the text has been read (shown entirely at the screen)
	 */
	protected int timesRead;

	/**
	 * Time the current text must be shown to be completely read
	 */
	private int timeShown;

	/**
	 * Times the text loops after it gets to its last part. If -1, loops
	 * infinite
	 */
	private int loops;

	private int heightOffset;

	/**
	 * Current text wrapped
	 */
	private String currentText;

	private VariableMap valueMap;

	private StringHandler stringsReader;

	@Inject
	public RuntimeCaption(FontHandler fontCache, VariableMap valueMap,
			StringHandler stringsReader, AssetHandler assetHandler) {
		this.fontCache = fontCache;
		this.valueMap = valueMap;
		this.stringsReader = stringsReader;
		this.assetHandler = assetHandler;
		logger.info("New instance");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#loadAsset()
	 */
	@Override
	public boolean loadAsset() {
		font = fontCache.get(descriptor.getFont());
		if (descriptor.getFields().size() > 0)
			text = valueMap.processTextVars(
					stringsReader.getString(descriptor.getText()),
					descriptor.getFields());
		else
			text = stringsReader.getString(descriptor.getText());

		lines = new ArrayList<String>();
		widths = new ArrayList<Integer>();
		wrapText();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#freeMemory()
	 */
	@Override
	public void freeMemory() {
		// DO NOTHING
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#isLoaded()
	 */
	@Override
	public boolean isLoaded() {
		return lines != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#update(es.eucm.
	 * eadventure.engine.core.GameState)
	 */
	@Override
	public void update() {
		if (!isLoaded())
			loadAsset();

		timeShown -= GameLoop.SKIP_MILLIS_TICK;
		if (timeShown <= 0) {
			goForward(1);
		}

		if (descriptor.getFields().size() > 0) {
			text = valueMap.processTextVars(
					stringsReader.getString(descriptor.getText()),
					descriptor.getFields());
		} else {
			text = stringsReader.getString(descriptor.getText());
		}

		// If text has changed
		if (!currentText.equals(text))
			wrapText();
	}

	@Override
	public int getWidth() {
		if (bounds == null)
			return 1;
		return bounds.width;
	}

	@Override
	public int getHeight() {
		if (bounds == null)
			return 1;
		return bounds.height;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends EAdDrawable> DrawableAsset<S, GraphicContext> getDrawable() {
		return (DrawableAsset<S, GraphicContext>) this;
	}

	private void wrapText() {
		this.currentText = text;
		lines.clear();
		widths.clear();
		totalParts = 0;
		bounds = new EAdRectangle(0, 0, 0, 0);
		lineHeight = font.lineHeight();

		int preferredWidth = 0;

		switch (descriptor.getPreferredWidth()) {
		case EAdCaption.AUTO_SIZE:
			preferredWidth = Integer.MAX_VALUE;
			break;
		case EAdCaption.SCREEN_SIZE:
			preferredWidth = valueMap.getValue(SystemFields.GAME_WIDTH);
			break;
		default:
			preferredWidth = descriptor.getPreferredWidth();
		}
		preferredWidth -= descriptor.getPadding() * 2;

		bounds.width = 0;
		String[] words = text.split(" ");
		if (words.length == 0) {
			words = new String[] { text };
		}

		// Current line
		String line = "";
		int contWord = 0;

		int currentLineWidth = 0;

		while (contWord < words.length) {

			int nextWordWidth = font.stringWidth(words[contWord] + " ");

			if (currentLineWidth + nextWordWidth <= preferredWidth) {
				currentLineWidth += nextWordWidth;
				line += words[contWord++] + " ";
			} else if (line != "") {
				lines.add(line);
				currentLineWidth = font.stringWidth(line);
				widths.add(currentLineWidth);
				bounds.width = currentLineWidth > bounds.width ? currentLineWidth
						: bounds.width;
				currentLineWidth = 0;
				line = "";
			} else {
				line = splitLongWord(font, lines, words[contWord++],
						preferredWidth);
				currentLineWidth = font.stringWidth(line);
			}
		}

		if (line != "") {
			lines.add(line);
			currentLineWidth = font.stringWidth(line);
			widths.add(currentLineWidth);
			bounds.width = currentLineWidth > bounds.width ? currentLineWidth
					: bounds.width;
		}

		int preferredHeight = 0;
		switch (descriptor.getPreferredHeight()) {
		case EAdCaption.SCREEN_SIZE:
			preferredHeight = valueMap.getValue(SystemFields.GAME_HEIGHT);
			break;
		case EAdCaption.AUTO_SIZE:
			preferredHeight = Integer.MAX_VALUE;
			break;
		default:
			preferredHeight = descriptor.getPreferredHeight();
		}
		preferredHeight -= descriptor.getPadding() * 2;

		linesInPart = preferredHeight / lineHeight;
		linesInPart = linesInPart < lines.size() ? linesInPart : lines.size();
		totalParts = (int) Math
				.ceil((float) lines.size() / (float) linesInPart);
		bounds.height = descriptor.getPreferredHeight() == EAdCaption.AUTO_SIZE ? linesInPart
				* lineHeight
				: preferredHeight;

		bounds.width = descriptor.getPreferredWidth() == EAdCaption.AUTO_SIZE ? bounds.width
				: preferredWidth;

		heightOffset = descriptor.getPreferredHeight() != EAdCaption.AUTO_SIZE ? (preferredHeight - (linesInPart * lineHeight)) / 2
				: 0;

		bounds.width += descriptor.getPadding() * 2;
		bounds.height += descriptor.getPadding() * 2;

		reset();
	}

	private String splitLongWord(RuntimeFont f, List<String> lines,
			String word, int lineWidth) {

		boolean finished = false;
		String currentLine = "";

		int i = 0;
		while (!finished) {
			currentLine = "";

			while (i < word.length()
					&& f.stringWidth(currentLine + word.charAt(i)) < lineWidth) {
				currentLine += word.charAt(i++);
			}

			if (i == word.length()) {
				finished = true;
			} else {
				lines.add(currentLine);
				int currentLineWidth = f.stringWidth(currentLine);
				widths.add(currentLineWidth);
				bounds.width = currentLineWidth > bounds.width ? currentLineWidth
						: bounds.width;
			}

		}
		return currentLine;

	}

	/**
	 * If text is divided in parts and current part is n, this method advances
	 * the text to n + i part
	 * 
	 * @param i
	 *            steps to go forward
	 */
	public void goForward(int i) {
		if (totalParts > 0) {
			currentPart += i;
			if (currentPart >= totalParts) {
				while (currentPart >= totalParts) {
					currentPart -= totalParts;
					loops--;
					timesRead++;
				}
				if (loops <= 0)
					currentPart = totalParts - 1;
			}
		}
		updateTimeShown();
	}

	public int getCurrentPart() {
		return currentPart;
	}

	public int getTotalParts() {
		return totalParts;
	}

	/**
	 * Updates the time a text must be shown to be completely read
	 */
	private void updateTimeShown() {
		// FIXME Deberia venir de un controlador con las opciones de la rapidez
		// de lectura del texto
		int multiplier = 1;
		timeShown = 0;
		for (String s : getText()) {
			timeShown += (int) (TIME_FOR_WORD * s.split(" ").length * multiplier);
		}

		if (timeShown < (int) (MINIMUM_TIME_TEXT * multiplier))
			timeShown = (int) (MINIMUM_TIME_TEXT * multiplier);
	}

	public List<String> getText() {
		int beginIndex = currentPart * linesInPart;
		int lastIndex = beginIndex + linesInPart;
		lastIndex = lastIndex > lines.size() ? lines.size() : lastIndex;
		return lines.subList(beginIndex, lastIndex);
	}

	public void setHasBuble(boolean b) {
	}

	/**
	 * Returns the number of times the text has been read by the player. This
	 * calculation is made from an reading time estimation
	 * 
	 * @return the number of times the text has been read by the player. This
	 *         calculation is made from an reading time estimation
	 */
	public int getTimesRead() {
		return timesRead;
	}

	/**
	 * Sets how many times this text game object loops before adding one to
	 * times read. Negative number will be interpreted as infinitum
	 * 
	 * @param loops
	 *            times text loops
	 */
	public void setLoops(int loops) {
		this.loops = loops;
	}

	public void reset() {
		currentPart = 0;
		timesRead = 0;
		loops = 0;
		updateTimeShown();
	}

	public EAdCaption getCaption() {
		return descriptor;
	}

	public EAdRectangle getBounds() {
		return bounds;
	}

	public int getLineHeight() {
		return this.font.lineHeight();
	}

	public RuntimeFont getFont() {
		return font;
	}

	public void render(GenericCanvas<GraphicContext> c) {
		// Draw bubble
		if (getAssetDescriptor().hasBubble()) {
			RectangleShape shape = new RectangleShape(getWidth(), getHeight());
			shape.setPaint(getAssetDescriptor().getBubblePaint());
			assetHandler.getDrawableAsset(shape, c).render(c);
		}

		
		c.setFont(descriptor.getFont());
		int xOffset = 0;
		int yOffset = getAssetDescriptor().getPadding();
		if (currentPart == totalParts - 1 && lines.size() % linesInPart != 0) {
			yOffset += (bounds.height - getAssetDescriptor().getPadding() * 2 - ((lines
					.size() % linesInPart) * lineHeight)) / 2;
		} else {
			yOffset += heightOffset;
		}

		int i = currentPart * linesInPart;
		// Draw lines
		try {
			for (String s : getText()) {
				switch (descriptor.getAlignment()) {
				case CENTER:
					xOffset = (bounds.width - widths.get(i)) / 2;
					break;
				case RIGHT:
					xOffset = (bounds.width - widths.get(i))
							- descriptor.getPadding();
					break;
				default:
					xOffset = descriptor.getPadding();
				}
				c.setPaint(descriptor.getTextPaint());
				c.drawText(s, xOffset, yOffset);
				yOffset += getLineHeight();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean contains(int x, int y) {
		return x > 0 && y > 0 && x < getWidth() && y < getHeight();
	}

}
