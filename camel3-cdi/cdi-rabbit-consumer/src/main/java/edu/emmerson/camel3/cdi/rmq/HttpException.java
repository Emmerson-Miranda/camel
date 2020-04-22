package edu.emmerson.camel3.cdi.rmq;

public class HttpException extends Exception {

	private static final long serialVersionUID = 8943179824070328223L;


	public HttpException(String message) {
		super(message);
	}


	@Override
	public String toString() {
		return this.getMessage();
	}

	
}
