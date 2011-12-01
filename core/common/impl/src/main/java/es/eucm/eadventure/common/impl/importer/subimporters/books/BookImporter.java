package es.eucm.eadventure.common.impl.importer.subimporters.books;

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

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.book.Book;
import es.eucm.eadventure.common.data.chapter.book.BookParagraph;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.conditions.impl.EmptyCondition;
import es.eucm.eadventure.common.model.conditions.impl.OperationCondition;
import es.eucm.eadventure.common.model.conditions.impl.enums.Comparator;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationLoopType;
import es.eucm.eadventure.common.model.effects.impl.enums.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.enums.ConditionedEventType;
import es.eucm.eadventure.common.model.events.enums.SceneElementEventType;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.MathOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.ValueOperation;
import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.FontStyle;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl.Corner;
import es.eucm.eadventure.common.predef.model.effects.EAdChangeAppearance;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.CircleShape;
import es.eucm.eadventure.common.resources.assets.drawable.compounds.impl.ComposedDrawableImpl;

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

	private FontRenderContext frc = new FontRenderContext(null, true, true);
	private Font titleFont = new Font("Arial", Font.PLAIN, 33);
	private EAdFont titleEAdFont = new EAdFontImpl("Arial", 33, FontStyle.PLAIN);

	private Font textFont = new Font("Arial", Font.PLAIN, 18);
	private EAdFont textEAdFont = new EAdFontImpl("Arial", 18, FontStyle.PLAIN);

	private int dispY = 0;
	private ComposedDrawableImpl image;
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
		EAdScene scene = new EAdSceneImpl();
		scene.setId(oldObject.getId());
		return scene;
	}

	@Override
	public EAdScene convert(Book oldObject, Object newElement) {
		EAdSceneImpl book = (EAdSceneImpl) newElement;
		// Import background
		AssetDescriptor background = resourceImporter.getAssetDescritptor(
				oldObject.getResources().get(0)
						.getAssetPath(Book.RESOURCE_TYPE_BACKGROUND),
				ImageImpl.class);
		book.getBackground()
				.getResources()
				.addAsset(book.getBackground().getInitialBundle(),
						EAdBasicSceneElement.appearance, background);

		dispY = TEXT_Y;
		column = 0;
		image = new ComposedDrawableImpl();

		for (BookParagraph p : oldObject.getParagraphs()) {
			if (p.getContent() != null && !p.getContent().equals(""))
				switch (p.getType()) {
				case BookParagraph.TITLE:
					addTextDrawable(p.getContent(), titleFont, titleEAdFont, 0,
							TITLE_HEIGHT, TEXT_WIDTH);
					break;
				case BookParagraph.TEXT:
					addTextDrawable(p.getContent(), textFont, textEAdFont, 0,
							LINE_HEIGHT, TEXT_WIDTH);
					break;
				case BookParagraph.BULLET:
					if (dispY + LINE_HEIGHT > PAGE_TEXT_HEIGHT) {
						column++;
						dispY = TEXT_Y;
					}
					CircleShape bullet = new CircleShape(0, 0,
							BULLET_WIDTH / 3, 20);
					bullet.setPaint(EAdColor.BLACK);
					image.addDrawable(bullet, getDispX() + BULLET_WIDTH / 2,
							dispY + LINE_HEIGHT / 2);
					addTextDrawable(p.getContent(), textFont, textEAdFont,
							BULLET_WIDTH, LINE_HEIGHT, TEXT_WIDTH_BULLET);
					break;
				case BookParagraph.IMAGE:
					Image i = (Image) resourceImporter.getAssetDescritptor(
							p.getContent(), ImageImpl.class);
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

		EAdBasicSceneElement content = new EAdBasicSceneElement();
		content.setId(oldObject.getId() + "_content");
		content.getResources().addAsset(content.getInitialBundle(),
				EAdBasicSceneElement.appearance, image);
		content.setPosition(0, 0);
		content.setClone(true);

		EAdField<Integer> xVar = new EAdFieldImpl<Integer>(content,
				EAdBasicSceneElement.VAR_X);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl();
		event.setId("restartBook");
		event.addEffect(SceneElementEventType.ADDED_TO_SCENE,
				new EAdChangeFieldValueEffect( xVar,
						new ValueOperation( 0)));
		content.getEvents().add(event);

		EAdCondition leftCondition = new OperationCondition(xVar, 0,
				Comparator.LESS);
		EAdBasicSceneElement leftArrow = getArrow(oldObject, content,
				Book.RESOURCE_TYPE_ARROW_LEFT_NORMAL,
				Book.RESOURCE_TYPE_ARROW_LEFT_OVER, "[0] + " + BOOK_WIDTH,
				leftCondition);
		Point p = oldObject.getPreviousPagePoint();
		int x = 10;
		int y = 10;
		if (p != null) {
			x = p.x;
			y = p.y;
		}
		leftArrow.setPosition(x, y);

		EAdCondition rightCondition = EmptyCondition.TRUE_EMPTY_CONDITION;
		EAdBasicSceneElement rightArrow = getArrow(oldObject, content,
				Book.RESOURCE_TYPE_ARROW_RIGHT_NORMAL,
				Book.RESOURCE_TYPE_ARROW_RIGHT_OVER, "[0] - " + BOOK_WIDTH,
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

		rightArrow.setPosition(new EAdPositionImpl(c, x, y));

		EAdCondition endCondition = new OperationCondition(xVar,
				-(((column / 2) - 1) * BOOK_WIDTH + BOOK_WIDTH / 2),
				Comparator.LESS);

		EAdChangeScene changeScene = new EAdChangeScene();
		changeScene.setId("endBook");
		changeScene.setCondition(endCondition);
		rightArrow.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, changeScene);

		book.getComponents().add(content);
		book.getComponents().add(leftArrow);
		book.getComponents().add(rightArrow);
		book.setReturnable(false);

		return book;
	}

	private EAdBasicSceneElement getArrow(Book book, EAdSceneElement content,
			String resourceNormal, String resourceOver, String expression,
			EAdCondition condition) {
		EAdBasicSceneElement arrow = new EAdBasicSceneElement();
		arrow.setId("arrow");
		this.addAppearance(book, arrow, resourceNormal, resourceOver);

		EAdField<Integer> xVar = new EAdFieldImpl<Integer>(content,
				EAdBasicSceneElement.VAR_X);

		EAdField<Boolean> visibleVar = new EAdFieldImpl<Boolean>(arrow,
				EAdBasicSceneElement.VAR_VISIBLE);
		EAdInterpolationEffect move = new EAdInterpolationEffect(xVar,
				new MathOperation("[0]", xVar), new MathOperation(expression,
						xVar), 500, InterpolationLoopType.NO_LOOP,
				InterpolationType.BOUNCE_END);

		EAdConditionEvent event = new EAdConditionEventImpl();
		event.setCondition(condition);
		event.addEffect(ConditionedEventType.CONDITIONS_MET,
				new EAdChangeFieldValueEffect( visibleVar,
						BooleanOperation.TRUE_OP));
		event.addEffect(ConditionedEventType.CONDITIONS_UNMET,
				new EAdChangeFieldValueEffect( visibleVar,
						BooleanOperation.FALSE_OP));
		arrow.getEvents().add(event);

		arrow.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, move);
		return arrow;
	}

	private void addTextDrawable(String text, Font font, EAdFont eadFont,
			int xOffset, int lineHeight, int textWidth) {
		List<String> lines = getLines(text, font, textWidth);
		for (String l : lines) {
			CaptionImpl caption = new CaptionImpl();
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

	private void addAppearance(Book book, EAdBasicSceneElement arrow,
			String normal, String over) {
		AssetDescriptor normalAsset = getArrowAsset(book, normal);

		AssetDescriptor overAsset = getArrowAsset(book, over);
		arrow.getResources().addAsset(arrow.getInitialBundle(),
				EAdBasicSceneElement.appearance, normalAsset);

		EAdBundleId bundle = new EAdBundleId("over");
		arrow.getResources().addBundle(bundle);
		arrow.getResources().addAsset(bundle, EAdBasicSceneElement.appearance,
				overAsset);

		EAdChangeAppearance change1 = new EAdChangeAppearance(
				arrow, bundle);
		change1.setId("changeArrowOver");
		arrow.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, change1);

		EAdChangeAppearance change2 = new EAdChangeAppearance(
				 arrow, arrow.getInitialBundle());
		change2.setId("changeArrowOver");
		arrow.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, change2);

	}

	private static final Image normalLeft = new ImageImpl(
			"@drawable/default_left_arrow.png");
	private static final Image overLeft = new ImageImpl(
			"@drawable/default_left_over_arrow.png");
	private static final Image normalRight = new ImageImpl(
			"@drawable/default_right_arrow.png");
	private static final Image overRight = new ImageImpl(
			"@drawable/default_right_over_arrow.png");

	private AssetDescriptor getArrowAsset(Book book, String resource) {
		String path = book.getResources().get(0).getAssetPath(resource);
		if (path != null) {
			return resourceImporter.getAssetDescritptor(path, ImageImpl.class);
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
