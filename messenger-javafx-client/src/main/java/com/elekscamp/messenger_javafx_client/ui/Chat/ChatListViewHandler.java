package com.elekscamp.messenger_javafx_client.ui.Chat;

import com.elekscamp.messenger_javafx_client.GlobalVariables;
import com.elekscamp.messenger_javafx_client.dal.ContentProvider;
import com.elekscamp.messenger_javafx_client.entities.Announcement;
import com.elekscamp.messenger_javafx_client.entities.Conversation;
import com.elekscamp.messenger_javafx_client.entities.Message;
import com.elekscamp.messenger_javafx_client.entities.User;
import com.elekscamp.messenger_javafx_client.ui.Chat.ListCells.ActiveAnnouncementListCell;
import com.elekscamp.messenger_javafx_client.ui.Chat.ListCells.ClosedAnnouncementsListCell;
import com.elekscamp.messenger_javafx_client.ui.Chat.ListCells.ConversationListCell;
import com.elekscamp.messenger_javafx_client.ui.Chat.ListCells.MessageListCell;
import com.elekscamp.messenger_javafx_client.ui.Chat.ListCells.UserListCell;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ChatListViewHandler {
	
	public void customizeListViewClosedAnnouncements(ListView<Announcement> listViewClosedAnnouncements, ContentProvider provider, int currentUserId,
			ObservableList<Announcement> activeAnnouncementsObservableList, ObservableList<Announcement> closedAnnouncementsObservableList) {
		
		listViewClosedAnnouncements.setCellFactory(new Callback<ListView<Announcement>, ListCell<Announcement>>() {
			
			@Override 
			public ListCell<Announcement> call(ListView<Announcement> param) {
				
				ClosedAnnouncementsListCell listCell = new ClosedAnnouncementsListCell(provider);
				
				listCell.prefWidthProperty().bind(listViewClosedAnnouncements.widthProperty().subtract(50));
				listCell.initData(currentUserId, activeAnnouncementsObservableList, closedAnnouncementsObservableList);
				
				return listCell;
			}
		});
	}
	
	public void customizeListViewActiveAnnouncements(ListView<Announcement> listViewActiveAnnouncements, ContentProvider provider,
			ObservableList<Announcement> activeAnnouncementsObservableList, ObservableList<Announcement> closedAnnouncementsObservableList) {
		
		listViewActiveAnnouncements.setCellFactory(new Callback<ListView<Announcement>, ListCell<Announcement>>() {
			
			@Override 
			public ListCell<Announcement> call(ListView<Announcement> param) {

				ActiveAnnouncementListCell listCell = new ActiveAnnouncementListCell(provider);

				listCell.prefWidthProperty().bind(listViewActiveAnnouncements.widthProperty().subtract(50));
				listCell.initData(activeAnnouncementsObservableList, closedAnnouncementsObservableList);
				
				return listCell;
			}
		});
	}
	
	public void customizeListViewConversations(ListView<Conversation> listViewConversations, ContentProvider provider, 
			int currentUserId, ObservableList<Conversation> conversationsObservableList) {
		
		listViewConversations.setCache(true);
		listViewConversations.setCellFactory(new Callback<ListView<Conversation>, ListCell<Conversation>>() {
			@Override 
			public ListCell<Conversation> call(ListView<Conversation> param) {

				ConversationListCell listCell = new ConversationListCell(provider);
				listCell.getStylesheets().add(getClass().getResource("/css/list-cell.css").toExternalForm());
				listCell.prefWidthProperty().bind(listViewConversations.widthProperty().subtract(50));
				listCell.initData(currentUserId, conversationsObservableList);
			
				return listCell;
			}
		});
	}
	
	public void customizeListViewUsers(ListView<User> listViewUsers, ContentProvider provider) {
		listViewUsers.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
			@Override 
			public ListCell<User> call(ListView<User> param) {

				UserListCell listCell = new UserListCell(provider);
				listCell.getStylesheets().add(getClass().getResource("/css/list-cell.css").toExternalForm());
				listCell.prefWidthProperty().bind(listViewUsers.widthProperty().subtract(50));

				return listCell;
			}
		});
	}
	
	public void customizeChatListView(ListView<Message> listViewChat, int currentUserId) {
		listViewChat.setPlaceholder(new Text(GlobalVariables.languageDictionary.getChooseConversaion()));
		listViewChat.setCache(true);
		listViewChat.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
			@Override public ListCell<Message> call(ListView<Message> param) {

				MessageListCell listCell = new MessageListCell();
				listCell.getStylesheets().add(getClass().getResource("/css/list-cell.css").toExternalForm());
				listCell.prefWidthProperty().bind(listViewChat.widthProperty().subtract(50));
				listCell.setCurrentUserId(currentUserId);

				return listCell;
			}
		});
	}
}
