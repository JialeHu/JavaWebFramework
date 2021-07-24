package framework.web.exception;

import framework.web.constant.MediaType;
import framework.web.constant.StatusCode;
import lombok.Getter;

public class APIException extends WebException {

    @Getter
    protected String body;
    @Getter
    protected MediaType mediaType = MediaType.TEXT_PLAIN;

    public APIException(String body) {
        super();
        this.body = body;
    }

    public APIException(String message, String body) {
        super(message);
        this.body = body;
    }

    public APIException(String message, Throwable cause, String body) {
        super(message, cause);
        this.body = body;
    }

    public APIException(Throwable cause, String body) {
        super(cause);
        this.body = body;
    }

    protected APIException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace,
                           String body) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.body = body;
    }

    public APIException(String body, MediaType mediaType) {
        super();
        this.body = body;
        this.mediaType = mediaType;
    }

    public APIException(String message, String body, MediaType mediaType) {
        super(message);
        this.body = body;
        this.mediaType = mediaType;
    }

    public APIException(String message, Throwable cause, String body, MediaType mediaType) {
        super(message, cause);
        this.body = body;
        this.mediaType = mediaType;
    }

    public APIException(Throwable cause, String body, MediaType mediaType) {
        super(cause);
        this.body = body;
        this.mediaType = mediaType;
    }

    protected APIException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace,
                           String body,
                           MediaType mediaType) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.body = body;
        this.mediaType = mediaType;
    }

    public APIException(StatusCode statusCode, String body) {
        super();
        this.statusCode = statusCode;
        this.body = body;
    }

    public APIException(String message, StatusCode statusCode, String body) {
        super(message);
        this.statusCode = statusCode;
        this.body = body;
    }

    public APIException(String message, Throwable cause, StatusCode statusCode, String body) {
        super(message, cause);
        this.statusCode = statusCode;
        this.body = body;
    }

    public APIException(Throwable cause, StatusCode statusCode, String body) {
        super(cause);
        this.statusCode = statusCode;
        this.body = body;
    }

    protected APIException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace,
                           StatusCode statusCode,
                           String body) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = statusCode;
        this.body = body;
    }

    public APIException(StatusCode statusCode, String body, MediaType mediaType) {
        super();
        this.statusCode = statusCode;
        this.body = body;
        this.mediaType = mediaType;
    }

    public APIException(String message, StatusCode statusCode, String body, MediaType mediaType) {
        super(message);
        this.statusCode = statusCode;
        this.body = body;
        this.mediaType = mediaType;
    }

    public APIException(String message, Throwable cause, StatusCode statusCode, String body, MediaType mediaType) {
        super(message, cause);
        this.statusCode = statusCode;
        this.body = body;
        this.mediaType = mediaType;
    }

    public APIException(Throwable cause, StatusCode statusCode, String body, MediaType mediaType) {
        super(cause);
        this.statusCode = statusCode;
        this.body = body;
        this.mediaType = mediaType;
    }

    protected APIException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace,
                           StatusCode statusCode,
                           String body,
                           MediaType mediaType) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = statusCode;
        this.body = body;
        this.mediaType = mediaType;
    }

}
