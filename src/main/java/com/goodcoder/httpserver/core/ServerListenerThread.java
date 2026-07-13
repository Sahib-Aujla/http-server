package com.goodcoder.httpserver.core;

import com.goodcoder.httpserver.HttpServer;
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
    private String webport;

    public ServerListenerThread(int port, String webport) throws IOException {
        this.port = port;
        this.webport = webport;
        serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {

        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {


                Socket socket = serverSocket.accept();
                logger.info("Connection accepted from : " + socket.getInetAddress());
                HttpConnectionWorkerThread httpConnectionWorkerThread = new HttpConnectionWorkerThread(socket);
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
