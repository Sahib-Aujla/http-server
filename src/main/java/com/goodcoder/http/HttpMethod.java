package com.goodcoder.http;

public enum HttpMethod {
    GET, HEAD;
    public static final int MAX_LENGTH;

    static {
        int tempLength = -1;
        for (HttpMethod method : HttpMethod.values()) {
            if (method.name().length() > tempLength) {
                tempLength = method.name().length();
            }
        }
        MAX_LENGTH = tempLength;
    }
}
