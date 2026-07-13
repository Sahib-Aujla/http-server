package com.goodcoder.httpserver.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {
    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            String html = "<html><head><title>simple page</title></head><body><h1>Kida Jatta, chlda fer server!<h1></body></html>";
            final String CRLF = "\r\n";
            String response = "HTTP/1.1 200 OK" + CRLF + "Content-Length: " + html.getBytes().length + CRLF +//Header
                    CRLF + html + CRLF + CRLF; //body
            outputStream.write(response.getBytes());
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
