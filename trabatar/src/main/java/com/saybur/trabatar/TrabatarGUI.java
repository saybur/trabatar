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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates the GUI for managing user I/O.
 * 
 * @author saybur
 *
 */
public final class TrabatarGUI extends JFrame
{
	public enum Status
	{
		GOOD(new Color(189, 244, 189)),
		WAITING(new Color(244, 241, 189)),
		PROBLEM(new Color(244, 189, 189));
		
		private final Color color;
		
		private Status(Color color)
		{
			this.color = color;
		}
	}
	
	private static final long serialVersionUID = -43242356441676430L;
	private static final String TITLE_FORMAT = "Trabatar";
	private static final String ICON_PATH =
			"com/saybur/trabatar/icon.png";
	private static final String BACKGROUND_PATH =
			"com/saybur/trabatar/background.png";
	private static final Dimension BACKGROUND_SIZE =
			new Dimension(512, 342);
	
	private final Logger log = LoggerFactory.getLogger(TrabatarGUI.class);
	private final JLabel messageLabel;

	TrabatarGUI(final Trabatar parent)
	{
		Objects.requireNonNull(parent);
		
		// set basic parameters
		setTitle(String.format(TITLE_FORMAT));
		setResizable(false);
		
		// attempt to use the standard icon for the frame
		try
		{
			final ImageIcon icon = new ImageIcon(getClass()
					.getClassLoader()
					.getResource(ICON_PATH));
			setIconImage(icon.getImage());
		}
		catch(Exception e)
		{
			log.warn("unable to load program icon", e);
		}

		// register program exit to window close
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				parent.commandExit();
			}
		});

		// make the component that the mouse is trapped within
		final JLabel core = new JLabel();
		core.setPreferredSize(BACKGROUND_SIZE);
		core.addMouseListener(parent.getMouse());
		core.addMouseMotionListener(parent.getMouse());
		try
		{
			final ImageIcon background = new ImageIcon(getClass()
					.getClassLoader()
					.getResource(BACKGROUND_PATH));
			core.setIcon(background);
		}
		catch(Exception e)
		{
			log.warn("unable to load program background", e);
		}
		
		// and the message component
		messageLabel = new JLabel();
		messageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		messageLabel.setBorder(BorderFactory
				.createEmptyBorder(10, 10, 10, 10));
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setOpaque(true);
		showMessage("Initializing...", Status.WAITING);
		
		// then pull the GUI together
		final JPanel content = new JPanel(new BorderLayout());
		content.add(core, BorderLayout.CENTER);
		content.add(messageLabel, BorderLayout.SOUTH);
		setContentPane(content);
		pack();
	}
	
	public void showMessage(String message, Status status)
	{
		Objects.requireNonNull(message);
		Objects.requireNonNull(status);
		
		messageLabel.setText(message);
		messageLabel.setBackground(status.color);
	}
}
