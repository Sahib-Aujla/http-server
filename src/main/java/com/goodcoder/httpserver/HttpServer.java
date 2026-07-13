package com.goodcoder.httpserver;

import com.goodcoder.httpserver.config.Configuration;
import com.goodcoder.httpserver.config.ConfigurationManager;
import com.goodcoder.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class HttpServer {
    private final static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        logger.info("startign http server...");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getConguration();


        logger.info("Port  " + conf.getPort());
        logger.info("Webroot  " + conf.getWebroot());
        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.run();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }


    }
}
