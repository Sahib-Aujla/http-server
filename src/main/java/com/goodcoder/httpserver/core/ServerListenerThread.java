package com.goodcoder.httpserver.core;

import com.goodcoder.httpserver.HttpServer;
import com.goodcoder.httpserver.core.io.WebRootHandler;
import com.goodcoder.httpserver.core.io.WebRootNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(ServerListenerThread.class);

    private ServerSocket serverSocket;
    private int port;
    private String webroot;

    private WebRootHandler webRootHandler;

    public ServerListenerThread(int port, String webroot) throws IOException, WebRootNotFoundException {
        this.port = port;
        this.webroot = webroot;
        this.webRootHandler = new WebRootHandler(webroot);
        serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {

        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {


                Socket socket = serverSocket.accept();
                logger.info("Connection accepted from : " + socket.getInetAddress());
                HttpConnectionWorkerThread httpConnectionWorkerThread = new HttpConnectionWorkerThread(socket,webRootHandler);
                httpConnectionWorkerThread.run();
                logger.info("Request completed");
                // }

                //handle later
                // serverSocket.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            }
        }

    }
}
