package com.goodcoder.httpserver;

import com.goodcoder.httpserver.config.Configuration;
import com.goodcoder.httpserver.config.ConfigurationManager;

public class HttpServer {

    public static void main(String [] args){
        System.out.println("startign http server...");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf=ConfigurationManager.getInstance().getConguration();


        System.out.println("Port  " +conf.getPort());
        System.out.println("Webroot  " +conf.getWebroot());

    }
}
