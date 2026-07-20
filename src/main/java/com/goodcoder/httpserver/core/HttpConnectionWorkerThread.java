package com.goodcoder.httpserver.core;

import com.goodcoder.http.*;
import com.goodcoder.httpserver.core.io.ReadFileException;
import com.goodcoder.httpserver.core.io.WebRootHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {
    private Socket socket;
    private static Logger logger = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private HttpParser httpParser = new HttpParser();
    private WebRootHandler webRootHandler;

    public HttpConnectionWorkerThread(Socket socket, WebRootHandler webRootHandler) {
        this.socket = socket;
        this.webRootHandler = webRootHandler;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            //send the input stream to http parser


            HttpRequest request = httpParser.parseHttpRequest(inputStream);

            HttpResponse response = handleRequest(request);

//            String html = "<html><head><title>simple page</title></head><body><h1>Kida Jatta, chlda fer server!<h1></body></html>";
//            final String CRLF = "\r\n";
//            String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length: " + html.getBytes().length + CRLF +//Header
//                    CRLF + html + CRLF + CRLF; //body
            outputStream.write(response.getResponseBytes());
            logger.info("Request sent back");

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("Error closing input stream, " + e);
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                logger.error("Error closing output stream, " + e);

            }
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Error closing socket connection, " + e);

            }
        }

    }


    private HttpResponse handleRequest(HttpRequest request) {
        switch (request.getMethod()) {
            case GET:
                logger.info("Mehtod GET:  Request ");
            case HEAD:
                logger.info("Method HEAD:  Request ");
            default:
                //send not implemented
                return new HttpResponse.Builder().httpVersion(request.getBestCompatibleVersion().LITERAL)
                        .statusCode(HttpStatusCode.CLIENT_ERROR_501_BAD_REQUEST)
                        .build();
        }
    }

    private HttpResponse handleGetRequest(HttpRequest request, boolean setMessageBody) {
        try {

            HttpResponse.Builder builder = new HttpResponse.Builder()
                    .httpVersion(request.getBestCompatibleVersion().LITERAL)
                    .statusCode(HttpStatusCode.OK)
                    .addHeader(HttpHeaderName.CONTENT_TYPE.headerName, webRootHandler.getFileMimePath(request.getRequestTarget()));

            if (setMessageBody) {
                byte[] messageBody = webRootHandler.getFileByteArrayData(request.getRequestTarget());
                builder.addHeader(HttpHeaderName.CONTENT_LENGTH.headerName, String.valueOf(messageBody.length))
                        .messageBody(messageBody);
            }

            return builder.build();

        } catch (FileNotFoundException e) {

            return new HttpResponse.Builder()
                    .httpVersion(request.getBestCompatibleVersion().LITERAL)
                    .statusCode(HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND)
                    .build();

        } catch (ReadFileException e) {

            return new HttpResponse.Builder()
                    .httpVersion(request.getBestCompatibleVersion().LITERAL)
                    .statusCode(HttpStatusCode.CLIENT_ERROR_500_BAD_REQUEST)
                    .build();
        }

    }
}
