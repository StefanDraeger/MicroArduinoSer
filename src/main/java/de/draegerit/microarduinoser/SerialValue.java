package de.draegerit.microarduinoser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SerialValue {
	
	private long recievedAt;

	private String value;

	public SerialValue() {
		// Jackson deserialization
	}

	public SerialValue(long recievedAt, String value) {
		this.recievedAt = recievedAt;
		this.value = value;
	}

	@JsonProperty
	public long getRecievedAt() {
		return recievedAt;
	}

	@JsonProperty
	public String getValue() {
		return value;
	}
}
