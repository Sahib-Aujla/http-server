package com.goodcoder.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {
    private static Logger logger = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; //32
    private static final int CR = 0x0D;
    private static final int LF = 0x0A;

    public HttpRequest parseHttpRequest(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();
        try {
            parseRequestLine(inputStreamReader, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        parseHeader(inputStreamReader, request);
        parseBody(inputStreamReader, request);
        return request;
    }

    private void parseBody(InputStreamReader inputStreamReader, HttpRequest request) {
    }

    private void parseHeader(InputStreamReader inputStreamReader, HttpRequest request) {

    }

    private void parseRequestLine(InputStreamReader inputStreamReader, HttpRequest request) throws IOException {
        int _byte;
        StringBuffer processingBuffer = new StringBuffer();
        boolean methodParsed = false, requestTargetParsed = false;
        while ((_byte = inputStreamReader.read()) >= 0) {
            if (_byte == CR) {
                //read next byte
                _byte = inputStreamReader.read();
                if (_byte == LF) {
                    logger.info("Requesting Buffer Line LF: " + processingBuffer);

                    return;
                }
            }

            if (_byte == SP) {
                if (!methodParsed) {
                    logger.info("Requesting METHOD Buffer Line: " + processingBuffer);
                    methodParsed = true;
                }
                if (!requestTargetParsed) {
                    logger.info("Requesting REQUEST Buffer Line: " + processingBuffer);
                    requestTargetParsed = true;
                }
                logger.info("Requesting Buffer Line: " + processingBuffer);
                processingBuffer.delete(0, processingBuffer.length());
            } else {
                processingBuffer.append((char) _byte);
            }
        }
    }
}
