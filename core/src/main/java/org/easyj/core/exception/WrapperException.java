package org.easyj.core.exception;

/**
 * Wrapper Exception.
 *
 * @author wangliang181230
 */
public class WrapperException extends RuntimeException {

	public WrapperException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrapperException(Throwable cause) {
		super(cause);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		// do nothing
		return null;
	}
}
