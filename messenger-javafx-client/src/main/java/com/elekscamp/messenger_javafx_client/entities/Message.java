package com.elekscamp.messenger_javafx_client.entities;

import java.sql.Timestamp;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

	private int id;
	private int userId;
	private int conversationId;
	private String text;
	private String attachment;
	private long sendDate;

	public Message() { }

	public Message(int userId, int conversationId, String text) {

		this.userId = userId;
		this.conversationId = conversationId;
		this.text = text;
		sendDate = new Date().getTime();
	}

	@Override public String toString() {
		return "Message [id=" + id + ", userId=" + userId + ", conversationId=" + conversationId + ", text=" + text
				+ ", attachment=" + attachment + ", sendDate=" + new Timestamp(sendDate) + "]";
	}

	public boolean equals(Object obj) { 
	    if (obj instanceof Message) {
	    	Message message = (Message)obj;
	    	
	    	if (((attachment == null && message.attachment != null) || 
	    			(attachment != null && message.attachment == null)) &&
	    			(attachment != null && !attachment.equals(message.attachment)) ||
	    			((text == null && message.text != null) ||
	    			(text != null && message.text == null)) &&
	    			(text != null && !text.equals(message.text))) {
	    		return false;
	    	}
	    	
	        return id == message.id &&
	        		userId == message.userId &&
	        		conversationId == message.conversationId &&
	        		sendDate == message.sendDate;
	    }
	    return false; 
	} 
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public long getSendDate() {
		return sendDate;
	}

	public void setSendDate(long sendDate) {
		this.sendDate = sendDate;
	}
}
