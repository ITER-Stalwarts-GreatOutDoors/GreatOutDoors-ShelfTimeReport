package com.cg.iter.shelftimereport.exception;

public class UserException extends Exception {

	/**
	 * 
	 */
	public UserException() {
		super();

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserException(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 */
	public UserException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public UserException(Throwable cause) {
		super(cause);

	}

}
