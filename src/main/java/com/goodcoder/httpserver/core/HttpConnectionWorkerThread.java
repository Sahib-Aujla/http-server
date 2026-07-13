package com.goodcoder.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {
    private Socket socket;
    private static Logger logger = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String html = "<html><head><title>simple page</title></head><body><h1>Kida Jatta, chlda fer server!<h1></body></html>";
            final String CRLF = "\r\n";
            String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length: " + html.getBytes().length + CRLF +//Header
                    CRLF + html + CRLF + CRLF; //body
            outputStream.write(response.getBytes());

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
