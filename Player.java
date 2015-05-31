package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "player")
public class Player {

	private String userName;
	private String name;

	public Player() {
	}

	public Player(String username, String name) {
		this.userName = username;
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Player{" + "userName=" + userName + ", name=" + name + '}';
	}

}
