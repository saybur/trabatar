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

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saybur.trabatar.TrabatarGUI.Status;

/**
 * Basic program for sending mouse/keyboard data to a connected
 * <a href="https://github.com/saybur/trabular">trabular</a> configured serial
 * device.
 * 
 * @author saybur
 *
 */
public class Trabatar
{
	// messages displayed to the user
	private static final String STR_CANT_OPEN = "Can't open destination stream";
	private static final String STR_WAIT_CAPTURE = "Waiting for mouse capture...";
	private static final String STR_TRANSMITTING = "Transmitting... (SHIFT 5x to escape)";
	private static final String STR_ERROR = "Data send failed!";

	public static void main(String[] args)
	{
		if(GraphicsEnvironment.isHeadless())
		{
			throw new IllegalStateException(
					"This program must be run in a graphical environment");
		}

		// read the first argument to get the device the user wants
		if(args.length < 1)
		{
			JOptionPane.showMessageDialog(null,
				"Please provide an argument pointing to the\n"
				+ "destination where data will be written.",
				"Trabatar Error",
					JOptionPane.WARNING_MESSAGE);
			System.exit(10);
		}

		// parse where we should write
		final String writeDestinationStr = args[0];
		final Path writeDestination;
		try
		{
			writeDestination = Paths.get(writeDestinationStr);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,
					"Write destination couldn't be parsed:\n"
					+ "\"" + writeDestinationStr + "\"",
					"Trabatar Error",
					JOptionPane.WARNING_MESSAGE);
			System.exit(11);
			return;
		}

		// then finally start up
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new Trabatar(writeDestination);
			}
		});
	}
	
	private final Logger log = LoggerFactory.getLogger(Trabatar.class);
	private final TrabatarGUI gui;
	private final Robot robot;
	private final Mouse mouse;
	private final Keyboard keyboard;
	private final Path destination;
	private final OutputStream dout;
	
	private boolean error = false;
	private boolean active = false;
	
	Trabatar(Path destination)
	{
		this.destination = Objects.requireNonNull(destination);
		
		// initialize the mouse/keyboard interfaces
		mouse = new Mouse(this);
		keyboard = new Keyboard(this);
		
		// try to create the robot that will move the mouse back
		// to where it belongs when it starts to escape
		try
		{
			robot = new Robot();
		}
		catch(AWTException e)
		{
			log.error("unable to create the robot", e);
			throw new IllegalStateException(e);
		}
		
		// make the GUI
		gui = new TrabatarGUI(this);
		gui.addKeyListener(keyboard);
		
		// try to open the output system
		OutputStream doutLocal;
		try
		{
			doutLocal = Files.newOutputStream(destination,
					StandardOpenOption.WRITE);
			gui.showMessage(STR_WAIT_CAPTURE, Status.WAITING);
		}
		catch(IOException e)
		{
			log.warn("unable to open file destination", e);
			doutLocal = null;
			gui.showMessage(STR_CANT_OPEN, Status.PROBLEM);
			error = true;
		}
		dout = doutLocal;
		
		// then start for the user
		gui.setVisible(true);
	}

	void commandCapture()
	{
		if(error)
		{
			return;
		}
		
		if(active == false)
		{
			this.active = true;
			mouseEscaping();
		}
	}
	
	void commandExit()
	{
		try
		{
			dout.close();
		}
		catch(Exception e)
		{
			log.warn("unable to close write destination", e);
		}
		
		System.exit(0);
	}
	
	void commandRelease()
	{
		active = false;
		gui.showMessage(STR_WAIT_CAPTURE, Status.WAITING);
	}
	
	Path getDestination()
	{
		return destination;
	}

	Keyboard getKeyboard()
	{
		return keyboard;
	}
	
	Mouse getMouse()
	{
		return mouse;
	}

	boolean isActive()
	{
		return active;
	}

	void mouseEscaping()
	{
		final Point p = gui.getLocationOnScreen();
		final Dimension d = gui.getSize();
		final int x = (int) (p.getX() + d.getWidth() / 2);
		final int y = (int) (p.getY() + d.getHeight() / 2);
		robot.mouseMove(x, y);
	}

	void sendData(int v)
	{
		if(dout != null)
		{
			if(log.isTraceEnabled())
			{
				log.trace(Integer.toHexString(v));
			}
			
			try
			{
				dout.write(v);
				gui.showMessage(STR_TRANSMITTING, Status.GOOD);
				gui.toFront();
			}
			catch(IOException e)
			{
				log.warn("data send failed", e);
				gui.showMessage(STR_ERROR, Status.PROBLEM);
			}
		}
	}
}
