package de.draegerit.microarduinoser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

import jssc.SerialPortException;

@Path("/arduino")
@Produces(MediaType.APPLICATION_JSON)
public class Resource {

	private String portname;
	private int baudrate;
	private int databits;
	private int stopbits;
	private int parity;
	private int sleep;
	
	private String firstCharacter;
	private String lastCharacter;
	
	public Resource(String portname, int baudrate, int databits, int stopbits, int parity, int sleep, String firstCharacter, String lastCharacter) {
		super();
		this.portname = portname;
		this.baudrate = baudrate;
		this.databits = databits;
		this.stopbits = stopbits;
		this.parity = parity;
		this.sleep = sleep;
		this.firstCharacter = firstCharacter;
		this.lastCharacter = lastCharacter;
	}

	@GET
	@Timed
	public SerialValue get(
					@QueryParam("portname") Optional<String> portname,
					@QueryParam("baudrate") Optional<Integer> baudrate,
					@QueryParam("databits") Optional<Integer> databits,
					@QueryParam("stopbits") Optional<Integer> stopbits,
					@QueryParam("parity") Optional<Integer> parity,
					@QueryParam("sleep") Optional<Integer> sleep,
					@QueryParam("firstCharacter") Optional<String> firstCharacter,
					@QueryParam("lastCharacter") Optional<String> lastCharacter) throws SerialPortException, InterruptedException {
		return new SerialValue(System.currentTimeMillis(), readSerialValue(
													portname.or(this.portname),
													baudrate.or(this.baudrate),
													databits.or(this.databits),
													stopbits.or(this.stopbits),
													parity.or(this.parity),
													sleep.or(this.sleep),
													firstCharacter.or(this.firstCharacter),
													lastCharacter.or(this.lastCharacter)));
	}

	private String readSerialValue(String portname, int baudrate, int databits, int stopbits, int parity, int sleep, String firstCharacter, String lastCharacter) throws SerialPortException, InterruptedException {
		 SerialCommunication serialCommunication = new SerialCommunication.SerialCommunicationBuilder(portname).setSerialPortParameter(baudrate, databits, stopbits, parity).setSleepForClosePortEvent(sleep).build();
		 serialCommunication.openPort();
		 String readString = serialCommunication.readString();
		 serialCommunication.closePort();
		 String sub = readString.substring((readString.indexOf(firstCharacter)+1), readString.indexOf(lastCharacter));
		return sub;
	}

}