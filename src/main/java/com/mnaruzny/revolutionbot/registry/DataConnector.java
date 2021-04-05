package com.mnaruzny.revolutionbot.registry;

import com.mnaruzny.revolutionbot.registry.settings.GuildSettings;
import com.mnaruzny.revolutionbot.registry.settings.MemberSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnector {

    public Connection getConnection() throws SQLException {

        String ip = System.getenv("DBIP");
        String url = ("jdbc:mariadb://" + ip);

        return DriverManager.getConnection(url, System.getenv("DBUSR"), System.getenv("DBPASS"));

    }

    public SmartReplies getSmartReplies() throws SQLException {
        return new SmartReplies(getConnection());
    }

    public GuildSettings getGuildSettings(long id) throws SQLException {
        return new GuildSettings(getConnection(), id);
    }

    public MemberSettings getMemberSettings(long memberid, long guildid) throws SQLException {
        return new MemberSettings(getConnection(), memberid, guildid);
    }

    public MusicList getMusicList() throws SQLException {
        return new MusicList(getConnection());
    }

}
