package ead.guitools.enginegui.effects.usetraces;

import ead.common.model.elements.effects.AbstractEffect;
import ead.common.model.elements.extra.EAdList;
import ead.common.params.guievents.EAdGUIEvent;
import ead.common.params.guievents.KeyGEv;
import ead.common.params.guievents.MouseGEv;
import ead.common.params.guievents.enums.KeyEventType;
import ead.common.params.guievents.enums.KeyGEvCode;
import ead.common.params.guievents.enums.MouseGEvButtonType;
import ead.common.params.guievents.enums.MouseGEvType;
import ead.common.util.EAdPosition;
import ead.tools.xml.XMLAttributes;
import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLNode;

public class UseTracesEffect extends AbstractEffect {

	private EAdList<EAdGUIEvent> inputEvents;

	private EAdList<EAdPosition> positions;

	private EAdList<Integer> timestamps;

	public UseTracesEffect() {
		inputEvents = new EAdList<EAdGUIEvent>();
		positions = new EAdList<EAdPosition>();
		timestamps = new EAdList<Integer>();
		setPersistent(true);
	}

	public EAdList<Integer> getTimestamps() {
		return timestamps;
	}

	public void setTimestamps(EAdList<Integer> timestamps) {
		this.timestamps = timestamps;
	}

	public EAdList<EAdGUIEvent> getInputEvents() {
		return inputEvents;
	}

	public void setInputEvents(EAdList<EAdGUIEvent> inputEvents) {
		this.inputEvents = inputEvents;
	}

	public EAdList<EAdPosition> getPositions() {
		return positions;
	}

	public void setPositions(EAdList<EAdPosition> positions) {
		this.positions = positions;
	}

	public void loadFromXML(XMLDocument document) {
		int lastTime = 0;
		inputEvents.clear();
		timestamps.clear();
		positions.clear();
		for (int i = 0; i < document.getFirstChild().getChildNodes()
				.getLength(); i++) {
			XMLNode node = document.getFirstChild().getChildNodes().item(i);
			if (node.getNodeName().equals("l")) {
				EAdGUIEvent event = null;
				EAdPosition p = null;

				XMLAttributes att = node.getAttributes();
				int timestamp = Integer.parseInt(node.getAttributes().getValue(
						"ms"));
				String device = att.getValue("t");
				String action = att.getValue("i");
				if (action.equals("en") || action.equals("ex")) {
					continue;
				}
				if (device.equals("m")) {
					int b = Integer.parseInt(att.getValue("b"));
					int x = Integer.parseInt(att.getValue("x"));
					int y = Integer.parseInt(att.getValue("y"));
					p = new EAdPosition(x, y);
					event = getMouseEvent(action, b);
				} else if (device.equals("k")) {
					int c = Integer.parseInt(att.getValue("c"));
					event = getKeyboardEvent(action, c);
					p = new EAdPosition(c, 0);
				}

				if (event != null && p != null) {
					inputEvents.add(event);
					positions.add(p);
					timestamps.add(timestamp - lastTime);
					lastTime = timestamp;
				}
			}
		}
	}

	private EAdGUIEvent getKeyboardEvent(String action, int c) {
		KeyEventType type = KeyEventType.KEY_PRESSED;
		if (action.equals("r")) {
			type = KeyEventType.KEY_RELEASED;
		} else if (action.equals("t")) {
			type = KeyEventType.KEY_TYPED;
		}

		KeyGEvCode code = KeyGEvCode.getCodeForChar((char) c);
		KeyGEv event = new KeyGEv(type, code);
		return event;
	}

	private EAdGUIEvent getMouseEvent(String action, int button) {
		MouseGEvType type = MouseGEvType.PRESSED;
		if (action.equals("r")) {
			type = MouseGEvType.RELEASED;
		} else if (action.equals("c")) {
			type = MouseGEvType.CLICK;
		} else if (action.equals("m")) {
			type = MouseGEvType.MOVED;
		}

		MouseGEvButtonType b = MouseGEvButtonType.NO_BUTTON;
		switch (button) {
		case 1:
			b = MouseGEvButtonType.BUTTON_1;
			break;
		case 2:
			b = MouseGEvButtonType.BUTTON_2;
			break;
		case 3:
			b = MouseGEvButtonType.BUTTON_3;
			break;
		default:

			b = MouseGEvButtonType.NO_BUTTON;
		}

		return MouseGEv.getMouseEvent(type, b);
	}

}
