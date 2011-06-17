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

package es.eucm.eadventure.engine.core.platform.assets.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.params.EAdRectangle;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.impl.VariableMap;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeFont;
import es.eucm.eadventure.engine.core.platform.impl.FontCacheImpl;

public class RuntimeCaption implements DrawableAsset<Caption> {

	private Logger logger = Logger.getLogger("RuntimeCaption");

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

	protected RuntimeFont font;

	protected EAdRectangle bounds;

	protected float alpha;

	protected int lineHeight;

	protected FontCacheImpl fontCache;

	private int linesInPart;

	protected Caption caption;

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

	private VariableMap valueMap;

	private StringHandler stringHandler;
	
	private PlatformConfiguration platformConfiguration;

	@Inject
	public RuntimeCaption(FontCacheImpl fontCache, VariableMap valueMap,
			StringHandler stringHandler, PlatformConfiguration platformConfiguration) {
		this.fontCache = fontCache;
		this.valueMap = valueMap;
		this.stringHandler = stringHandler;
		this.platformConfiguration = platformConfiguration;
		logger.info("New instance");
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#loadAsset()
	 */
	@Override
	public boolean loadAsset() {
		font = fontCache.get(caption.getFont());
		text = valueMap.processTextVars(stringHandler.getString(caption
				.getText()));
		lines = new ArrayList<String>();
		wrapText();
		return true;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#freeMemory()
	 */
	@Override
	public void freeMemory() {
		//DO NOTHING
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#isLoaded()
	 */
	@Override
	public boolean isLoaded() {
		return lines != null;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#setDescriptor(es.eucm.eadventure.common.resources.assets.AssetDescriptor)
	 */
	@Override
	public void setDescriptor(Caption descriptor) {
		this.caption = descriptor;
		loadAsset();
		// minimumHeight = font == null || element.getMinimumHeight() >
		// font.lineHeight() ? element.getMinimumHeight() : font.lineHeight();
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#update(es.eucm.eadventure.engine.core.GameState)
	 */
	@Override
	public void update(GameState state) {
		if (!isLoaded())
			loadAsset();
		
		timeShown -= GameLoop.SKIP_MILLIS_TICK;
		if (timeShown <= 0) {
			goForward(1);
		}

		text = valueMap.processTextVars(stringHandler.getString(caption
				.getText()));
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

	public float getAlpha() {
		return caption.getAlpha();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Drawable> DrawableAsset<S> getDrawable() {
		return (DrawableAsset<S>) this;
	}

	private void wrapText() {		
		lines = new ArrayList<String>();
		totalParts = 0;
		bounds = new EAdRectangle(0, 0, 0, 0);
		lineHeight = font.lineHeight();
		
		int maximumWidth = (int) (caption.getMaximumWidth() == Caption.SCREEN_SIZE ? platformConfiguration.getWidth() / platformConfiguration.getScale() : caption.getMaximumWidth());
		int maximumHeight = (int) (caption.getMaximumWidth() < 0 ? platformConfiguration.getHeight() : caption.getMaximumHeight());
		

		// If width for drawing the text is infinite, we have only one line
		if (maximumWidth == Caption.INFINITE_SIZE ) {
			lines.add(text);
			totalParts = 1;
			int lineWidth = font.stringWidth(text);
			bounds.width = lineWidth > caption.getMinimumWidth() ? lineWidth
					: caption.getMinimumWidth();
		} else {
			bounds.width = caption.getMinimumWidth() > 0 ? caption
					.getMinimumWidth() : 0;
			String[] words = text.split(" ");

			// Current line
			String line = "";
			int contWord = 0;

			int currentLineWidth = 0;

			while (contWord < words.length) {

				int nextWordWidth = font.stringWidth(words[contWord] + " ");

				if (currentLineWidth + nextWordWidth <= maximumWidth ) {
					currentLineWidth += nextWordWidth;
					line += words[contWord++] + " ";
				} else if (line != "") {
					lines.add(line);
					bounds.width = currentLineWidth > bounds.width ? currentLineWidth
							: bounds.width;
					currentLineWidth = 0;
					line = "";
				} else {
					line = splitLongWord(font, lines, words[contWord++],
							maximumWidth);
					currentLineWidth = font.stringWidth(line);
				}
			}

			if (line != "") {
				lines.add(line);
				currentLineWidth = font.stringWidth(line);
				bounds.width = currentLineWidth > bounds.width ? currentLineWidth
						: bounds.width;
			}

		}

		linesInPart = maximumHeight / lineHeight;
		linesInPart = linesInPart < lines.size() ?  linesInPart : lines.size();
		totalParts = (int) Math
				.ceil((float) lines.size() / (float) linesInPart);

		bounds.height = linesInPart * lineHeight;
				
		if ( caption.hasBubble() ){
			bounds.width += caption.getPadding() * 2;
			bounds.height += caption.getPadding() * 2;
		}
	}

	private String splitLongWord(RuntimeFont f, List<String> lines,
			String word, int lineWidth) {

		boolean finished = false;
		String currentLine = "";

		int i = 0;
		while (!finished) {
			currentLine = "";

			while (i < word.length() && f.stringWidth(currentLine) < lineWidth) {
				currentLine += word.charAt(i++);
			}

			if (i == word.length()) {
				finished = true;
			} else {
				lines.add(currentLine);
				int currentLineWidth = f.stringWidth(currentLine);
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
		currentPart += i;
		if (currentPart >= totalParts) {
			if (loops > 0) {
				while (currentPart >= totalParts) {
					currentPart -= totalParts;
					loops--;
				}
				timesRead++;
			} else {
				currentPart = totalParts - 1;
			}
		}
		updateTimeShown();
	}

	/**
	 * Updates the time a text must be shown to be completely read
	 */
	private void updateTimeShown() {
		// FIXME Debería venir de un controlador con las opciones de la rapidez
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
	
	public Caption getCaption() {
		return caption;
	}
	
	public EAdRectangle getBounds() {
		return bounds;
	}

}
