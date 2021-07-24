package framework.web.exception;

import framework.web.constant.StatusCode;
import lombok.Getter;

/**
 * Base Web Exception to be converted to http response with message and status code
 */
public class WebException extends RuntimeException {

    @Getter
    protected StatusCode statusCode = StatusCode.HTTP_500_INTERNAL_SERVER_ERROR;

    public WebException() {
        super();
    }

    public WebException(String message) {
        super(message);
    }

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebException(Throwable cause) {
        super(cause);
    }

    protected WebException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public WebException(StatusCode statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public WebException(String message, StatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public WebException(String message, Throwable cause, StatusCode statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public WebException(Throwable cause, StatusCode statusCode) {
        super(cause);
        this.statusCode = statusCode;
    }

    protected WebException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace, StatusCode statusCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = statusCode;
    }
}
