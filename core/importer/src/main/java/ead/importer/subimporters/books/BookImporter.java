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

package ead.importer.subimporters.books;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.assets.AssetDescriptor;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.drawable.basics.shapes.CircleShape;
import ead.common.model.assets.drawable.compounds.ComposedDrawable;
import ead.common.model.assets.text.BasicFont;
import ead.common.model.assets.text.EAdFont;
import ead.common.model.assets.text.enums.FontStyle;
import ead.common.model.elements.BasicElement;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
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
import ead.common.model.elements.huds.InventoryHud;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.predef.effects.ChangeAppearanceEf;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.util.Position;
import ead.common.model.params.util.Position.Corner;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.ResourceImporter;
import ead.importer.resources.ResourceImporterImpl;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.book.Book;
import es.eucm.eadventure.common.data.chapter.book.BookPage;
import es.eucm.eadventure.common.data.chapter.book.BookParagraph;
import gui.ava.html.image.generator.HtmlImageGenerator;

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

	private static final Logger logger = LoggerFactory
			.getLogger("BookImporter");

	private FontRenderContext frc = new FontRenderContext(null, true, true);
	private Font titleFont = new Font("Arial", Font.PLAIN, 33);
	private EAdFont titleEAdFont = new BasicFont("Arial", 33, FontStyle.PLAIN);

	private Font textFont = new Font("Arial", Font.PLAIN, 18);
	private EAdFont textEAdFont = new BasicFont("Arial", 18, FontStyle.PLAIN);

	private int paragraphDispY = 0;
	private int paragraphColumn;

	private StringHandler stringHandler;
	private ResourceImporter resourceImporter;

	@Inject
	public BookImporter(ResourceImporter resourceImporter,
			StringHandler stringHandler, ImportAnnotator annotator) {
		this.resourceImporter = resourceImporter;
		this.stringHandler = stringHandler;
	}

	@Override
	public EAdScene init(Book oldObject) {
		EAdScene scene = new BasicScene();
		return scene;
	}

	@Override
	public EAdScene convert(Book oldObject, Object newElement) {
		BasicScene book = (BasicScene) newElement;
		ChangeFieldEf hideInventory = new ChangeFieldEf(
				new BasicField<Boolean>(new BasicElement(InventoryHud.ID),
						SceneElement.VAR_VISIBLE),
				EmptyCond.FALSE_EMPTY_CONDITION);
		SceneElementEv hideEvent = new SceneElementEv();
		hideEvent.addEffect(SceneElementEvType.ADDED_TO_SCENE, hideInventory);
		book.getEvents().add(hideEvent);
		// Import background
		AssetDescriptor background = resourceImporter.getAssetDescritptor(
				oldObject.getResources().get(0).getAssetPath(
						Book.RESOURCE_TYPE_BACKGROUND), Image.class);
		book.getBackground().getDefinition().addAsset(

		SceneElementDef.appearance, background);

		ChangeFieldEf showInventory = new ChangeFieldEf(
				new BasicField<Boolean>(new BasicElement(InventoryHud.ID),
						SceneElement.VAR_VISIBLE),
				EmptyCond.TRUE_EMPTY_CONDITION);

		EAdDrawable image = null;
		// Create content
		paragraphColumn = 0;
		if (oldObject.getType() == Book.TYPE_PAGES) {
			image = this.generatePagesBookContent(oldObject);
		} else if (oldObject.getType() == Book.TYPE_PARAGRAPHS) {
			paragraphDispY = TEXT_Y;
			image = generateParagraphBookContent(oldObject);
		}

		SceneElement content = new SceneElement(image);

		EAdField<Float> xField = new BasicField<Float>(content,
				SceneElement.VAR_X);
		// Event to restart the x variable
		SceneElementEv xEvent = new SceneElementEv();
		content.getEvents().add(xEvent);
		ChangeFieldEf changeX = new ChangeFieldEf(xField, new ValueOp(0));
		xEvent.addEffect(SceneElementEvType.ADDED_TO_SCENE, changeX);

		book.setReturnable(false);
		book.getSceneElements().add(content);

		addArrowsParagraphs(book, content, oldObject, xField, showInventory);

		return book;
	}

	private void addArrowsParagraphs(GroupElement book, SceneElement content,
			Book oldObject, EAdField<Float> xField, EAdEffect showInventory) {
		content.setPosition(Corner.TOP_LEFT, 0, 0);

		SceneElementEv event = new SceneElementEv();
		event.addEffect(SceneElementEvType.FIRST_UPDATE, new ChangeFieldEf(
				xField, new ValueOp(0)));
		content.getEvents().add(event);

		EAdCondition leftCondition = new OperationCond(xField, 0,
				Comparator.LESS);
		SceneElement leftArrow = getArrow(oldObject, content,
				Book.RESOURCE_TYPE_ARROW_LEFT_NORMAL,
				Book.RESOURCE_TYPE_ARROW_LEFT_OVER, BOOK_WIDTH, leftCondition);
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

		rightArrow.setPosition(new Position(c, x, y));

		EAdCondition endCondition = new OperationCond(xField,
				-(((paragraphColumn / 2) - 1) * BOOK_WIDTH + BOOK_WIDTH / 2),
				Comparator.LESS);

		ChangeSceneEf changeScene = new ChangeSceneEf();
		changeScene.setCondition(endCondition);
		rightArrow.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, changeScene);
		changeScene.getNextEffects().add(showInventory);

		book.getSceneElements().add(leftArrow);
		book.getSceneElements().add(rightArrow);

	}

	private EAdDrawable generateParagraphBookContent(Book oldObject) {
		ComposedDrawable image = new ComposedDrawable();
		for (BookParagraph p : oldObject.getParagraphs()) {
			if (p.getContent() != null && !p.getContent().equals(""))
				switch (p.getType()) {
				case BookParagraph.TITLE:
					addTextDrawable(image, p.getContent(), titleFont,
							titleEAdFont, 0, TITLE_HEIGHT, TEXT_WIDTH);
					break;
				case BookParagraph.TEXT:
					addTextDrawable(image, p.getContent(), textFont,
							textEAdFont, 0, LINE_HEIGHT, TEXT_WIDTH);
					break;
				case BookParagraph.BULLET:
					if (paragraphDispY + LINE_HEIGHT > PAGE_TEXT_HEIGHT) {
						paragraphColumn++;
						paragraphDispY = TEXT_Y;
					}
					CircleShape bullet = new CircleShape(BULLET_WIDTH / 3);
					bullet.setPaint(ColorFill.BLACK);
					image.addDrawable(bullet, getDispX() + BULLET_WIDTH / 2,
							paragraphDispY + LINE_HEIGHT / 2);
					addTextDrawable(image, p.getContent(), textFont,
							textEAdFont, BULLET_WIDTH, LINE_HEIGHT,
							TEXT_WIDTH_BULLET);
					break;
				case BookParagraph.IMAGE:
					Image i = (Image) resourceImporter.getAssetDescritptor(p
							.getContent(), Image.class);
					try {
						BufferedImage im = ImageIO.read(new File(
								resourceImporter.getNewProjecFolder(), i
										.getUri().toString().substring(1)));
						int height = im.getHeight();
						if (paragraphDispY + height > PAGE_TEXT_HEIGHT) {
							paragraphColumn++;
							paragraphDispY = TEXT_Y;
						}
						image.addDrawable(i, getDispX(), paragraphDispY);
						paragraphDispY += height;
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
		}
		return image;
	}

	private EAdDrawable generatePagesBookContent(Book oldObject) {
		ComposedDrawable image = new ComposedDrawable();
		HtmlImageGenerator imgGenerator = new HtmlImageGenerator();
		int x = 0;

		for (BookPage page : oldObject.getPageURLs()) {
			int leftMargin = page.getMarginStart();
			int topMargin = page.getMarginTop();
			int rightMargin = page.getMarginEnd();
			int bottomMargin = page.getMarginBottom();
			Image i = null;
			switch (page.getType()) {
			case BookPage.TYPE_IMAGE:
				i = (Image) resourceImporter.getAssetDescritptor(page.getUri(),
						Image.class);
				break;
			case BookPage.TYPE_RESOURCE:
				URL url = resourceImporter.getInputStreamCreator().buildURL(
						page.getUri());
				if (url != null) {

					// Generate image
					int width = rightMargin - leftMargin > 0 ? rightMargin
							- leftMargin : 800;
					int height = bottomMargin - topMargin > 0 ? bottomMargin
							- topMargin : 600;
					imgGenerator.setSize(new Dimension(width, height));
					imgGenerator.loadUrl(url);
					BufferedImage img = imgGenerator.getBufferedImage();

					String path = ResourceImporterImpl.DRAWABLE + "/"
							+ page.getUri().replace('/', '_')
							+ Math.round(Math.random() * 100) + ".png";
					File f = new File(resourceImporter.getNewProjecFolder(),
							path);
					try {
						ImageIO.write(img, "PNG", f);
						i = new Image("@" + path);
					} catch (IOException e) {
						logger.error("Error writing image {}", e);
					}
				} else {
					logger.warn("{} page not found.", page.getUri());
				}
				break;
			case BookPage.TYPE_URL:
				logger
						.warn("Remote URLs are no longer supported by eAdventure. {} wasn't imported."
								+ page.getUri());
				break;
			}

			if (i != null) {
				image.addDrawable(i, x + leftMargin, topMargin);
				x += BOOK_WIDTH;
				paragraphColumn += 2;
			}
		}

		return image;
	}

	private SceneElement getArrow(Book book, EAdSceneElement content,
			String resourceNormal, String resourceOver, Integer expression,
			EAdCondition condition) {
		SceneElement arrow = new SceneElement();
		this.addAppearance(book, arrow, resourceNormal, resourceOver);

		EAdField<Float> xVar = new BasicField<Float>(content,
				SceneElement.VAR_X);

		EAdField<Boolean> visibleVar = new BasicField<Boolean>(arrow,
				SceneElement.VAR_VISIBLE);
		InterpolationEf move = new InterpolationEf(xVar, 0, expression, 500,
				InterpolationLoopType.NO_LOOP, InterpolationType.DESACCELERATE);

		ConditionedEv event = new ConditionedEv();
		event.setCondition(condition);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, new ChangeFieldEf(
				visibleVar, EmptyCond.TRUE_EMPTY_CONDITION));
		event.addEffect(ConditionedEvType.CONDITIONS_UNMET, new ChangeFieldEf(
				visibleVar, EmptyCond.FALSE_EMPTY_CONDITION));
		arrow.getEvents().add(event);

		arrow.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, move);
		return arrow;
	}

	private void addTextDrawable(ComposedDrawable image, String text,
			Font font, EAdFont eadFont, int xOffset, int lineHeight,
			int textWidth) {
		List<String> lines = getLines(text, font, textWidth);
		for (String l : lines) {
			Caption caption = new Caption();
			stringHandler.setString(caption.getText(), l);
			caption.setFont(eadFont);
			caption.setPadding(0);
			if (paragraphDispY + lineHeight > PAGE_TEXT_HEIGHT) {
				paragraphColumn++;
				paragraphDispY = TEXT_Y;
			}

			image.addDrawable(caption, getDispX() + xOffset, paragraphDispY);
			paragraphDispY += lineHeight;
		}
	}

	private int getDispX() {
		int offset = paragraphColumn / 2 * BOOK_WIDTH;
		if (paragraphColumn % 2 == 0) {
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
		arrow.getDefinition().addAsset(SceneElementDef.appearance, normalAsset);

		String bundle = "over";
		arrow.getDefinition().addAsset(bundle, SceneElementDef.appearance,
				overAsset);

		ChangeAppearanceEf change1 = new ChangeAppearanceEf(arrow, bundle);
		arrow.addBehavior(MouseGEv.MOUSE_ENTERED, change1);

		ChangeAppearanceEf change2 = new ChangeAppearanceEf(arrow,
				SceneElementDef.INITIAL_BUNDLE);
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
