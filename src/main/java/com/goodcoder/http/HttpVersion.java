package com.goodcoder.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;
    private static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws BadHttpException {
        Matcher matcher = httpVersionRegexPattern.matcher(literalVersion);
        if (!matcher.find() || matcher.groupCount() != 2) {
            //throw 505
            throw new BadHttpException();
        }

        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));
        HttpVersion tempVersion = null;
        for (HttpVersion version : HttpVersion.values()) {
            if (version.LITERAL.equals(literalVersion)) {
                //we fully support the method
                return version;
            } else {
                //find the nearest compatible match
                //check if minor matches
                if (version.MAJOR == major) {
                    if (version.MINOR < minor) {
                        //it is compatible
                        tempVersion = version;
                    }
                }
            }

        }

        return tempVersion;
    }


}
