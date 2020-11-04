package com.mnaruzny.revolutionbot.registry.entities;

import com.mnaruzny.revolutionbot.registry.DataConnector;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class NNNStatus {

    private final Properties config;
    private final Member member;

    public NNNStatus(Properties config, Member member) {
        this.config = config;
        this.member = member;
    }

    public boolean hasUserJoined(){
        try {
            Connection conn = new DataConnector(config).getConnection();
            String sql = "SELECT userJoined FROM revolutionbot.rv_nnnStatus WHERE discordUserId = ? AND discordGuildId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, member.getIdLong());
            pstmt.setLong(2, member.getGuild().getIdLong());
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return (rs.getBoolean("userJoined"));
            } else {
                addToDB();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private void addToDB(){
        try {
            Connection conn = new DataConnector(config).getConnection();
            String sql = "INSERT INTO revolutionbot.rv_nnnStatus (discordUserId, discordGuildId) VALUE (?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, member.getIdLong());
            pstmt.setLong(2, member.getGuild().getIdLong());
            pstmt.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static NNNStatus getStatus(Properties config, Member member){
        return new NNNStatus(config, member);
    }

}
