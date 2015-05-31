package com.lhickin.speedmath;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class ServerHelper {

    private final Gson gson = new Gson();
    private final String baseURI = "http://tele303-scores-18.appspot.com/rest";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public void addNewPlayer(Player playerToAdd) {
        Log.e("ADD PLAYER", "ATTEMPTING TO ADD PLAYER: " + playerToAdd.getName());
        try {
            String json = gson.toJson(playerToAdd);
            Log.e("addNewPlayer", "successfully converted json");
            Log.e("addNewPlayer", "json: " + json);
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(baseURI + "/players")
                    .addHeader("content-type", "application/json")
                    .post(body)
                    .build();
            Log.e("addNewPlayer", "make a new request");
            Response response = client.newCall(request).execute();
            Log.e("Add player to server", "" + response.body().string());
        } catch (IOException e) {
            Log.e("Add player to server", " PLAYER " + playerToAdd.getName() + " already exists/server exception");
            Log.e("newgame", "error push", e);
        }
    }

    public void newGame(Game gameToAdd, Player playerOfGame) {
        Log.e("newGame", "ADDING GAME WITH SCORE: " + gameToAdd.getScore());
        try {
            //Game game = new Game(null, 300, 60, new Date().getTime());  // game ID is server-assigned, so leave null here

            String json = gson.toJson(gameToAdd);
            Log.e("newGame", "successfully converted json");
            Log.e("newGame", "json: " + json);
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(baseURI + "/players/" + playerOfGame.getName() + "/games")
                    .addHeader("content-type", "application/json")
                    .post(body)
                    .build();
            Log.e("newGame", "make a new request");
            Response response = client.newCall(request).execute();
            Log.e("Add game to server", "" + response.body().string());
        } catch (IOException ex) {
            Log.e("newGame", "" + playerOfGame.getName() + " doesn't exist/server exception");
            Log.e("newgame", ex.getMessage(), ex);
        }
    }
}
