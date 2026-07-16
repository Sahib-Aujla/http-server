package com.goodcoder.http;

public class HttpRequest extends HttpMessage {


    private HttpMethod method;
    private String requestTarget;
    private String httpVersion;

    public HttpRequest() {

    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public void setRequestTarget(String requestTarget) {
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
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }
}
