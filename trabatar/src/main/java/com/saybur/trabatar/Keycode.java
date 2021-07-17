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

/**
 * Extended Keyboard II keycodes.
 * 
 * @author saybur
 *
 */
public enum Keycode
{
	KC_POWER(0x7F),
	KC_ESC(0x35),
	KC_F1(0x7A),
	KC_F2(0x78),
	KC_F3(0x63),
	KC_F4(0x76),
	KC_F5(0x60),
	KC_F6(0x61),
	KC_F7(0x62),
	KC_F8(0x64),
	KC_F9(0x65),
	KC_F10(0x6d),
	KC_F11(0x67),
	KC_F12(0x6F),
	KC_F13(0x69),
	KC_F14(0x6B),
	KC_F15(0x71),
	KC_BACK_QUOTE(0x32),
	KC_1(0x12),
	KC_2(0x13),
	KC_3(0x14),
	KC_4(0x15),
	KC_5(0x17),
	KC_6(0x16),
	KC_7(0x1A),
	KC_8(0x1C),
	KC_9(0x19),
	KC_0(0x1D),
	KC_MINUS(0x1B),
	KC_EQUALS(0x18),
	KC_BACKSPACE(0x33),
	KC_TAB(0x30),
	KC_Q(0x0C),
	KC_W(0x0D),
	KC_E(0x0E),
	KC_R(0x0F),
	KC_T(0x11),
	KC_Y(0x10),
	KC_U(0x20),
	KC_I(0x22),
	KC_O(0x1F),
	KC_P(0x23),
	KC_OPEN_BRACKET(0x21),
	KC_CLOSE_BRACKET(0x1E),
	KC_BACKSLASH(0x2A),
	KC_CAPSLOCK(0x39),
	KC_A(0x00),
	KC_S(0x01),
	KC_D(0x02),
	KC_F(0x03),
	KC_G(0x05),
	KC_H(0x04),
	KC_J(0x26),
	KC_K(0x28),
	KC_L(0x25),
	KC_SEMICOLON(0x29),
	KC_QUOTE(0x27),
	KC_RETURN(0x24),
	KC_SHIFT(0x38),
	KC_Z(0x06),
	KC_X(0x07),
	KC_C(0x08),
	KC_V(0x09),
	KC_B(0x0B),
	KC_N(0x2D),
	KC_M(0x2E),
	KC_COMMA(0x2B),
	KC_PERIOD(0x2F),
	KC_SLASH(0x2C),
	KC_CONTROL(0x36),
	KC_OPTION(0x3A),
	KC_COMMAND(0x37),
	KC_SPACE(0x31),
	KC_INSERT(0x72),
	KC_HOME(0x73),
	KC_PAGE_UP(0x74),
	KC_DELETE(0x75),
	KC_END(0x77),
	KC_PAGE_DOWN(0x79),
	KC_UP(0x3E),
	KC_DOWN(0x3D),
	KC_LEFT(0x3B),
	KC_RIGHT(0x3C),
	KC_NUM_LOCK(0x47),
	KC_NP0(0x52),
	KC_NP1(0x53),
	KC_NP2(0x54),
	KC_NP3(0x55),
	KC_NP4(0x56),
	KC_NP5(0x57),
	KC_NP6(0x58),
	KC_NP7(0x59),
	KC_NP8(0x5B),
	KC_NP9(0x5C),
	KC_NP_SEPARATOR(0x41),
	KC_NP_PLUS(0x45),
	KC_NP_SUBTRACT(0x4E),
	KC_NP_MULTIPLY(0x43),
	KC_NP_DIVIDE(0x4B),
	KC_NP_EQUALS(0x51),
	KC_NP_ENTER(0x4C);
	
	private final int code;
	
	private Keycode(int code)
	{
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}
}
