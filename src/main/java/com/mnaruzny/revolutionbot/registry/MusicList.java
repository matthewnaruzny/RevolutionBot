package com.mnaruzny.revolutionbot.registry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicList {

    private final Connection c;

    public MusicList(Connection c){
        this.c = c;
    }

    public List<String> getList() throws SQLException {

        List<String> urls = new ArrayList<>();

        String sql = "SELECT * FROM revolutionbot.rv_musicList";
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            urls.add(rs.getString("url"));
        }
        return urls;
    }

}
