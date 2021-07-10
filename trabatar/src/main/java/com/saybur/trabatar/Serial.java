/*
 * Copyright 2016-2021 saybur
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

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

/**
 * Manages the serial connection and dispatches outgoing data.
 * 
 * @author saybur
 *
 */
public class Serial implements Closeable, Consumer<Integer>
{
	private final class Dispatcher implements Runnable
	{
		private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

		public void run()
		{
			byte[] arr = new byte[1];
			try
			{
				while (! closed)
				{
					arr[0] = (byte) (queue.take().intValue() & 0xFF);
					serialPort.writeBytes(arr, 1);
				}
			}
			catch(Exception e)
			{
				log.warn("unexpected exception in dispatcher", e);
			}
		}
	}

	public static final int BAUDRATE = 38400;
	public static final int DATA_BITS = 8;
	public static final int STOP_BITS = SerialPort.ONE_STOP_BIT;
	public static final int PARITY = SerialPort.NO_PARITY;
	public static final int OPEN_TIMEOUT = 500;
	public static final int DATA_TIMEOUT = 0;

	private final Logger log = LoggerFactory.getLogger(Serial.class);
	private final SerialPort serialPort;
	private final Dispatcher dispatcher;
	private final ExecutorService executor;

	private volatile boolean closed;

	Serial(SerialPort serialPort)
	{
		this.serialPort = Objects.requireNonNull(
				serialPort, "serialPort");
		log.info("request to open serial port \"{}\" [{}]",
				serialPort.getDescriptivePortName(),
				serialPort.getSystemPortName());

		// configure the serial port for our specification
		serialPort.setComPortParameters(
				BAUDRATE,
				DATA_BITS,
				STOP_BITS,
				PARITY);
		serialPort.setComPortTimeouts(
				SerialPort.TIMEOUT_READ_BLOCKING,
				DATA_TIMEOUT,
				DATA_TIMEOUT);
		serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

		// attempt to open the stream
		log.info("attempting to open port [{}] now...",
				serialPort.getSystemPortName());
		final boolean open = serialPort.openPort(OPEN_TIMEOUT);
		if(open)
		{
			closed = false;
			log.info("port [{}] is now open.",
					serialPort.getSystemPortName());
		}
		else
		{
			closed = true;
			log.warn("open failed, port [{}] did not report it was open.",
					serialPort.getSystemPortName());
		}

		// setup the continuous reader
		dispatcher = new Dispatcher();
		executor = Executors.newSingleThreadExecutor();
		executor.submit(dispatcher);
	}

	public void close() throws IOException
	{
		if (!closed)
		{
			log.info("closing port [{}].", serialPort.getSystemPortName());
			closed = true;
			executor.shutdownNow();
			serialPort.closePort();
		}
	}

	public boolean isClosed()
	{
		return closed;
	}

	@Override
	public void accept(Integer t)
	{
		dispatcher.queue.offer(t);
	}
}
