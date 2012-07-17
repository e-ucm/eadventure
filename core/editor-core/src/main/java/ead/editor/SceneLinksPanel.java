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

package ead.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.resources.assets.drawable.basics.Image;
import java.awt.dnd.MouseDragGestureRecognizer;
import javax.swing.JPanel;

/**
 * Class that implements the panel that shows a preview of the scenes with their
 * links to each other
 */
public class SceneLinksPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 613000378798269881L;

	private static final Logger logger = LoggerFactory
			.getLogger("SceneLinksPanelTest");
	/**
	 * The thumbnail image that represents the appearance of each scene
	 */
	private BufferedImage image;
	/**
	 * The scale used to draw the appearance of the scenes on the screen
	 */
	private double drawingScale;
	/**
	 * The list of scenes to show on the panel
	 */
	private EAdList<EAdScene> scenes;
	/**
	 * The list of elements used to show the scenes on the panel
	 */
	private List<SceneElement> elements;
	/**
	 * The listener used for dragging the contents of the drag panel
	 */
	private MouseDragGestureRecognizer listener;

	/**
	 * Constructor for the SceneLinksPanel class. Creates a new panel with a
	 * list of scenes.
	 * 
	 * @param scns
	 *            the list of scenes to show on the panel
	 * 
	 */
	public SceneLinksPanel(EAdList<EAdScene> scns) {

		this.scenes = scns;
		drawingScale = (double) (0.35f * (1 - Math.log(this.scenes.size())
				/ Math.log(100)));
		listener = null;
		paintScenes();

	}

	/**
	 * Method for painting all the thumbnails of the scenes on the panel
	 */
	public void paintScenes() {

		this.elements = new ArrayList<SceneElement>();

		for (EAdScene scene : scenes) {

			try {
				EAdSceneElement element = scene.getBackground();
				// TODO Loading images to be fixed with some specific image
				// object
				Image imageImpl = (Image) element
						.getDefinition()
						.getResources()
						.getAsset(element.getDefinition().getInitialBundle(),
								SceneElementDef.appearance);
				image = ImageIO.read(getResourceAsStream(imageImpl.getUri()
						.getPath()));

				// To test functionality, loads an image on demand
				// image =
				// ImageIO.read(getResourceAsStream("@drawable/loading.png"));

			} catch (IOException e) {
				logger.error(
						"Could not load image! "
								+ ((Image) scene
										.getDefinition()
										.getResources()
										.getAsset(
												SceneElementDef.appearance))
										.getUri(), e);
			}

			image = getScaledInstance(image,
					(int) (image.getWidth() * drawingScale),
					(int) (image.getHeight() * drawingScale),
					RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

			SceneElement sc = new SceneElement() {

				private static final long serialVersionUID = 1L;

				public void paintComponent(Graphics g) {

					g.drawImage(image, 0, 0, null);

				}

			};

			sc.setSize(image.getWidth(), image.getHeight());
			sc.setPosX((new Random()).nextInt(600));
			sc.setPosY((new Random()).nextInt(400));
			sc.setBounds((int) sc.getPosX(), (int) sc.getPosY(), sc.getWidth(),
					sc.getHeight());
//			sc.addKeyListener(listener);
//			sc.addMouseListener(listener);
//			sc.addMouseMotionListener(listener);
//
//			dragPanel.add(sc);
//			elements.add(sc);
		}

		image.flush();
	}

	/**
	 * Method for clearing and repainting all the thumbnails of the scenes on
	 * the panel
	 */
	public void repaintScenes() {

//		dragPanel.removeAll();
		this.paintScenes();
	}

	/**
	 * Setter for the list of scenes attribute
	 */
	public void setScenes(EAdList<EAdScene> scns) {

		this.scenes = scns;
	}

	/**
	 * +++Temporal method for loading resources, future AssetHandler instance
	 * expected+++
	 */
	// TODO Expected AssetHandler instance or similar to load assets
	private InputStream getResourceAsStream(String path) {

		String location = path.replaceAll("@", "");
		return ClassLoader.getSystemResourceAsStream(location);

	}

	/**
	 * Convenience method that returns a scaled instance of the provided one
	 */
	private BufferedImage getScaledInstance(BufferedImage img, int targetWidth,
			int targetHeight, Object hint, boolean higherQuality) {

		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			w = img.getWidth();
			h = img.getHeight();
		} else {
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

	/**
	 * Adds a line form a given point to the given sceneElement
	 * 
	 * @param x1
	 *            The x value of the initial point
	 * @param y1
	 *            The y value of the initial point
	 * @param temp
	 *            The destination sceneElement
	 * @param color
	 *            The color of the line
	 * @param lines
	 *            The list of lines where to add the new one
	 */
	// TODO To be used when the scenes are linked one to another
	protected void addLine(int x1, int y1, SceneElement temp, Color color,
			Stroke stroke, List<Line> lines) {

		double w = temp.getWidth() * drawingScale / 2;
		double h = temp.getHeight() * drawingScale / 2;
		int x2 = (int) (temp.getPosX() + w);
		int y2 = (int) (temp.getPosY() + h);

		int x3 = x2;
		int y3 = y2;

		if (h == 0
				|| y2 == y1
				|| ((y2 - y1) != 0 && Math.abs(w / h) <= Math.abs((x2 - x1)
						/ (y2 - y1)))) {
			if (x1 <= x2) {
				x3 = (int) (x2 - w);
				y3 = (int) (y2 - (w / (x2 - x1)) * (y2 - y1));
			} else if (x1 > x2) {
				x3 = (int) (x2 + w);
				y3 = (int) (y2 + (w / (x2 - x1)) * (y2 - y1));
			}
		} else {
			if (y1 <= y2) {
				y3 = (int) (y2 - h);
				x3 = (int) (x2 - (h / (y2 - y1)) * (x2 - x1));
			} else if (y1 > y2) {
				y3 = (int) (y2 + h);
				x3 = (int) (x2 + (h / (y2 - y1)) * (x2 - x1));
			}
		}

		lines.add(new Line(x1, y1, x3, y3, color, stroke));
	}

	/**
	 * Class with all the elements of a line
	 */
	// TODO To be used when the scenes are linked one to another
	public class Line {

		public int x1;

		public int x2;

		public int y1;

		public int y2;

		public Color color;

		public Stroke stroke;

		public Line(int x1, int y1, int x2, int y2, Color color, Stroke stroke) {

			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			this.color = color;
			this.stroke = stroke;
		}
	}

}
