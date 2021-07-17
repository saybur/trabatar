/*
 * Copyright 2016 saybur
 * 
 * This file is part of trabatar.
 * 
 * trabatar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * trabatar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with trabatar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.saybur.trabatar;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Optional;

/**
 * Holds the translation table from virtual keys to the raw keycodes.
 * 
 * @author saybur
 *
 */
public final class Keymap
{
	private final HashMap<Integer, Keycode> km;
	
	Keymap()
	{
		km = new HashMap<Integer, Keycode>();
		km.put(KeyEvent.VK_ESCAPE, Keycode.KC_ESC);
		km.put(KeyEvent.VK_F1, Keycode.KC_F1);
		km.put(KeyEvent.VK_F2, Keycode.KC_F2);
		km.put(KeyEvent.VK_F3, Keycode.KC_F3);
		km.put(KeyEvent.VK_F4, Keycode.KC_F4);
		km.put(KeyEvent.VK_F5, Keycode.KC_F5);
		km.put(KeyEvent.VK_F6, Keycode.KC_F6);
		km.put(KeyEvent.VK_F7, Keycode.KC_F7);
		km.put(KeyEvent.VK_F8, Keycode.KC_F8);
		km.put(KeyEvent.VK_F9, Keycode.KC_F9);
		km.put(KeyEvent.VK_F10, Keycode.KC_F10);
		km.put(KeyEvent.VK_F11, Keycode.KC_F11);
		km.put(KeyEvent.VK_F12, Keycode.KC_F12);
		km.put(KeyEvent.VK_F13, Keycode.KC_F13);
		km.put(KeyEvent.VK_F14, Keycode.KC_F14);
		km.put(KeyEvent.VK_F15, Keycode.KC_F15);
		km.put(KeyEvent.VK_BACK_QUOTE, Keycode.KC_BACK_QUOTE);
		km.put(KeyEvent.VK_1, Keycode.KC_1);
		km.put(KeyEvent.VK_2, Keycode.KC_2);
		km.put(KeyEvent.VK_3, Keycode.KC_3);
		km.put(KeyEvent.VK_4, Keycode.KC_4);
		km.put(KeyEvent.VK_5, Keycode.KC_5);
		km.put(KeyEvent.VK_6, Keycode.KC_6);
		km.put(KeyEvent.VK_7, Keycode.KC_7);
		km.put(KeyEvent.VK_8, Keycode.KC_8);
		km.put(KeyEvent.VK_9, Keycode.KC_9);
		km.put(KeyEvent.VK_0, Keycode.KC_0);
		km.put(KeyEvent.VK_MINUS, Keycode.KC_MINUS);
		km.put(KeyEvent.VK_EQUALS, Keycode.KC_EQUALS);
		km.put(KeyEvent.VK_BACK_SPACE, Keycode.KC_BACKSPACE);
		km.put(KeyEvent.VK_TAB, Keycode.KC_TAB);
		km.put(KeyEvent.VK_Q, Keycode.KC_Q);
		km.put(KeyEvent.VK_W, Keycode.KC_W);
		km.put(KeyEvent.VK_E, Keycode.KC_E);
		km.put(KeyEvent.VK_R, Keycode.KC_R);
		km.put(KeyEvent.VK_T, Keycode.KC_T);
		km.put(KeyEvent.VK_Y, Keycode.KC_Y);
		km.put(KeyEvent.VK_U, Keycode.KC_U);
		km.put(KeyEvent.VK_I, Keycode.KC_I);
		km.put(KeyEvent.VK_O, Keycode.KC_O);
		km.put(KeyEvent.VK_P, Keycode.KC_P);
		km.put(KeyEvent.VK_OPEN_BRACKET, Keycode.KC_OPEN_BRACKET);
		km.put(KeyEvent.VK_CLOSE_BRACKET, Keycode.KC_CLOSE_BRACKET);
		km.put(KeyEvent.VK_BACK_SLASH, Keycode.KC_BACKSLASH);
		km.put(KeyEvent.VK_CAPS_LOCK, Keycode.KC_CAPSLOCK);
		km.put(KeyEvent.VK_A, Keycode.KC_A);
		km.put(KeyEvent.VK_S, Keycode.KC_S);
		km.put(KeyEvent.VK_D, Keycode.KC_D);
		km.put(KeyEvent.VK_F, Keycode.KC_F);
		km.put(KeyEvent.VK_G, Keycode.KC_G);
		km.put(KeyEvent.VK_H, Keycode.KC_H);
		km.put(KeyEvent.VK_J, Keycode.KC_J);
		km.put(KeyEvent.VK_K, Keycode.KC_K);
		km.put(KeyEvent.VK_L, Keycode.KC_L);
		km.put(KeyEvent.VK_SEMICOLON, Keycode.KC_SEMICOLON);
		km.put(KeyEvent.VK_QUOTE, Keycode.KC_QUOTE);
		km.put(KeyEvent.VK_ENTER, Keycode.KC_RETURN);
		km.put(KeyEvent.VK_SHIFT, Keycode.KC_SHIFT);
		km.put(KeyEvent.VK_Z, Keycode.KC_Z);
		km.put(KeyEvent.VK_X, Keycode.KC_X);
		km.put(KeyEvent.VK_C, Keycode.KC_C);
		km.put(KeyEvent.VK_V, Keycode.KC_V);
		km.put(KeyEvent.VK_B, Keycode.KC_B);
		km.put(KeyEvent.VK_N, Keycode.KC_N);
		km.put(KeyEvent.VK_M, Keycode.KC_M);
		km.put(KeyEvent.VK_COMMA, Keycode.KC_COMMA);
		km.put(KeyEvent.VK_PERIOD, Keycode.KC_PERIOD);
		km.put(KeyEvent.VK_SLASH, Keycode.KC_SLASH);
		km.put(KeyEvent.VK_CONTROL, Keycode.KC_CONTROL);
		km.put(KeyEvent.VK_WINDOWS, Keycode.KC_OPTION);
		km.put(KeyEvent.VK_ALT, Keycode.KC_COMMAND);
		km.put(KeyEvent.VK_SPACE, Keycode.KC_SPACE);
		km.put(KeyEvent.VK_INSERT, Keycode.KC_INSERT);
		km.put(KeyEvent.VK_HOME, Keycode.KC_HOME);
		km.put(KeyEvent.VK_PAGE_UP, Keycode.KC_PAGE_UP);
		km.put(KeyEvent.VK_DELETE, Keycode.KC_DELETE);
		km.put(KeyEvent.VK_END, Keycode.KC_END);
		km.put(KeyEvent.VK_PAGE_DOWN, Keycode.KC_PAGE_DOWN);
		km.put(KeyEvent.VK_UP, Keycode.KC_UP);
		km.put(KeyEvent.VK_DOWN, Keycode.KC_DOWN);
		km.put(KeyEvent.VK_LEFT, Keycode.KC_LEFT);
		km.put(KeyEvent.VK_RIGHT, Keycode.KC_RIGHT);
		km.put(KeyEvent.VK_NUM_LOCK, Keycode.KC_NUM_LOCK);
		km.put(KeyEvent.VK_NUMPAD0, Keycode.KC_NP0);
		km.put(KeyEvent.VK_NUMPAD1, Keycode.KC_NP1);
		km.put(KeyEvent.VK_NUMPAD2, Keycode.KC_NP2);
		km.put(KeyEvent.VK_NUMPAD3, Keycode.KC_NP3);
		km.put(KeyEvent.VK_NUMPAD4, Keycode.KC_NP4);
		km.put(KeyEvent.VK_NUMPAD5, Keycode.KC_NP5);
		km.put(KeyEvent.VK_NUMPAD6, Keycode.KC_NP6);
		km.put(KeyEvent.VK_NUMPAD7, Keycode.KC_NP7);
		km.put(KeyEvent.VK_NUMPAD8, Keycode.KC_NP8);
		km.put(KeyEvent.VK_NUMPAD9, Keycode.KC_NP9);
		km.put(KeyEvent.VK_SEPARATOR, Keycode.KC_NP_SEPARATOR);
		km.put(KeyEvent.VK_ADD, Keycode.KC_NP_PLUS);
		//km.put(KeyEvent.VK_MINUS, Keycode.KC_NP_SUBTRACT);
		km.put(KeyEvent.VK_MULTIPLY, Keycode.KC_NP_MULTIPLY);
		//km.put(KeyEvent.VK_SLASH, Keycode.KC_NP_DIVIDE);
		//km.put(KeyEvent.VK_EQUALS, Keycode.KC_NP_EQUALS);
		//km.put(KeyEvent.VK_ENTER, Keycode.KC_NP_ENTER);
		km.put(KeyEvent.VK_DECIMAL, Keycode.KC_NP_PERIOD);
	}
	
	public Optional<Keycode> get(int vk)
	{
		return Optional.ofNullable(km.get(vk));
	}
}
