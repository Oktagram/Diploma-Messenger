package com.elekscamp.messenger_javafx_client.Entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Announcement {
	
	private int id;
	private long closingDate;
	private long creationDate;
	private String description;
	private boolean isActive;
	private int userId;
	
	public Announcement() { }
	
	public Announcement(String description, int userId) {
		this.description = description;
		this.userId = userId;
	}
	
	@Override public String toString() {
		return "Announcement [id=" + id + ", closingDate=" + closingDate + ", creationDate=" + creationDate
				+ ", description=" + description + ", isActive=" + isActive + ", userId=" + userId + "]";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(long closingDate) {
		this.closingDate = closingDate;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}