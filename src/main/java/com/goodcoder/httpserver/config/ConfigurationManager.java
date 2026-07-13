package com.goodcoder.httpserver.config;

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

    public void loadConfigurationFile(String filePath) throws IOException, FileNotFoundException {
        try {
            FileReader fileReader = new FileReader(filePath);
            StringBuffer sb = new StringBuffer();
            int i;
            while ((i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
            JsonNode node = Json.parse(sb.toString());
            myConfiguration = Json.fromJson(node, Configuration.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration getConguration() {
        if (myConfiguration == null) {
            throw new HttpConfigurationException("No current configuration set");
        }
        return myConfiguration;
    }
}
