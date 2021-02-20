package com.mnaruzny.revolutionbot.registry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SmartReplies {

    private final Connection connection;

    public SmartReplies(Connection connection){
        this.connection = connection;
    }

    public List<String> getReplies(String category) throws SQLException{
        String sql = "SELECT * FROM revolutionbot.rv_smartWordlist WHERE category = ?";
        List<String> words = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                words.add(rs.getString("word"));
            }
        }
        return words;
    }

    public HashMap<String, ArrayList<String>> getTrainingData() throws SQLException {
        String sql = "SELECT * FROM revolutionbot.rv_smartRepliesTraining";
        HashMap<String, ArrayList<String>> trainingData = new HashMap<>();
        try (ResultSet rs = connection.createStatement().executeQuery(sql)){
            while(rs.next()){
                String category = rs.getString("category");
                if(trainingData.containsKey(category)){
                    ArrayList<String> tc = trainingData.get(category);
                    tc.add(rs.getString("word"));
                } else {
                    ArrayList<String> tC = new ArrayList<>();
                    tC.add(rs.getString("word"));
                    trainingData.put(category, tC);
                }
            }
        }
        return trainingData;
    }

    public void addTrainingData(String category, String[] words) throws SQLException {
        String sql = "INSERT INTO revolutionbot.rv_smartRepliesTraining (category, word) VALUES (?,?)";
        String dbWords = String.join(" ", words);
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, category);
            pstmt.setString(2, dbWords);
            pstmt.execute();
        }

    }
}
