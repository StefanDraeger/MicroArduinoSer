package de.draegerit.microarduinoser.configuration;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MicroArduinoSerConfiguration extends io.dropwizard.Configuration {

	@NotNull
	private String portname;
	
	@NotNull
	private int baudrate;
	
	@NotNull
	private int databits;
	
	@NotNull
	private int stopbits;
	
	@NotNull
	private int parity;
	
	@NotNull
	private int sleep;
	
	@NotNull
	private String firstCharacter;
	
	@NotNull
	private String lastCharacter;
	
	
	@JsonProperty
	public String getPortname() {
		return portname;
	}
	
	@JsonProperty
	public void setPortname(String portname) {
		this.portname = portname;
	}
	
	@JsonProperty
	public int getBaudrate() {
		return baudrate;
	}
	
	@JsonProperty
	public void setBaudrate(int baudrate) {
		this.baudrate = baudrate;
	}
	
	@JsonProperty
	public int getDatabits() {
		return databits;
	}
	
	@JsonProperty
	public void setDatabits(int databits) {
		this.databits = databits;
	}

	@JsonProperty
	public int getStopbits() {
		return stopbits;
	}

	@JsonProperty
	public void setStopbits(int stopbits) {
		this.stopbits = stopbits;
	}

	@JsonProperty
	public int getParity() {
		return parity;
	}

	@JsonProperty
	public void setParity(int parity) {
		this.parity = parity;
	}

	@JsonProperty
	public int getSleep() {
		return sleep;
	}

	@JsonProperty
	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	@JsonProperty
	public String getFirstCharacter() {
		return firstCharacter;
	}

	@JsonProperty
	public void setFirstCharacter(String firstCharacter) {
		this.firstCharacter = firstCharacter;
	}

	@JsonProperty
	public String getLastCharacter() {
		return lastCharacter;
	}

	@JsonProperty
	public void setLastCharacter(String lastCharacter) {
		this.lastCharacter = lastCharacter;
	}
	
}
