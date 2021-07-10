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
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;
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
	public static final String STR_CANT_OPEN = "Can't open destination stream";
	public static final String STR_WAIT_CAPTURE = "Click to start...";
	public static final String STR_TRANSMITTING = "Transmitting... (RIGHT-CTRL to escape)";
	public static final String STR_ERROR = "Data send failed!";
	public static final String STR_NOT_CONNECTED = "Not connected";

	public static void main(String[] args)
	{
		if(GraphicsEnvironment.isHeadless())
		{
			throw new IllegalStateException(
					"This program must be run in a graphical environment");
		}

		SwingUtilities.invokeLater(Trabatar::new);
	}
	
	private final Logger log = LoggerFactory.getLogger(Trabatar.class);
	private final TrabatarGUI gui;
	private final Robot robot;
	private final Mouse mouse;
	private final Keyboard keyboard;
	
	private volatile Optional<Serial> serial = Optional.empty();
	private boolean error = false;
	private boolean active = false;
	
	Trabatar()
	{
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

		// then start for the user
		gui.setVisible(true);
	}

	void commandCapture()
	{
		if(error || ! serial.isPresent())
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
		if(serial.isPresent())
		{
			try
			{
				serial.get().close();
			}
			catch(Exception e)
			{
				log.warn("unable to close serial port", e);
			}
		}
		
		System.exit(0);
	}
	
	void commandRelease()
	{
		active = false;
		gui.showMessage(STR_WAIT_CAPTURE, Status.WAITING);
	}
	
	void commandOpen()
	{
		// fetch the serial port options
		final SerialPort[] ports = SerialPort.getCommPorts();

		// fallback in the event there are no ports to pick from
		if(ports.length == 0)
		{
			log.info("no serial ports detected");
			JOptionPane.showMessageDialog(gui,
					"No serial ports were detected.",
					"Trabatar Error",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		// make a parallel list of names for the ports
		final Object[] portNames = Arrays.stream(ports)
				.map(p ->
				{
					return String.format("%s [%s]",
							p.getDescriptivePortName(),
							p.getSystemPortName());
				})
				.toArray();

		// prompt the user to pick one
		Object option = JOptionPane.showInputDialog(gui,
				"Select a communication port to open:",
				"Trabatar Message",
				JOptionPane.PLAIN_MESSAGE,
				null,
				portNames,
				portNames[0]);
		if(option == null)
		{
			// user cancelled
			return;
		}

		// figure out the one they picked
		SerialPort portSelected = null;
		if(option != null)
		{
			for(int i = 0; i < portNames.length; i++)
			{
				if(option.equals(portNames[i]))
				{
					portSelected = ports[i];
					break;
				}
			}
		}
		// in case it isn't found, which shouldn't be possible
		if(portSelected == null)
		{
			log.warn("user picked serial port [{}], but it was not found.",
					option);
			return;
		}

		// if serial exists, close it
		if(serial.isPresent())
		{
			try
			{
				serial.get().close();
			}
			catch(Exception e)
			{
				// swallow, but log
				log.warn("exception during existing port close.", e);
			}
		}

		// attempt to open the new port, and report any issues appropriately
		try
		{
			final Serial newSerial = new Serial(portSelected);
			if(! newSerial.isClosed())
			{
				serial = Optional.of(newSerial);
				JOptionPane.showMessageDialog(gui,
						"Connection opened!",
						"Trabatar Message",
						JOptionPane.INFORMATION_MESSAGE);
				gui.showMessage(STR_WAIT_CAPTURE, Status.WAITING);
			}
			else
			{
				serial = Optional.empty();
				JOptionPane.showMessageDialog(gui,
						"Unable to open the connection!",
						"Trabatar Error",
						JOptionPane.ERROR_MESSAGE);
				gui.showMessage(STR_NOT_CONNECTED, Status.PROBLEM);
			}
		}
		catch(Exception e)
		{
			log.warn("unexpected error during opening process.", e);
			serial = Optional.empty();
			JOptionPane.showMessageDialog(gui,
					"Unexpected error during the opening process.",
					"Trabatar Error",
					JOptionPane.ERROR_MESSAGE);
			gui.showMessage(STR_CANT_OPEN, Status.PROBLEM);
		}
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
		if(serial.isPresent())
		{
			if(log.isTraceEnabled())
			{
				log.trace(Integer.toHexString(v));
			}
			
			try
			{
				serial.get().accept(v);
				gui.showMessage(STR_TRANSMITTING, Status.GOOD);
				gui.toFront();
			}
			catch(Exception e)
			{
				log.warn("data send failed", e);
				gui.showMessage(STR_ERROR, Status.PROBLEM);
			}
		}
	}
}
