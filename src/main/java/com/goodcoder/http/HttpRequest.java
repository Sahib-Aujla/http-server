package com.goodcoder.http;

import java.util.HashMap;
import java.util.Set;

public class HttpRequest extends HttpMessage {


    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion;
    private HttpVersion bestCompatibleHttpVersion;
    private final HashMap<String, String> headers = new HashMap<>();

    public HttpRequest() {

    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.isEmpty()) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_500_BAD_REQUEST);
        }
        this.requestTarget = requestTarget;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(String method) throws HttpParsingException {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (method.equals(httpMethod.name())) {
                this.method = HttpMethod.valueOf(method);
                return;
            }
        }
        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_501_BAD_REQUEST);
    }

    public String getHttpVersion() {
        return originalHttpVersion;
    }

    public void setHttpVersion(String httpVersion) throws BadHttpException, HttpParsingException {
        this.originalHttpVersion = httpVersion;
        this.bestCompatibleHttpVersion = HttpVersion.getBestCompatibleVersion(httpVersion);
        if (bestCompatibleHttpVersion == null) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    public void addHeader(String fieldName, String fieldValue) {
        headers.put(fieldName.toLowerCase(), fieldValue);
    }

    public Set<String> getHeaders() {
        return headers.keySet();
    }

    public String getHeaderName(String headerName) {
        return headers.get(headerName.toLowerCase());
    }
}
