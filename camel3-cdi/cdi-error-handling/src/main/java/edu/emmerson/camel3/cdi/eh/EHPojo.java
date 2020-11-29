package edu.emmerson.camel3.cdi.eh;

/**
 * Error handling pojo
 * @author emmerson
 *
 */
public class EHPojo {
	
	private int value;
	private String message;
	
	public EHPojo() {
		super();
	}

	public EHPojo(int value, String message) {
		super();
		this.value = value;
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
