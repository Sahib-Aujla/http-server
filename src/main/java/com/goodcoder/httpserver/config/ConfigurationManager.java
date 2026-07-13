package com.goodcoder.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.goodcoder.httpserver.util.HttpConfigurationException;
import com.goodcoder.httpserver.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myConfiguration;

    private ConfigurationManager() {

    }

    public static ConfigurationManager getInstance() {
        if (myConfigurationManager == null) {
            myConfigurationManager = new ConfigurationManager();
        }
        return myConfigurationManager;
    }

    public void loadConfigurationFile(String filePath) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException("File not found");
        }
        StringBuffer sb = new StringBuffer();
        int i;
        while (true) {
            try {
                if (!((i = fileReader.read()) != -1)) break;
            } catch (IOException e) {
                throw new HttpConfigurationException(e);
            }
            sb.append((char) i);
        }
        JsonNode node = null;
        try {
            node = Json.parse(sb.toString());
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing configuration file",e);
        }
        try {
            myConfiguration = Json.fromJson(node, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing configuration file, internal",e);
        }

    }

    public Configuration getConguration() {
        if (myConfiguration == null) {
            throw new HttpConfigurationException("No current configuration set");
        }
        return myConfiguration;
    }
}
