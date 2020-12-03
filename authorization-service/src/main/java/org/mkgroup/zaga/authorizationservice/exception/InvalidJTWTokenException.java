package org.mkgroup.zaga.authorizationservice.exception;

public class InvalidJTWTokenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5799605029633953931L;

	public InvalidJTWTokenException(Throwable e) {
		super(e);
	}
	
}