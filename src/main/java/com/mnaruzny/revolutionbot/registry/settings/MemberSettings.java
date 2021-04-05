package com.mnaruzny.revolutionbot.registry.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberSettings {

    private final Connection conn;
    private final long memberId;
    private final long guildId;

    public MemberSettings(Connection conn, long memberId, long guildId){
        this.conn = conn;
        this.memberId = memberId;
        this.guildId = guildId;
    }

    public boolean isAdmin() throws SQLException {
        String sql = "SELECT * FROM revolutionbot.rv_guildUsers WHERE memberid = ? AND guildid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, guildId);
            ResultSet rs = pstmt.executeQuery();
            try {
                rs.next();
                return rs.getBoolean("isAdmin");
            } catch (SQLException exception) {
                registerMember();
                return false;
            }
        }
    }

    public void setAdmin(boolean isAdmin) throws SQLException {

    }

    private void registerMember() throws SQLException {
        String sql = "INSERT INTO revolutionbot.rv_guildUsers (memberid, guildid) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, guildId);
            pstmt.executeUpdate();
        }
    }

}
