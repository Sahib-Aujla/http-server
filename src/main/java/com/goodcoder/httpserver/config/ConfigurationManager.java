package com.goodcoder.httpserver.config;

public class ConfigurationManager {
    private static ConfigurationManager myConfigurationManager;

    private ConfigurationManager(){

    }

    public static ConfigurationManager getInstance(){
        if(myConfigurationManager==null){
            myConfigurationManager=new ConfigurationManager();
        }
        return myConfigurationManager;
    }

    public void loadConfigurationFile(String filePath){

    }
}
