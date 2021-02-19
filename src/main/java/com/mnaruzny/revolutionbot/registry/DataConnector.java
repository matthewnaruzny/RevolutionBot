package com.mnaruzny.revolutionbot.registry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataConnector {

    private final Properties config;

    public DataConnector(Properties config){
        this.config = config;
    }

    public Connection getConnection() throws SQLException {

        String ip = config.getProperty("dbIp");
        String url = ("jdbc:mariadb://" + ip);

        return DriverManager.getConnection(url, config.getProperty("dbUsername"), config.getProperty("dbPassword"));

    }

    public SmartReplies getSmartReplies() throws SQLException {
        return new SmartReplies(getConnection());
    }

}
