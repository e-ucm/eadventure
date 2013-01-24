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

package ead.common.params.guievents.enums;

import com.gwtent.reflection.client.Reflectable;

/**
 * 
 * Key code
 * 
 */
@Reflectable
public enum KeyGEvCode {
	CHARACTER, ANY_KEY, NUM_0, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, NUM_7, NUM_8, NUM_9, A, ALT_LEFT, ALT_RIGHT, APOSTROPHE, AT, B, BACK, BACKSLASH, C, CALL, CAMERA, CLEAR, COMMA, D, BACKSPACE, FORWARD_DEL, CENTER, DOWN, LEFT, RIGHT, UP, E, ENDCALL, ENTER, ENVELOPE, EQUALS, EXPLORER, F, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, FOCUS, G, GRAVE, H, HEADSETHOOK, HOME, I, J, K, L, LEFT_BRACKET, M, MEDIA_FAST_FORWARD, MEDIA_NEXT, MEDIA_PLAY_PAUSE, MEDIA_PREVIOUS, MEDIA_REWIND, MEDIA_STOP, MENU, MINUS, MUTE, N, NOTIFICATION, NUM, O, P, PERIOD, PLUS, POUND, POWER, Q, R, RIGHT_BRACKET, S, SEARCH, SEMICOLON, SHIFT_LEFT, SHIFT_RIGHT, SLASH, SOFT_LEFT, SOFT_RIGHT, SPACE, STAR, SYM, T, TAB, U, UNKNOWN, V, VOLUME_DOWN, VOLUME_UP, W, X, Y, Z, CONTROL_LEFT, CONTROL_RIGHT, ESCAPE, END, INSERT, PAGE_UP, PAGE_DOWN, COLON;

	public static KeyGEvCode getCodeForChar(char c) {
		switch (c) {
		case 'a':
		case 'A':
			return A;
		case 'b':
		case 'B':
			return B;
		case 'c':
		case 'C':
			return C;
		case 'd':
		case 'D':
			return D;
		case 'e':
		case 'E':
			return E;
		case 'f':
		case 'F':
			return F;
		case 'g':
		case 'G':
			return G;
		case 'H':
		case 'h':
			return H;
		case 'i':
		case 'I':
			return I;
		case 'J':
		case 'j':
			return J;
		case 'K':
		case 'k':
			return K;
		case 'l':
		case 'L':
			return L;
		case 'M':
		case 'm':
			return M;
		case 'n':
		case 'N':
			return N;
		case 'o':
		case 'O':
			return O;
		case 'p':
		case 'P':
			return P;
		case 'q':
		case 'Q':
			return Q;
		case 'r':
		case 'R':
			return R;
		case 's':
		case 'S':
			return S;
		case 't':
		case 'T':
			return T;
		case 'u':
		case 'U':
			return U;
		case 'v':
		case 'V':
			return V;
		case 'w':
		case 'W':
			return W;
		case 'x':
		case 'X':
			return X;
		case 'y':
		case 'Y':
			return Y;
		case 'z':
		case 'Z':
			return Z;
		default:
			return ANY_KEY;
		}
	}
}
