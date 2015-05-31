package com.lhickin.speedmath;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

//@XmlRootElement(name = "game")
public class Game implements Serializable {

	private Integer gameId;
	private Integer playerId;
	private Integer score;
	private Integer timeInSeconds;
	private Long date;

	private List<Equation> equations = new ArrayList<Equation>();

	public Game() {
	}

	public Game(Integer gameId, Integer score, Integer timeInSeconds, Long date) {
		this.gameId = gameId;
		this.score = score;
		this.timeInSeconds = timeInSeconds;
		this.date = date;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getTimeInSeconds() {
		return timeInSeconds;
	}

	public void setTimeInSeconds(Integer timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public List<Equation> getEquations() {
		return equations;
	}

	public void setEquations(List<Equation> equations) {
		this.equations = equations;
	}

	public void addEquation(Equation equation) {
		equations.add(equation);
	}


	@Override
	public String toString() {
		return "Game{" + "gameId=" + gameId + ", score=" + score + ", timeInSeconds=" + timeInSeconds + ", date=" + date + '}';
	}

}
