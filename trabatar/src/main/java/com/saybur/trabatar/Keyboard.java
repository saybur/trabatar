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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that manages the keyboard. Also see {@link Keycode} and {@link Keymap}
 * for the classes that handle mapping virtual keys to raw ADB keycodes.
 * 
 * @author saybur
 *
 */
public final class Keyboard implements KeyListener
{
	private static final int RELEASE_KEY = KeyEvent.VK_SHIFT;
	private static final int RELEASE_COUNT = 5;
	
	private final Logger log =
			Logger.getLogger(Keyboard.class.getName());
	private final Trabatar parent;
	private final Keymap keymap;
	
	private int releaseTracker;
	
	Keyboard(Trabatar parent)
	{
		this.parent = Objects.requireNonNull(parent);
		keymap = new Keymap();
		releaseTracker = 0;
	}
	
	@Override
	public void keyPressed(KeyEvent evt)
	{
		if(! parent.isActive())
		{
			return;
		}
		
		Optional<Keycode> key = keymap.get(evt.getKeyCode());
		if(log.isLoggable(Level.FINE))
		{
			log.fine(String.format("Down: %d (%s)%n",
				evt.getKeyCode(),
				key.orElse(null)));
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
		
		Optional<Keycode> key = keymap.get(evt.getKeyCode());
		if(log.isLoggable(Level.FINE))
		{
			log.fine(String.format("Up: %d (%s)%n",
				evt.getKeyCode(),
				key.orElse(null)));
		}
		
		if(key.isPresent())
		{
			int kc = key.get().getCode();
			parent.sendData(0x40 + (kc & 0x0F));
			parent.sendData(0x58 + ((kc & 0xF0) >> 4));
		}
		
		/*
		 * Track the number of times the user presses the release key. When that
		 * exceeds the release count, then we stop operations.
		 */
		if(evt.getKeyCode() == RELEASE_KEY)
		{
			releaseTracker++;
		}
		else
		{
			releaseTracker = 0;
		}
		if(releaseTracker >= RELEASE_COUNT)
		{
			releaseTracker = 0;
			parent.commandRelease();
		}
	}

	@Override
	public void keyTyped(KeyEvent evt)
	{
		// ignore this event
	}
}
