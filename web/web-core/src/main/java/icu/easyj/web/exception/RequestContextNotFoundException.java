package icu.easyj.web.exception;

/**
 * HTTP请求上下文未找到的异常
 *
 * @author wangliang181230
 */
public class RequestContextNotFoundException extends RuntimeException {

	public RequestContextNotFoundException() {
		super("HTTP请求上下文未找到");
	}

	public RequestContextNotFoundException(String message) {
		super(message);
	}

	public RequestContextNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestContextNotFoundException(Throwable cause) {
		super(cause);
	}
}
