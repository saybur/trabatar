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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;

/**
 * Class that manages the mouse.
 * 
 * @author saybur
 *
 */
public class Mouse implements MouseListener, MouseMotionListener
{
	private static final double DECELERATION_FACTOR = 0.5;
	private static final int BUTTON_ONE_DOWN = 0x61;
	private static final int BUTTON_TWO_DOWN = 0x62;
	private static final int BUTTON_BOTH_DOWN = 0x63;
	private static final int BUTTON_RELEASE = 0x60;
	private static final int HIGH_BITS = 0x20;
	private static final int NEGATION = 0x10;
	
	private final Trabatar parent;
	
	private int lastX, lastY;
	private boolean mouseEscaping;
	
	Mouse(Trabatar parent)
	{
		this.parent = Objects.requireNonNull(parent);
		lastX = 0;
		lastY = 0;
		mouseEscaping = false;
	}

	private void handleMovement(MouseEvent evt)
	{
		int x = evt.getX();
		int y = evt.getY();
		
		// only send data if the mouse didn't escape the
		// last time around
		if (! mouseEscaping)
		{
			movementData(x, lastX, false);
			movementData(y, lastY, true);
		}
		
		lastX = x;
		lastY = y;
	}

	private void handlePressRelease(MouseEvent evt)
	{
		final int bothButtons = MouseEvent.BUTTON1_DOWN_MASK
				| MouseEvent.BUTTON2_DOWN_MASK;
		if ((evt.getModifiersEx() & bothButtons) == bothButtons)
		{
			parent.sendData(BUTTON_BOTH_DOWN);
		}
		else if ((evt.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK)
				== MouseEvent.BUTTON1_DOWN_MASK)
		{
			parent.sendData(BUTTON_ONE_DOWN);
		}
		else if ((evt.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK)
				== MouseEvent.BUTTON2_DOWN_MASK)
		{
			parent.sendData(BUTTON_TWO_DOWN);
		}
		else
		{
			parent.sendData(BUTTON_RELEASE);
		}
	}

	@Override
	public void mouseClicked(MouseEvent evt)
	{
		if(! parent.isActive())
		{
			parent.commandCapture();
		}
	}

	@Override
	public void mouseDragged(MouseEvent evt)
	{
		if(! parent.isActive())
		{
			return;
		}
		
		handleMovement(evt);
	}

	@Override
	public void mouseEntered(MouseEvent evt)
	{
		mouseEscaping = false;
		lastX = evt.getX();
		lastY = evt.getY();
	}
	
	@Override
	public void mouseExited(MouseEvent evt)
	{
		if(! parent.isActive())
		{
			return;
		}
		
		// re-center in window
		parent.mouseEscaping();
		mouseEscaping = true;
	}
	
	@Override
	public void mouseMoved(MouseEvent evt)
	{
		if(! parent.isActive())
		{
			return;
		}
		
		handleMovement(evt);
	}

	@Override
	public void mousePressed(MouseEvent evt)
	{
		if(! parent.isActive())
		{
			return;
		}
		
		handlePressRelease(evt);
	}
	
	@Override
	public void mouseReleased(MouseEvent evt)
	{
		if(! parent.isActive())
		{
			return;
		}
		
		handlePressRelease(evt);
	}

	private void movementData(int position, int lastPosition,
			boolean verticalAxis)
	{
		int base = verticalAxis ? 0xC0 : 0x80;
		int delta = position - lastPosition;
		if (delta < 0)
		{
			base += NEGATION;
		}

		int deltaP = (int) Math.ceil(Math.abs(delta) * DECELERATION_FACTOR);
		int low = deltaP & 0x0F;
		int high = (deltaP & 0xF0) >> 4;
		parent.sendData(base + low);
		if (high > 0)
		{
			parent.sendData(base + HIGH_BITS + high);
		}
	}
}
