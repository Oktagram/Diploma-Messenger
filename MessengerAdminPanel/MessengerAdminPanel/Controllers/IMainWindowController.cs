using MessengerAdminPanel.ViewModels;
using System;
using System.Linq.Expressions;

namespace MessengerAdminPanel
{
	public interface IMainWindowController
	{
		void UpdateDataGridEventLog(Expression<Func<EventLog, bool>> predicate, int eventLogEntity, int eventLogEvent);

		void UpdateAnnouncementsListView(bool isActive);
		void CreateNewAnnouncement();
		void EditAnnouncement(AnnouncementViewModel a, bool activity);
		void DeleteAnnouncement(AnnouncementViewModel a, bool activity);
		void ChangeAnnouncementStatus(AnnouncementViewModel a, bool activity);

		void UpdateConversationData(string conversationId);
		void ChangeConversationName(string conversaitonId, string currentName);
		void DeleteConversation(string conversationId);
		void UpdateListViewUsersInConversation(string conversationId);
		void UpdateListViewMessagesInConversation(string conversationId);
	
		void UpdateMessageData(string messageId);
		void DeleteMessage(string messageId);
		
		void UpdateUserDataById(string userId);
		void UpdateUserDataByUsername(string username);
		void UpdateUserListView(string userId, string username, int userData);

		void ChangeUsername(string currentUsername);
		void ChangeUserBanStatus(string username, bool status);
		void ChangeUserAdminStatus(string username, bool status);

		void OpenFile(string fileName);
	}
}