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

package ead.common.importer.subimporters.books;

import java.awt.Font;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.inject.Inject;

import ead.common.EAdElementImporter;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.conditions.enums.Comparator;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.effects.InterpolationEf;
import ead.common.model.elements.effects.enums.InterpolationLoopType;
import ead.common.model.elements.effects.enums.InterpolationType;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.model.elements.variables.operations.ValueOp;
import ead.common.model.predef.effects.ChangeAppearanceEf;
import ead.common.params.fills.ColorFill;
import ead.common.resources.EAdBundleId;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.text.BasicFont;
import ead.common.resources.assets.text.EAdFont;
import ead.common.resources.assets.text.enums.FontStyle;
import ead.common.util.EAdPosition;
import ead.common.util.EAdPosition.Corner;
import ead.common.util.StringHandler;
import es.eucm.eadventure.common.data.chapter.book.Book;
import es.eucm.eadventure.common.data.chapter.book.BookParagraph;

public class BookImporter implements EAdElementImporter<Book, EAdScene> {

	private static final int BOOK_WIDTH = 800;

	/**
	 * X position of the first column of text
	 */
	public static final int TEXT_X_1 = 110;

	/**
	 * X position for the second column of text
	 */
	public static final int TEXT_X_2 = 445;

	/**
	 * Y position for both columns of text
	 */
	public static final int TEXT_Y = 75;

	/**
	 * Width of each column of text
	 */
	public static final int TEXT_WIDTH = 250;

	/**
	 * Width of each column of the bullet text
	 */
	public static final int TEXT_WIDTH_BULLET = 225;

	public static final int BULLET_WIDTH = TEXT_WIDTH - TEXT_WIDTH_BULLET;

	/**
	 * Height of each column of text
	 */
	public static final int PAGE_TEXT_HEIGHT = 500;

	/**
	 * Height of each line of text
	 */
	public static final int LINE_HEIGHT = 25;

	/**
	 * Height of each line of a title
	 */
	public static final int TITLE_HEIGHT = 50;

	private static final String HTML_NOT_SUPPORTED = "Sorry. HTML Books are no longer supported by eAdventure.";

	private FontRenderContext frc = new FontRenderContext(null, true, true);
	private Font titleFont = new Font("Arial", Font.PLAIN, 33);
	private EAdFont titleEAdFont = new BasicFont("Arial", 33, FontStyle.PLAIN);

	private Font textFont = new Font("Arial", Font.PLAIN, 18);
	private EAdFont textEAdFont = new BasicFont("Arial", 18, FontStyle.PLAIN);

	private int dispY = 0;
	private ComposedDrawable image;
	private int column;

	private StringHandler stringHandler;
	private ResourceImporter resourceImporter;

	@Inject
	public BookImporter(ResourceImporter resourceImporter,
			StringHandler stringHandler) {
		this.resourceImporter = resourceImporter;
		this.stringHandler = stringHandler;
	}

	@Override
	public EAdScene init(Book oldObject) {
		EAdScene scene = new BasicScene();
		scene.setId(oldObject.getId());
		return scene;
	}

	@Override
	public EAdScene convert(Book oldObject, Object newElement) {
		BasicScene book = (BasicScene) newElement;
		ChangeFieldEf hideInventory = new ChangeFieldEf(
				SystemFields.SHOW_INVENTORY, BooleanOp.FALSE_OP);
		SceneElementEv hideEvent = new SceneElementEv();
		hideEvent.addEffect(SceneElementEvType.ADDED_TO_SCENE, hideInventory);
		book.getEvents().add(hideEvent);
		// Import background
		AssetDescriptor background = resourceImporter.getAssetDescritptor(
				oldObject.getResources().get(0)
						.getAssetPath(Book.RESOURCE_TYPE_BACKGROUND),
				Image.class);
		book.getBackground()
				.getDefinition()
				.getResources()
				.addAsset(
						book.getBackground().getDefinition().getInitialBundle(),
						SceneElementDef.appearance, background);

		dispY = TEXT_Y;
		column = 0;
		image = new ComposedDrawable();

		ChangeFieldEf showInventory = new ChangeFieldEf(
				SystemFields.SHOW_INVENTORY, BooleanOp.TRUE_OP);

		if (oldObject.getType() == Book.TYPE_PAGES) {
			Caption captionImpl = new Caption();
			captionImpl.setFont(new BasicFont(18));
			// captionImpl.setAlignment(Alignment.CENTER);
			stringHandler.setString(captionImpl.getLabel(), HTML_NOT_SUPPORTED);
			image.addDrawable(captionImpl, 0, 0);
		} else
			for (BookParagraph p : oldObject.getParagraphs()) {
				if (p.getContent() != null && !p.getContent().equals(""))
					switch (p.getType()) {
					case BookParagraph.TITLE:
						addTextDrawable(p.getContent(), titleFont,
								titleEAdFont, 0, TITLE_HEIGHT, TEXT_WIDTH);
						break;
					case BookParagraph.TEXT:
						addTextDrawable(p.getContent(), textFont, textEAdFont,
								0, LINE_HEIGHT, TEXT_WIDTH);
						break;
					case BookParagraph.BULLET:
						if (dispY + LINE_HEIGHT > PAGE_TEXT_HEIGHT) {
							column++;
							dispY = TEXT_Y;
						}
						CircleShape bullet = new CircleShape(0, 0,
								BULLET_WIDTH / 3, 20);
						bullet.setPaint(ColorFill.BLACK);
						image.addDrawable(bullet,
								getDispX() + BULLET_WIDTH / 2, dispY
										+ LINE_HEIGHT / 2);
						addTextDrawable(p.getContent(), textFont, textEAdFont,
								BULLET_WIDTH, LINE_HEIGHT, TEXT_WIDTH_BULLET);
						break;
					case BookParagraph.IMAGE:
						Image i = (Image) resourceImporter.getAssetDescritptor(
								p.getContent(), Image.class);
						try {
							BufferedImage im = ImageIO.read(new File(
									resourceImporter.getNewProjecFolder(), i
											.getUri().toString().substring(1)));
							int height = im.getHeight();
							if (dispY + height > PAGE_TEXT_HEIGHT) {
								column++;
								dispY = TEXT_Y;
							}
							image.addDrawable(i, getDispX(), dispY);
							dispY += height;
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
			}

		SceneElement content = new SceneElement(image);
		content.setId(oldObject.getId() + "_content");

		EAdField<Integer> xField = new BasicField<Integer>(content,
				SceneElement.VAR_X);
		// Event to restart the x variable
		SceneElementEv xEvent = new SceneElementEv();
		content.getEvents().add(xEvent);
		ChangeFieldEf changeX = new ChangeFieldEf(xField, new ValueOp(0));
		xEvent.addEffect(SceneElementEvType.ADDED_TO_SCENE, changeX);

		book.setReturnable(false);
		book.getSceneElements().add(content);

		if (oldObject.getType() == Book.TYPE_PARAGRAPHS) {
			content.setPosition(Corner.TOP_LEFT, 0, 0);

			SceneElementEv event = new SceneElementEv();
			event.setId("restartBook");
			event.addEffect(SceneElementEvType.FIRST_UPDATE, new ChangeFieldEf(
					xField, new ValueOp(0)));
			content.getEvents().add(event);

			EAdCondition leftCondition = new OperationCond(xField, 0,
					Comparator.LESS);
			SceneElement leftArrow = getArrow(oldObject, content,
					Book.RESOURCE_TYPE_ARROW_LEFT_NORMAL,
					Book.RESOURCE_TYPE_ARROW_LEFT_OVER, BOOK_WIDTH,
					leftCondition);
			Point p = oldObject.getPreviousPagePoint();
			int x = 10;
			int y = 10;
			if (p != null) {
				x = p.x;
				y = p.y;
			}
			leftArrow.setPosition(x, y);

			EAdCondition rightCondition = EmptyCond.TRUE_EMPTY_CONDITION;
			SceneElement rightArrow = getArrow(oldObject, content,
					Book.RESOURCE_TYPE_ARROW_RIGHT_NORMAL,
					Book.RESOURCE_TYPE_ARROW_RIGHT_OVER, -BOOK_WIDTH,
					rightCondition);

			p = oldObject.getNextPagePoint();
			x = 790;
			y = 10;
			Corner c = Corner.TOP_RIGHT;
			if (p != null) {
				x = p.x;
				y = p.y;
				c = Corner.TOP_LEFT;
			}

			rightArrow.setPosition(new EAdPosition(c, x, y));

			EAdCondition endCondition = new OperationCond(xField,
					-(((column / 2) - 1) * BOOK_WIDTH + BOOK_WIDTH / 2),
					Comparator.LESS);

			ChangeSceneEf changeScene = new ChangeSceneEf();
			changeScene.setId("endBook");
			changeScene.setCondition(endCondition);
			rightArrow.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, changeScene);
			changeScene.getNextEffects().add(showInventory);

			book.getSceneElements().add(leftArrow);
			book.getSceneElements().add(rightArrow);
		} else {
			content.setPosition(Corner.CENTER, 400, 300);
			content.setVarInitialValue(SceneElement.VAR_ENABLE, false);
			ChangeSceneEf changeScene = new ChangeSceneEf();
			changeScene.getNextEffects().add(showInventory);
			book.getBackground().addBehavior(MouseGEv.MOUSE_LEFT_PRESSED,
					changeScene);
		}

		return book;
	}

	private SceneElement getArrow(Book book, EAdSceneElement content,
			String resourceNormal, String resourceOver, Integer expression,
			EAdCondition condition) {
		SceneElement arrow = new SceneElement();
		arrow.setId("arrow");
		this.addAppearance(book, arrow, resourceNormal, resourceOver);

		EAdField<Integer> xVar = new BasicField<Integer>(content,
				SceneElement.VAR_X);

		EAdField<Boolean> visibleVar = new BasicField<Boolean>(arrow,
				SceneElement.VAR_VISIBLE);
		InterpolationEf move = new InterpolationEf(xVar, 0, expression, 500,
				InterpolationLoopType.NO_LOOP, InterpolationType.DESACCELERATE);

		ConditionedEv event = new ConditionedEv();
		event.setCondition(condition);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, new ChangeFieldEf(
				visibleVar, BooleanOp.TRUE_OP));
		event.addEffect(ConditionedEvType.CONDITIONS_UNMET, new ChangeFieldEf(
				visibleVar, BooleanOp.FALSE_OP));
		arrow.getEvents().add(event);

		arrow.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		return arrow;
	}

	private void addTextDrawable(String text, Font font, EAdFont eadFont,
			int xOffset, int lineHeight, int textWidth) {
		List<String> lines = getLines(text, font, textWidth);
		for (String l : lines) {
			Caption caption = new Caption();
			stringHandler.setString(caption.getText(), l);
			caption.setFont(eadFont);
			caption.setPadding(0);
			if (dispY + lineHeight > PAGE_TEXT_HEIGHT) {
				column++;
				dispY = TEXT_Y;
			}

			image.addDrawable(caption, getDispX() + xOffset, dispY);
			dispY += lineHeight;
		}
	}

	private int getDispX() {
		int offset = column / 2 * BOOK_WIDTH;
		if (column % 2 == 0) {
			offset += TEXT_X_1;
		} else
			offset += TEXT_X_2;
		return offset;
	}

	private ArrayList<String> getLines(String completeLine, Font f,
			int maximumWidth) {
		ArrayList<String> lines = new ArrayList<String>();
		String[] words = completeLine.split(" ");
		String line = "";
		int contWord = 0;
		int currentLineWidth = 0;
		while (contWord < words.length) {

			int nextWordWidth = (int) f.getStringBounds(words[contWord] + " ",
					frc).getWidth();

			if (currentLineWidth + nextWordWidth <= maximumWidth) {
				currentLineWidth += nextWordWidth;
				line += words[contWord++] + " ";
			} else if (line != "") {
				lines.add(line);
				currentLineWidth = 0;
				line = "";
			} else {
				line = splitLongWord(f, lines, words[contWord++], maximumWidth);
				currentLineWidth = (int) f.getStringBounds(line, frc)
						.getWidth();
			}
		}

		if (line != "")
			lines.add(line);

		return lines;
	}

	private String splitLongWord(Font f, List<String> lines, String word,
			int lineWidth) {

		boolean finished = false;
		String currentLine = "";

		int i = 0;
		while (!finished) {
			currentLine = "";

			while (i < word.length()
					&& f.getStringBounds(currentLine, frc).getWidth() < lineWidth) {
				currentLine += word.charAt(i++);
			}

			if (i == word.length()) {
				finished = true;
			} else {
				lines.add(currentLine);
			}

		}
		return currentLine;

	}

	private void addAppearance(Book book, SceneElement arrow, String normal,
			String over) {
		AssetDescriptor normalAsset = getArrowAsset(book, normal);

		AssetDescriptor overAsset = getArrowAsset(book, over);
		arrow.getDefinition()
				.getResources()
				.addAsset(arrow.getDefinition().getInitialBundle(),
						SceneElementDef.appearance, normalAsset);

		EAdBundleId bundle = new EAdBundleId("over");
		arrow.getDefinition().getResources().addBundle(bundle);
		arrow.getDefinition().getResources()
				.addAsset(bundle, SceneElementDef.appearance, overAsset);

		ChangeAppearanceEf change1 = new ChangeAppearanceEf(arrow, bundle);
		change1.setId("changeArrowOver");
		arrow.addBehavior(MouseGEv.MOUSE_ENTERED, change1);

		ChangeAppearanceEf change2 = new ChangeAppearanceEf(arrow, arrow
				.getDefinition().getInitialBundle());
		change2.setId("changeArrowOver");
		arrow.addBehavior(MouseGEv.MOUSE_EXITED, change2);

	}

	private static final Image normalLeft = new Image(
			"@drawable/default_left_arrow.png");
	private static final Image overLeft = new Image(
			"@drawable/default_left_over_arrow.png");
	private static final Image normalRight = new Image(
			"@drawable/default_right_arrow.png");
	private static final Image overRight = new Image(
			"@drawable/default_right_over_arrow.png");

	private AssetDescriptor getArrowAsset(Book book, String resource) {
		String path = book.getResources().get(0).getAssetPath(resource);
		if (path != null) {
			return resourceImporter.getAssetDescritptor(path, Image.class);
		} else {
			if (resource.equals(Book.RESOURCE_TYPE_ARROW_LEFT_NORMAL)) {
				return normalLeft;
			} else if (resource.equals(Book.RESOURCE_TYPE_ARROW_LEFT_OVER)) {
				return overLeft;
			} else if (resource.equals(Book.RESOURCE_TYPE_ARROW_RIGHT_NORMAL)) {
				return normalRight;
			} else if (resource.equals(Book.RESOURCE_TYPE_ARROW_RIGHT_OVER)) {
				return overRight;
			}
		}
		return null;

	}

}
