package com.mnaruzny.revolutionbot.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadPropertyFile {

    public static Properties getProperties(String path) throws IOException {
        Properties prop = new Properties();
        FileInputStream ip = new FileInputStream(path);
        prop.load(ip);
        return prop;
    }

}
