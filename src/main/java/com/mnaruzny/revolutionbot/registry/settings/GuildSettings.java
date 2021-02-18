package com.mnaruzny.revolutionbot.registry.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            return rs.getBoolean(1);
        }
    }

    public void setChildSafe(boolean childSafe) throws SQLException{
        String sql = "UPDATE revolutionbot.rv_guildSettings SET `child-safe` = ? WHERE id = ?";
        try(PreparedStatement pstmt = c.prepareStatement(sql)){
            pstmt.setBoolean(1, childSafe);
            pstmt.setLong(1, id);
            pstmt.execute();
        }
    }



}
