package es.eucm.eadventure.common.impl.importer.subimporters.books;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
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
import es.eucm.eadventure.common.model.conditions.impl.VarCondition.Operator;
import es.eucm.eadventure.common.model.conditions.impl.VarValCondition;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.InterpolationType;
import es.eucm.eadventure.common.model.effects.impl.EAdVarInterpolationEffect.LoopType;
import es.eucm.eadventure.common.model.effects.impl.variables.EAdChangeFieldValueEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.EAdConditionEvent.ConditionedEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.variables.EAdField;
import es.eucm.eadventure.common.model.variables.impl.EAdFieldImpl;
import es.eucm.eadventure.common.model.variables.impl.operations.AssignOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.BooleanOperation;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;
import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.EAdFont.Style;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
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
	private EAdFont titleEAdFont = new EAdFontImpl("Arial", 33, Style.PLAIN);

	private Font textFont = new Font("Arial", Font.PLAIN, 18);
	private EAdFont textEAdFont = new EAdFontImpl("Arial", 18, Style.PLAIN);

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
		return new EAdSceneImpl(oldObject.getId());
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
					CircleShape bullet = new CircleShape(0, 0, BULLET_WIDTH / 3, 20);
					bullet.setFill(EAdColor.BLACK);
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
										.getURI().toString().substring(1)));
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

		EAdBasicSceneElement content = new EAdBasicSceneElement(
				oldObject.getId() + "_content");
		content.getResources().addAsset(content.getInitialBundle(),
				EAdBasicSceneElement.appearance, image);
		content.setPosition(0, 0);
		content.setClone(true);

		EAdField<Integer> xVar = new EAdFieldImpl<Integer>(content,
				EAdBasicSceneElement.VAR_X);

		EAdSceneElementEvent event = new EAdSceneElementEventImpl("restartBook");
		event.addEffect(SceneElementEvent.ADDED_TO_SCENE,
				new EAdChangeFieldValueEffect("restarBook", xVar,
						new AssignOperation("assign", 0)));
		content.getEvents().add(event);

		EAdCondition leftCondition = new VarValCondition("leftCondition", xVar,
				0, Operator.LESS);
		EAdBasicSceneElement leftArrow = getArrow(oldObject, content,
				Book.RESOURCE_TYPE_ARROW_LEFT_NORMAL,
				Book.RESOURCE_TYPE_ARROW_LEFT_OVER, "[0] + " + BOOK_WIDTH,
				leftCondition);
		leftArrow.setPosition(oldObject.getPreviousPagePoint().x,
				oldObject.getPreviousPagePoint().y);

		EAdCondition rightCondition = EmptyCondition.TRUE_EMPTY_CONDITION;
		EAdBasicSceneElement rightArrow = getArrow(oldObject, content,
				Book.RESOURCE_TYPE_ARROW_RIGHT_NORMAL,
				Book.RESOURCE_TYPE_ARROW_RIGHT_OVER, "[0] - " + BOOK_WIDTH,
				rightCondition);
		rightArrow.setPosition(oldObject.getNextPagePoint().x,
				oldObject.getNextPagePoint().y);

		EAdCondition endCondition = new VarValCondition(xVar,
				-(((column / 2) - 1) * BOOK_WIDTH + BOOK_WIDTH / 2),
				Operator.LESS);

		EAdChangeScene changeScene = new EAdChangeScene("endBook");
		changeScene.setCondition(endCondition);
		rightArrow.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, changeScene);

		book.getElements().add(content);
		book.getElements().add(leftArrow);
		book.getElements().add(rightArrow);
		book.setReturnable(false);

		return book;
	}

	private EAdBasicSceneElement getArrow(Book book, EAdSceneElement content,
			String resourceNormal, String resourceOver, String expression,
			EAdCondition condition) {
		EAdBasicSceneElement arrow = new EAdBasicSceneElement("arrow");
		this.addAppearance(book, arrow, resourceNormal, resourceOver);

		EAdField<Integer> xVar = new EAdFieldImpl<Integer>(content,
				EAdBasicSceneElement.VAR_X);

		EAdField<Boolean> visibleVar = new EAdFieldImpl<Boolean>(arrow,
				EAdBasicSceneElement.VAR_VISIBLE);
		EAdVarInterpolationEffect move = new EAdVarInterpolationEffect(
				"changePage");
		move.setInterpolation(xVar,
				new LiteralExpressionOperation("[0]", xVar),
				new LiteralExpressionOperation(expression, xVar), 500,
				LoopType.NO_LOOP, InterpolationType.BOUNCE_END);

		EAdConditionEvent event = new EAdConditionEventImpl("event");
		event.setCondition(condition);
		event.addEffect(ConditionedEvent.CONDITIONS_MET,
				new EAdChangeFieldValueEffect("visible", visibleVar,
						BooleanOperation.TRUE_OP));
		event.addEffect(ConditionedEvent.CONDITIONS_UNMET,
				new EAdChangeFieldValueEffect("notVisible", visibleVar,
						BooleanOperation.FALSE_OP));
		arrow.getEvents().add(event);

		arrow.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, move);
		return arrow;
	}

	private void addTextDrawable(String text, Font font, EAdFont eadFont,
			int xOffset, int lineHeight, int textWidth) {
		List<String> lines = getLines(text, font, textWidth);
		for (String l : lines) {
			CaptionImpl caption = new CaptionImpl(stringHandler.addString(l));
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

		EAdChangeAppearance change1 = new EAdChangeAppearance("changeArrowOver");
		change1.setBundleId(bundle);
		change1.setElement(arrow);
		arrow.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, change1);

		EAdChangeAppearance change2 = new EAdChangeAppearance("changeArrowOver");
		change2.setBundleId(arrow.getInitialBundle());
		change2.setElement(arrow);
		arrow.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, change2);

	}

	private static final String normalLeft = "assets/special/DefaultLeftNormalArrow.png";
	private static final String overLeft = "assets/special/DefaultLeftOverArrow.png";

	private String normalRight, overRight;

	private AssetDescriptor getArrowAsset(Book book, String resource) {
		String path = book.getResources().get(0).getAssetPath(resource);
		if (path != null) {
			return resourceImporter.getAssetDescritptor(path, ImageImpl.class);
		} else {
			if (resource.equals(Book.RESOURCE_TYPE_ARROW_LEFT_NORMAL)) {
				return resourceImporter.getAssetDescritptor(normalLeft,
						ImageImpl.class);
			} else if (resource.equals(Book.RESOURCE_TYPE_ARROW_LEFT_OVER)) {
				return resourceImporter.getAssetDescritptor(overLeft,
						ImageImpl.class);
			} else if (resource.equals(Book.RESOURCE_TYPE_ARROW_RIGHT_NORMAL)) {
				if (normalRight == null) {
					loadNormalRight();
				}
				return new ImageImpl(normalRight);
			} else if (resource.equals(Book.RESOURCE_TYPE_ARROW_RIGHT_OVER)) {
				if (overRight == null) {
					loadOverRight();
				}
				return new ImageImpl(overRight);
			}
		}
		return null;

	}

	private void loadOverRight() {
		ImageImpl i = (ImageImpl) resourceImporter.getAssetDescritptor(
				overLeft, ImageImpl.class);
		try {
			BufferedImage im = ImageIO.read(new File(resourceImporter
					.getNewProjecFolder(), i.getURI().toString().substring(1)));
			overRight = "drawable/assets_special_DefaultOverRightArrow.png";
			BufferedImage out = new BufferedImage(im.getWidth(),
					im.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = (Graphics2D) out.getGraphics();

			g.drawImage(im, AffineTransform.getRotateInstance(Math.PI,
					im.getWidth() / 2, im.getHeight() / 2), null);
			ImageIO.write(out, "png",
					new File(resourceImporter.getNewProjecFolder(), overRight));
			overRight = "@" + overRight;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loadNormalRight() {
		ImageImpl i = (ImageImpl) resourceImporter.getAssetDescritptor(
				normalLeft, ImageImpl.class);
		try {
			BufferedImage im = ImageIO.read(new File(resourceImporter
					.getNewProjecFolder(), i.getURI().toString().substring(1)));
			normalRight = "drawable/assets_special_DefaultNormalRightArrow.png";
			BufferedImage out = new BufferedImage(im.getWidth(),
					im.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = (Graphics2D) out.getGraphics();
			g.drawImage(im, AffineTransform.getRotateInstance(Math.PI,
					im.getWidth() / 2, im.getHeight() / 2), null);
			ImageIO.write(
					out,
					"png",
					new File(resourceImporter.getNewProjecFolder(), normalRight));
			normalRight = "@" + normalRight;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
