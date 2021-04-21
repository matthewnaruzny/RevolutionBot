package com.mnaruzny.revolutionbot.registry.settings;

import java.sql.*;

public class GuildSettings {

    private final Connection c;
    private final long id;

    public GuildSettings(Connection c, long id){
        this.c = c;
        this.id = id;
    }

    public boolean isChildSafe() throws SQLException {
        String sql = "SELECT `child-safe` FROM `revolutionbot`.rv_guildSettings WHERE id = ?";
        try(PreparedStatement pstmt = c.prepareStatement(sql)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            boolean isChildSafe = true;
            try{
                rs.next();
                isChildSafe = rs.getBoolean(1);
            } catch (SQLDataException ex) {
                newGuildSetting(id);
            }
            return isChildSafe;
        }
    }

    public void setChildSafe(boolean childSafe) throws SQLException {
        String sql = "UPDATE revolutionbot.rv_guildSettings SET `child-safe` = ? WHERE id = ?";
        try(PreparedStatement pstmt = c.prepareStatement(sql)){
            pstmt.setBoolean(1, childSafe);
            pstmt.setLong(2, id);
            pstmt.execute();
        }  catch (SQLDataException ex) {
            newGuildSetting(id);
        }
    }

    public void setMorningChannel(long channel) throws SQLException {
        String sql = "UPDATE revolutionbot.rv_guildSettings SET `morningannounce` = ? WHERE id = ?";
        try(PreparedStatement pstmt = c.prepareStatement(sql)){
            pstmt.setLong(1, channel);
            pstmt.setLong(2, id);
            pstmt.execute();
        }  catch (SQLDataException ex) {
            newGuildSetting(id);
        }
    }

    public long getMorningChannel() throws SQLException {
        return 0;
    }

    public void newGuildSetting(long id) throws SQLException {
        String sql = "INSERT INTO revolutionbot.rv_guildSettings (id) VALUE (?)";
        try(PreparedStatement pstmt = c.prepareStatement(sql)){
            pstmt.setLong(1, id);
            pstmt.execute();
        }
    }



}
