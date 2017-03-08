using MessengerAdminPanel.ViewModels;
using System;
using System.Linq.Expressions;

namespace MessengerAdminPanel
{
	public interface IMainWindowController
	{
		void UpdateDataGridLog(Expression<Func<EventLog, bool>> predicate, int eventLogEntity, int eventLogEvent);
		void UpdateAnnouncementsListView(bool isActive);
		void CreateNewAnnouncement();
		void EditAnnouncement(AnnouncementViewModel a, bool activity);
		void DeleteAnnouncement(AnnouncementViewModel a, bool activity);
		void ChangeAnnouncementStatus(AnnouncementViewModel a, bool activity);
		void UpdateConversationData(string conversationId);
		void UpdateListViewUsersInConversation(string conversationId);
		void UpdateListViewMessagesInConversation(string conversationId);
	}
}