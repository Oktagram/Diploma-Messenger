package com.elekscamp.messenger_javafx_client.dal;

import com.elekscamp.messenger_javafx_client.entities.Announcement;
import com.elekscamp.messenger_javafx_client.entities.Conversation;
import com.elekscamp.messenger_javafx_client.entities.Message;
import com.elekscamp.messenger_javafx_client.entities.PersonalInfo;
import com.elekscamp.messenger_javafx_client.entities.User;
import com.elekscamp.messenger_javafx_client.entities.UserConversation;

public class ContentProvider {

	private UserProvider userProvider;
	private MessageProvider messageProvider;
	private PersonalInfoProvider personalInfoProvider;
	private ConversationProvider conversationProvider;
	private UserConversationProvider userConversationProvider;
	private FileProvider fileProvider;
	private AnnouncementProvider announcementProvider;
	
	public ContentProvider() {
		userProvider = new UserProvider(new RequestManager<User>(User.class));
		messageProvider = new MessageProvider(new RequestManager<Message>(Message.class));
		personalInfoProvider = new PersonalInfoProvider(new RequestManager<PersonalInfo>(PersonalInfo.class));
		conversationProvider = new ConversationProvider(new RequestManager<Conversation>(Conversation.class));
		userConversationProvider = new UserConversationProvider(new RequestManager<UserConversation>(UserConversation.class));
		fileProvider = new FileProvider();
		announcementProvider = new AnnouncementProvider(new RequestManager<Announcement>(Announcement.class));
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
	
	public AnnouncementProvider getAnnouncementProvider() {
		return announcementProvider;
	}
}