package de.draegerit.microarduinoser;

import org.junit.Test;

import jssc.SerialPortException;

public class TestCommunication {

	private String portname = "COM12";
	private int baudrate = 9600;
	private int databits = 8;
	private int stopbits = 1;
	private int parity = 0;
	private int sleep = 750;

	@Test
	public void shouldBeRecievedData() throws SerialPortException, InterruptedException {
		SerialCommunication serialCommunication = new SerialCommunication.SerialCommunicationBuilder(portname)
				.setSerialPortParameter(baudrate, databits, stopbits, parity).setSleepForClosePortEvent(sleep).build();
		for (int i = 0; i < 10; i++) {
			serialCommunication.openPort();
			String readString = serialCommunication.readString();
			serialCommunication.closePort();
			System.out.println("Ausgabe: " + readString);
		//	org.junit.Assert.assertFalse(StringUtils.isEmpty(readString));
		}
	}
}
