package com.goodcoder.http;

public enum HttpStatusCode {
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),

    CLIENT_ERROR_401_BAD_REQUEST(401, "Method Not Allowed"),

    CLIENT_ERROR_414_BAD_REQUEST(414, "URI Too Long"),

    //server error
    CLIENT_ERROR_500_BAD_REQUEST(500, "Internal Server Error"),
    CLIENT_ERROR_501_BAD_REQUEST(501, "Not Implemented"),
    CLIENT_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "Http Version Not Supporteed");


    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatusCode(int statusCode, String message) {
        STATUS_CODE = statusCode;
        MESSAGE = message;
    }
}
