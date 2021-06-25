package org.easyj.core.exception;

/**
 * Skip Callback Wrapper Exception.
 *
 * @author wangliang181230
 */
public class SkipCallbackWrapperException extends WrapperException {

	public SkipCallbackWrapperException(Throwable cause) {
		super(cause);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		// do nothing
		return null;
	}
}
