package com.elekscamp.messenger_javafx_client.Entities;

public class UserConversation {
	
	private int userId;
	private int conversationId;
	
	public UserConversation() { }
	
	public UserConversation(int userId, int conversationId) {
		this.userId = userId;
		this.conversationId = conversationId;
	}

	@Override
	public String toString() {
		return "UserConversation [userId=" + userId + ", conversationId=" + conversationId + "]";
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getConversationId() {
		return conversationId;
	}

	public void setConversationId(int conversationId) {
		this.conversationId = conversationId;
	}
}
