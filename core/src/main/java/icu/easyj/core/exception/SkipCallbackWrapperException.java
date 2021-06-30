package icu.easyj.core.exception;

/**
 * Skip Callback Wrapper Exception.
 *
 * @author wangliang181230
 */
public class SkipCallbackWrapperException extends WrapperException {

	public SkipCallbackWrapperException(String message, Throwable cause) {
		super(message, cause);
	}

	public SkipCallbackWrapperException(Throwable cause) {
		super(cause);
	}
}
