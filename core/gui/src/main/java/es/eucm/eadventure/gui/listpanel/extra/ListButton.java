package es.eucm.eadventure.gui.listpanel.extra;

import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import es.eucm.eadventure.gui.CommonGUIMessages;
import es.eucm.eadventure.gui.R;

public class ListButton extends JButton {

	private static final long serialVersionUID = 1L;

	public static enum Type {ADD, DELETE, MOVE_UP, MOVE_DOWN, DUPLICATE};
	
	public ListButton(Type type) {
		super();
		
		try {
			InputStream is = ClassLoader
					.getSystemResourceAsStream(getImage(type));
			setIcon(new ImageIcon(ImageIO.read(is)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setContentAreaFilled(false);
		setMargin(new Insets(0, 0, 0, 0));
		setToolTipText(getToolTip(type));
	
	}
	
	public String getImage(Type type) {
		switch(type) {
		case ADD:
			return R.Drawable.add_png;
		case DELETE:
			return R.Drawable.delete_png;
		case MOVE_UP:
			return R.Drawable.move_up_png;
		case MOVE_DOWN:
			return R.Drawable.move_down_png;
		case DUPLICATE:
			return R.Drawable.duplicate_png;
		}
		return null;
	}
	

	public String getToolTip(Type type) {
		switch(type) {
		case ADD:
			return CommonGUIMessages.add_new;
		case DELETE:
			return CommonGUIMessages.delete;
		case MOVE_UP:
			return CommonGUIMessages.move_up;
		case MOVE_DOWN:
			return CommonGUIMessages.move_down;
		case DUPLICATE:
			return CommonGUIMessages.duplicate;
		}
		return null;
	}

	
}
