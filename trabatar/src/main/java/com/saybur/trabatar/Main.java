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

import java.awt.GraphicsEnvironment;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
	public static void main(String[] args)
	{
		// are we in the GUI?
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
		
		// setup the logger
		final Logger log = LoggerFactory.getLogger(Main.class);
		
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
		log.info("will write to desintation \"" + writeDestination + "\"");
		
		// then finally start up
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new Trabatar(writeDestination);
			}
		});
	}
}
