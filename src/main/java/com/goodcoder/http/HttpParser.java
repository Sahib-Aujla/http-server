package com.goodcoder.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpParser {
    private static Logger logger = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; //32
    private static final int CR = 0x0D;
    private static final int LF = 0x0A;

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(inputStreamReader, request);
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }

        try {
            parseHeader(inputStreamReader, request);
        } catch (IOException e) {
            // throw new RuntimeException(e);
        }
        parseBody(inputStreamReader, request);
        return request;
    }

    private void parseBody(InputStreamReader inputStreamReader, HttpRequest request) {
    }

    private void parseHeader(InputStreamReader inputStreamReader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processingBuffer = new StringBuilder();
        boolean crlfFound = false;
        int _byte;
        while ((_byte = inputStreamReader.read()) >= 0) {
            if (_byte == CR) {
                _byte = inputStreamReader.read();
                if (_byte == LF) {
                    if (!crlfFound) {
                        crlfFound = true;
                        processSingleHeaderField(processingBuffer, request);
                        processingBuffer.delete(0, processingBuffer.length());
                    } else {
                        // Two CRLF received, end of Headers section
                        return;
                    }

                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            } else {
                crlfFound = false;
                processingBuffer.append((char) _byte);
            }


        }

    }

    private void processSingleHeaderField(StringBuilder processingDataBuffer, HttpRequest request) throws HttpParsingException {
        String rawHeaderField = processingDataBuffer.toString();
        Pattern pattern = Pattern.compile("^(?<fieldName>[!#$%&’*+\\-./^_‘|˜\\dA-Za-z]+):\\s?(?<fieldValue>[!#$%&’*+\\-./^_‘|˜(),:;<=>?@[\\\\]{}\" \\dA-Za-z]+)\\s?$");

        Matcher matcher = pattern.matcher(rawHeaderField);
        if (matcher.matches()) {
            // We found a proper header
            String fieldName = matcher.group("fieldName");
            String fieldValue = matcher.group("fieldValue");
            request.addHeader(fieldName, fieldValue);
        } else {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private void parseRequestLine(InputStreamReader inputStreamReader, HttpRequest request) throws HttpParsingException, IOException {
        int _byte;
        StringBuffer processingBuffer = new StringBuffer();
        boolean methodParsed = false, requestTargetParsed = false;
        while ((_byte = inputStreamReader.read()) >= 0) {
            if (_byte == CR) {
                //read next byte
                _byte = inputStreamReader.read();
                if (_byte == LF) {
                    logger.info("Requesting Buffer Line LF: " + processingBuffer);
                    if (!methodParsed || !requestTargetParsed) {
                        //empty request line
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    try {
                        request.setHttpVersion(processingBuffer.toString());
                    } catch (BadHttpException e) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    return;
                } else {
                    // if after /r no /n is found
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }

            if (_byte == SP) {
                if (!methodParsed) {
                    try {
                        request.setMethod(processingBuffer.toString());
                    } catch (HttpParsingException e) {
                        throw new HttpParsingException(e.getErrorCode());
                    }
                    logger.info("Requesting METHOD Buffer Line: " + processingBuffer);
                    methodParsed = true;
                } else if (!requestTargetParsed) {
                    logger.info("Requesting REQUEST Buffer Line: " + processingBuffer);
                    request.setRequestTarget(processingBuffer.toString());
                    requestTargetParsed = true;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                logger.info("Requesting Buffer Line: " + processingBuffer);
                processingBuffer.delete(0, processingBuffer.length());
            } else {
                processingBuffer.append((char) _byte);
                //check the length of the buffer, if larger than the HTTP max length than not implemeneted error
                if (!methodParsed) {
                    if (processingBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_501_BAD_REQUEST);
                    }
                }
            }
        }
    }
}
