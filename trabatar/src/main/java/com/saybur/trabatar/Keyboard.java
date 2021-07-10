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
import java.awt.event.KeyListener;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that manages the keyboard. Also see {@link Keycode} and {@link Keymap}
 * for the classes that handle mapping virtual keys to raw ADB keycodes.
 * 
 * @author saybur
 *
 */
public final class Keyboard implements KeyListener
{
	private static final int RELEASE_KEY = KeyEvent.VK_CONTROL;
	private static final int RELEASE_SIDE = KeyEvent.KEY_LOCATION_RIGHT;
	
	private final Logger log = LoggerFactory.getLogger(Keyboard.class);
	private final Trabatar parent;
	private final Keymap keymap;

	Keyboard(Trabatar parent)
	{
		this.parent = Objects.requireNonNull(parent);
		keymap = new Keymap();
	}
	
	@Override
	public void keyPressed(KeyEvent evt)
	{
		if(! parent.isActive())
		{
			return;
		}

		if(evt.getKeyCode() == RELEASE_KEY
				&& evt.getKeyLocation() == RELEASE_SIDE)
		{
			// only handle on release
			return;
		}

		Optional<Keycode> key = keymap.get(evt.getKeyCode());
		if(log.isTraceEnabled())
		{
			log.trace("Down: {} ({})",
				evt.getKeyCode(),
				key.orElse(null));
		}
		
		if(key.isPresent())
		{
			int kc = key.get().getCode();
			parent.sendData(0x40 + (kc & 0x0F));
			parent.sendData(0x50 + ((kc & 0xF0) >> 4));
		}
	}

	@Override
	public void keyReleased(KeyEvent evt)
	{
		if(! parent.isActive())
		{
			return;
		}

		if(evt.getKeyCode() == RELEASE_KEY
				&& evt.getKeyLocation() == RELEASE_SIDE)
		{
			parent.commandRelease();
			return;
		}

		Optional<Keycode> key = keymap.get(evt.getKeyCode());
		if(log.isTraceEnabled())
		{
			log.trace("Up: {} ({})",
					evt.getKeyCode(),
					key.orElse(null));
		}
		
		if(key.isPresent())
		{
			int kc = key.get().getCode();
			parent.sendData(0x40 + (kc & 0x0F));
			parent.sendData(0x58 + ((kc & 0xF0) >> 4));
		}
	}

	@Override
	public void keyTyped(KeyEvent evt)
	{
		// ignore this event
	}
}
