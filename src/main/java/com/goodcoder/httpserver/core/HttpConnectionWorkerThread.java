package com.goodcoder.httpserver.core;

import com.goodcoder.http.HttpParser;
import com.goodcoder.http.HttpRequest;
import com.goodcoder.httpserver.core.io.WebRootHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            

            String html = "<html><head><title>simple page</title></head><body><h1>Kida Jatta, chlda fer server!<h1></body></html>";
            final String CRLF = "\r\n";
            String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length: " + html.getBytes().length + CRLF +//Header
                    CRLF + html + CRLF + CRLF; //body
            outputStream.write(response.getBytes());
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
}
