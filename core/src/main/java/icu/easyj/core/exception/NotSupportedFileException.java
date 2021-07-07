package icu.easyj.core.exception;

/**
 * 不支持的文件类型
 *
 * @author wangliang181230
 */
public class NotSupportedFileException extends RuntimeException {

	public NotSupportedFileException() {
		super();
	}

	public NotSupportedFileException(String message) {
		super(message);
	}

	public NotSupportedFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotSupportedFileException(Throwable cause) {
		super(cause);
	}

}
