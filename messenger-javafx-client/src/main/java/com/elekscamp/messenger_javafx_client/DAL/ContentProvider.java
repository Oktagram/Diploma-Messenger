package com.elekscamp.messenger_javafx_client.DAL;

import com.elekscamp.messenger_javafx_client.Entities.Conversation;
import com.elekscamp.messenger_javafx_client.Entities.Message;
import com.elekscamp.messenger_javafx_client.Entities.PersonalInfo;
import com.elekscamp.messenger_javafx_client.Entities.User;
import com.elekscamp.messenger_javafx_client.Entities.UserConversation;

public class ContentProvider {
	
	private UserProvider userProvider;
	private MessageProvider messageProvider;
	private PersonalInfoProvider personalInfoProvider;
	private ConversationProvider conversationProvider;
	private UserConversationProvider userConversationProvider;
	private FileProvider fileProvider;

	public ContentProvider() {
		userProvider = new UserProvider(new RequestManager<User>(User.class));
		messageProvider = new MessageProvider(new RequestManager<Message>(Message.class));
		personalInfoProvider = new PersonalInfoProvider(new RequestManager<PersonalInfo>(PersonalInfo.class));
		conversationProvider = new ConversationProvider(new RequestManager<Conversation>(Conversation.class));
		userConversationProvider = new UserConversationProvider(new RequestManager<UserConversation>(UserConversation.class));
		fileProvider = new FileProvider();
	}
	
	public UserProvider getUserProvider() {
		return userProvider;
	}
	public MessageProvider getMessageProvider() {
		return messageProvider;
	}
	public PersonalInfoProvider getPersonalInfoProvider() {
		return personalInfoProvider;
	}
	public ConversationProvider getConversationProvider() {
		return conversationProvider;
	}
	public UserConversationProvider getUserConversationProvider() {
		return userConversationProvider;
	}
	public FileProvider getFileProvider() {
		return fileProvider;
	}
	/*
	public void setUserConversationProvider(UserConversationProvider userConversationProvider) {
		this.userConversationProvider = userConversationProvider;
	}
	*/
}
