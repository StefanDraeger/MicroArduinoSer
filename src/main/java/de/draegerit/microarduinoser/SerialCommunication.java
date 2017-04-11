package de.draegerit.microarduinoser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.rowset.spi.SyncResolver;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * Klasse zum Senden eines @link{java.lang.String} an einen Seriellen ausgang.
 *
 * @author : Stefan Draeger
 * @since : 17.09.2015
 */
public final class SerialCommunication {

	/**
	 * Klasse SerialCommunicationBuilder, erzeugt mit der Methode
	 * {@link SerialCommunicationBuilder#build()} ein
	 * {@link SerialCommunication} Objekt. Dieses {@link SerialCommunication}
	 * Objekt kann vorher mit den Methoden
	 * <ul>
	 * <li>
	 * {@link SerialCommunicationBuilder#setSerialPortParameter(int, int, int, int)}
	 * , <i>und</i></li>
	 * <li>{@link SerialCommunicationBuilder#setSleepForClosePortEvent(long)}
	 * </li>
	 * </ul>
	 * parametriesiert werden. Wenn keine zusätzlichen Parameter gesetzt
	 * werden, werden folgende Standartwerte verwendet:
	 * <ul>
	 * <li>Baudrate : 9600</li>
	 * <li>Datenbits : 8</li>
	 * <li>Stopbits : 1</li>
	 * <li>Parity : 0</li>
	 * <li>SleepforClosePortEvent : 4000 ms.</li>
	 * </ul>
	 */
	public static class SerialCommunicationBuilder {

		/**
		 * Der Portname.
		 **/
		private String portname;
		/**
		 * Die Baudrate.
		 **/
		private int baudrate;
		/**
		 * Die Datenbits.
		 **/
		private int databits;
		/**
		 * Die Stopbits.
		 **/
		private int stopbits;
		/**
		 * Die Parity.
		 **/
		private int parity;
		/**
		 * Die Wartezeit bevor ein geöffneter Port geschlossen wird.
		 **/
		private long sleep = 4000;

		/**
		 * Konstruktor.
		 *
		 * @param inPortname
		 *            - der Port welcher für die Kommunikation verwendet werden
		 *            soll.
		 */
		public SerialCommunicationBuilder(final String inPortname) {
			this.portname = inPortname;
		}

		/**
		 * Setzt die benutzerdefinierten Parameter für die serielle
		 * Kommunikation.
		 *
		 * @param inBaudrate
		 *            die Baudrate
		 * @param inDatabits
		 *            die Datenbits
		 * @param inStopbits
		 *            die Stopbits
		 * @param inParity
		 *            die Parity
		 * @return liefert ein {@link SerialCommunicationBuilder} Objekt
		 */
		public final SerialCommunicationBuilder setSerialPortParameter(final int inBaudrate, final int inDatabits,
				final int inStopbits, final int inParity) {
			this.baudrate = inBaudrate;
			this.databits = inDatabits;
			this.stopbits = inStopbits;
			this.parity = inParity;
			return this;
		}

		/**
		 * Setzt den Wert für die Wartezeit bevor ein geöffneter Port
		 * geschlossen wird.
		 *
		 * @param inSleep
		 *            - die Wartezeit in Millisekunden.
		 * @return die Wartezeit als primitiver Long Wert.
		 */
		public final SerialCommunicationBuilder setSleepForClosePortEvent(final long inSleep) {
			this.sleep = inSleep;
			return this;
		}

		/**
		 * Erzeugt das {@link SerialCommunication} Objekt, aus den ggf. vorher
		 * gesetzen Benutzerdefinierten Eigenschaften.
		 *
		 * @return - ein {@link SerialCommunication} Objekt zum aufbauen einer
		 *         seriellen Verbindung.
		 * @throws SerialPortException
		 *             erzeugt eine Exception wenn beim erzeugen der Verbindung
		 *             etwas schief läuft.
		 */
		public final SerialCommunication build() throws SerialPortException {
			return new SerialCommunication(this);
		}

	}

	// Die Einstellungen für die Serielle Kommunikation, diese muss mit der
	// Empfangsseite übereinstimmen.
	/**
	 * Standardwert für die Baudrate.
	 **/
	private int baudrate = 9600;
	/**
	 * Standardwert für die Datenbits.
	 **/
	private int databits = 8;
	/**
	 * Standardwert für die Stopbits.
	 **/
	private int stopbits = 1;
	/**
	 * Standardwert für die Parity.
	 **/
	private int parity = 0;

	/**
	 * Wert für eine Wartezeit bis die Kommunikationsschnittstelle geschlossen
	 * wird.
	 **/
	private long sleep;

	/**
	 * Membervariable für den {@link SerialCommunicationPort}.
	 */
	private SerialCommunicationPort serialPort;
	/**
	 * Logger für die Ausgabe von Exceptions auf der Konsole.
	 **/
	private static Logger logger = Logger.getLogger("SerialCommunication");

	/**
	 * Konstruktor.
	 *
	 * @param builder
	 *            - benötigt einen {@link SerialCommunicationBuilder} für den
	 *            aufbau der seriellen Verbindung.
	 * @throws SerialPortException
	 *             - es wird eine {@link SerialPortException} weitergereicht
	 *             wenn der Port
	 *             <ul>
	 *             <li>nicht geöffnet</li>
	 *             <li>nicht geschlossen</li>
	 *             </ul>
	 *             werden kann.
	 */
	private SerialCommunication(final SerialCommunicationBuilder builder) throws SerialPortException {
		this.serialPort = new SerialCommunicationPort(builder.portname);
		this.baudrate = builder.baudrate;
		this.databits = builder.databits;
		this.stopbits = builder.stopbits;
		this.parity = builder.parity;
		this.sleep = builder.sleep;
	}

	/**
	 * Öffnet den seriellen Port.
	 *
	 * @throws SerialPortException
	 *             - wenn der Port bereits belegt ist oder nicht zur Verfügung
	 *             steht wird eine {@link SerialPortException} weitergereicht.
	 */
	public void openPort() throws SerialPortException {
		// öffnet den Port.
		this.serialPort.openPort();
		// setzt die Parameter
		this.serialPort.setParams(this.baudrate, this.databits, this.stopbits, this.parity);
	}

	/**
	 * Schließt den Port nach dem Ablauf von x Millisekunden.
	 *
	 * @throws SerialPortException
	 *             wenn beim schließen des Ports etwas schief geht so wird eine
	 *             {@link SerialPortException} wweiter gegeben.
	 */
	public void closePort() throws SerialPortException {
		// wartet x Millisekunden bis zum nächsten Schritt.
		// Wenn dieses nicht gemacht wird, wird der Port zu früh geschlossen
		// und der String kann nicht geschrieben werden.
		try {
			Thread.sleep(this.sleep);
		} catch (InterruptedException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
		// Schließt den Port.
		this.serialPort.closePort();
	}

	/**
	 * Sendet einen @link{java.lang.String} an die geöffnete serielle
	 * Verbindung.
	 *
	 * @param value
	 *            - der @link{java.lang.String}
	 * @return - liefert #Boolean.TRUE wenn der {@link java.lang.String}
	 *         erfolgreich geschrieben wurden, andernfalls #Boolean.FALSE wenn
	 *         der {@link java.lang.String} <b>nicht</b> erfolgreich geschrieben
	 *         wurde.
	 * @throws SerialPortException
	 *             - erzeugt eine {@link SerialPortException} wenn beim
	 *             Schreiben etwas schwerwiegendes Fehlgeschlagen ist. zbsp.:
	 *             <ul>
	 *             <li>die Methode openPort() wurde nicht vorher aufgerufen,
	 *             </li>
	 *             <li>die Verbindung konnte nicht aufgebaut werden da die
	 *             Parameter (Baudrate, Bitdata, Stopdata, Parity) nicht mit der
	 *             gegen seite übereinstimmen</li>
	 *             </ul>
	 */
	public boolean writeString(final String value) throws SerialPortException {
		final boolean[] writeResult = { false };
		// Die Methode #SerialPort.addEventListener erzeugt bei mehr als 1
		// registrierten
		// SerialPortEventListener eine Exception damit dieses hier nicht zu
		// einen abbruch führt
		// wird die Aktion nur ausgeführt wenn 1 registriertes Event existiert.
		if (this.serialPort.getRegisteredEventListener().size() == 0) {
			this.serialPort.addEventListener(new SerialPortEventListener() {

				public void serialEvent(final SerialPortEvent serialPortEvent) {
					try {
						writeResult[0] = serialPort.writeString(value);
					} catch (SerialPortException e) {
						logger.log(Level.WARNING, e.getMessage());
					}
				}
			});
		}
		return writeResult[0];
	}

	/**
	 * Ließt einen String und liefert diesen zurück.
	 * 
	 * @return
	 * @throws SerialPortException
	 * @throws InterruptedException
	 */
	public final String readString() throws SerialPortException, InterruptedException {
		final StringBuffer message = new StringBuffer();
		if (this.serialPort.getRegisteredEventListener().size() == 0) {
			this.serialPort.addEventListener(new SerialPortEventListener() {

				public synchronized void serialEvent(final SerialPortEvent event) {
					if (event.isRXCHAR() && event.getEventValue() > 0) {
						try {
							boolean recivedComplete = false;
							byte buffer[] = serialPort.readBytes();
							for (byte b : buffer) {
								if ((b == '\r' || b == '\n') && message.length() > 0) {
									recivedComplete = true;
								} else {
									message.append((char) b);
								}
							}
						} catch (SerialPortException ex) {
							logger.log(Level.WARNING, ex.getMessage());
						}
					}
				}
			});
		}
		Thread.sleep(sleep);
		return message.toString();
	}

	/**
	 * Liefert eine {@link List} mit den verfügbaren Portnamen.
	 *
	 * @return eine {@link List} mit den verfügbaren Portnamen
	 */
	public static List<String> getAvailablePortnameValues() {
		return new ArrayList<String>(Arrays.asList(SerialPortList.getPortNames()));
	}

	/**
	 * Liefert eine {@link List} mit den zulässigen werten für die Baudrate.
	 *
	 * @return eine {@link List} mit den zulässigen werten für die Baudrate.
	 */
	public static List<Integer> getAvailableBaudrateValues() {
		List<Integer> baudrateValues = new ArrayList<Integer>();
		baudrateValues.add(SerialPort.BAUDRATE_110);
		baudrateValues.add(SerialPort.BAUDRATE_300);
		baudrateValues.add(SerialPort.BAUDRATE_600);
		baudrateValues.add(SerialPort.BAUDRATE_1200);
		baudrateValues.add(SerialPort.BAUDRATE_4800);
		baudrateValues.add(SerialPort.BAUDRATE_9600);
		baudrateValues.add(SerialPort.BAUDRATE_14400);
		baudrateValues.add(SerialPort.BAUDRATE_19200);
		baudrateValues.add(SerialPort.BAUDRATE_38400);
		baudrateValues.add(SerialPort.BAUDRATE_57600);
		baudrateValues.add(SerialPort.BAUDRATE_115200);
		baudrateValues.add(SerialPort.BAUDRATE_128000);
		baudrateValues.add(SerialPort.BAUDRATE_256000);
		return baudrateValues;
	}

	/**
	 * Liefert eine {@link List} mit den zulässigen werten für die Datenbits.
	 *
	 * @return eine {@link List} mit den zulässigen werten für die Datenbits.
	 */
	public static List<Integer> getAvailableDatabitsValues() {
		List<Integer> databitsValues = new ArrayList<Integer>();
		databitsValues.add(SerialPort.DATABITS_5);
		databitsValues.add(SerialPort.DATABITS_6);
		databitsValues.add(SerialPort.DATABITS_7);
		databitsValues.add(SerialPort.DATABITS_8);
		return databitsValues;
	}

	/**
	 * Liefert eine {@link List} mit den zulässigen werten für die Stopbits.
	 *
	 * @return eine {@link List} mit den zulässigen werten für die Stopbits.
	 */
	public static List<Integer> getAvailableStopbitsValues() {
		List<Integer> stopbitsValues = new ArrayList<Integer>();
		stopbitsValues.add(SerialPort.STOPBITS_1);
		stopbitsValues.add(SerialPort.STOPBITS_1_5);
		stopbitsValues.add(SerialPort.STOPBITS_2);
		return stopbitsValues;
	}

	/**
	 * Liefert eine {@link List} mit den zulässigen werten für die Parity.
	 *
	 * @return eine {@link List} mit den zulässigen werten für die Parity.
	 */
	public static List<Integer> getAvailableParityValues() {
		List<Integer> parityValues = new ArrayList<Integer>();
		parityValues.add(SerialPort.PARITY_NONE);
		parityValues.add(SerialPort.PARITY_ODD);
		parityValues.add(SerialPort.PARITY_EVEN);
		parityValues.add(SerialPort.PARITY_MARK);
		parityValues.add(SerialPort.PARITY_SPACE);
		return parityValues;
	}

	/**
	 * Klasse SerialCommunicationPort. Erweitert die Klasse SerialPort um die
	 * Methoden
	 * <ul>
	 * <li>{@link SerialCommunicationPort#getRegisteredEventListener}</li>
	 * <li>
	 * {@link SerialCommunicationPort#removeEventListener(SerialPortEventListener eventListener)}
	 * </li>
	 * </ul>
	 * <p>
	 * Und erweitert die Methoden
	 * <ul>
	 * <li>
	 * {@link SerialCommunicationPort#addEventListener(SerialPortEventListener listener)}
	 * </li>
	 * <li>
	 * {@link SerialCommunicationPort#addEventListener(SerialPortEventListener listener, int mask)}
	 * </li>
	 * <li>{@link SerialCommunicationPort#closePort()}</li>
	 * </ul>
	 */
	public static class SerialCommunicationPort extends SerialPort {

		/**
		 * Konstruktor.
		 *
		 * @param portName
		 *            der Portname für den Aufbau der seriellen Verbindung.
		 */
		public SerialCommunicationPort(final String portName) {
			super(portName);
		}

		/**
		 * Liste mit den Registrierten SerialPortEventListener.
		 **/
		private final List<SerialPortEventListener> registeredEventListener = new ArrayList<SerialPortEventListener>();

		/**
		 * Liefert eine {@link java.util.List} mit den registrierten
		 * {@link SerialPortEventListener}.
		 *
		 * @return - eine {@link java.util.List} mit den registrierten
		 *         {@link SerialPortEventListener}.
		 */
		public final List<SerialPortEventListener> getRegisteredEventListener() {
			return registeredEventListener;
		}

		/**
		 * Entfernt einen registrierten {@link SerialPortEventListener} vom
		 * {@link SerialPort}.
		 *
		 * @param eventListener
		 *            - der {@link SerialPortEventListener} vom
		 *            {@link SerialPort} welcher entfernt werden soll.
		 */
		public final void removeEventListener(final SerialPortEventListener eventListener) {
			this.registeredEventListener.remove(eventListener);
		}

		@Override
		public final void addEventListener(final SerialPortEventListener listener) throws SerialPortException {
			super.addEventListener(listener);
			this.registeredEventListener.add(listener);
		}

		@Override
		public final void addEventListener(final SerialPortEventListener listener, final int mask)
				throws SerialPortException {
			super.addEventListener(listener, mask);
			this.registeredEventListener.add(listener);
		}

		@Override
		public final boolean closePort() throws SerialPortException {
			this.registeredEventListener.clear();
			return super.closePort();
		}
	}
}
