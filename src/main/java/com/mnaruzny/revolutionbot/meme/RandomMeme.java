package com.mnaruzny.revolutionbot.meme;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RandomMeme {

    public static MemeRequest getMeme (String subreddit) throws IOException {
        return getMeme(new URL("https://meme-api.herokuapp.com/gimme/" + subreddit + "/1"));
    }

    public static MemeRequest getMeme() throws IOException {
        return getMeme(new URL("https://meme-api.herokuapp.com/gimme/1"));
    }

    private static MemeRequest getMeme(URL url) throws IOException {

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");

        InputStream responseStream = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(responseStream));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null){
            response.append(line);
        }
        responseStream.close();

        Gson g = new Gson();

        return g.fromJson(response.toString(), MemeRequest.class);

    }
}