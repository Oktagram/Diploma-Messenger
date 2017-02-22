package com.elekscamp.messenger_javafx_client.entities;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversation {

	private int id;
	private String name;
	private long creationDate;

	public Conversation() {
	}

	public Conversation(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Conversation [id=" + id + ", name=" + name + ", creationDate=" + new Timestamp(creationDate) + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
}
