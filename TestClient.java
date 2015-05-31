package client;

import com.google.gson.Gson;
import com.joejernst.http.Request;
import com.joejernst.http.Response;
import java.io.IOException;
import java.util.Date;
import model.Equation;
import model.Game;
import model.Player;

public class TestClient {

//	private final String baseURI = "http://localhost:8080/rest";
	private final String baseURI = "http://tele303-backend-mg.appspot.com/rest";

	private final Gson gson = new Gson();

	public void getPlayers() throws IOException {
		System.out.println("\n-- Get Players --");
		String response = new Request(baseURI + "/players").getResource().getBody();

		Player[] players = gson.fromJson(response, Player[].class);

		for (Player player : players) {
			System.out.println(player);
		}
	}

	public void addNewPlayer() {
		System.out.println("\n-- Add Player --");

		try {
			Player fred = new Player("joe", "Joe");
			String json = gson.toJson(fred);

			Response resp = new Request(baseURI + "/players")
				.addHeader("content-type", "application/json")
				.setBody(json)
				.postResource();

			System.out.println(resp.getResponseCode());
		} catch (IOException ex) {
			System.out.println("Joe already exists");
		}
	}

	public Integer getGames() throws IOException {
		System.out.println("\n-- Get Games--");

		String response = new Request(baseURI + "/players/joe/games").getResource().getBody();

		Game[] games = gson.fromJson(response, Game[].class);

		for (Game game : games) {
			System.out.println(game);
		}

		return games[0].getGameId();
	}

	public void getEquations(Integer gameId) {
		System.out.println("\n-- Get Equations --");

		try {
			String response = new Request(baseURI + "/players/joe/games/" + gameId + "/equations").getResource().getBody();
			Equation[] eqs = gson.fromJson(response, Equation[].class);

			for (Equation eq : eqs) {
				System.out.println(eq);
			}
		} catch (IOException ex) {
			System.out.println("Joe or the game don't exist");
		}
	}

	public void checkUserName() {
		System.out.println("\n-- Check User Name --");
		try {
			new Request(baseURI + "/players/wilma").getResource().getBody();
		} catch (IOException ex) {
			System.out.println("'wilma' is available for use.");
		}
	}

	public void newGame() {
		System.out.println("\n-- Create a new game --");
		try {
			Game game = new Game(null, 300, 60, new Date().getTime());  // game ID is server-assigned, so leave null here

			// equation ids should represent the order in which the equations were shown the player
			game.addEquation(new Equation(1, "1 + 1 = 4", Boolean.TRUE, Boolean.FALSE));
			game.addEquation(new Equation(2, "3 + 4 = 7", Boolean.TRUE, Boolean.TRUE));
			game.addEquation(new Equation(3, "4 + 2 = 6", Boolean.FALSE, Boolean.TRUE));

			String json = gson.toJson(game);

			Response resp = new Request(baseURI + "/players/joe/games")
				.addHeader("content-type", "application/json")
				.setBody(json)
				.postResource();

			System.out.println("Response: " + resp.getResponseCode());
		} catch (IOException ex) {
			System.out.println("Joe doesn't exist");
		}
	}

	public static void main(String[] args) throws Exception {
		TestClient c = new TestClient();
		c.getPlayers();
		c.addNewPlayer();
		Integer gameId = c.getGames();
		c.getEquations(gameId);
		c.checkUserName();
		c.newGame();
	}

}
