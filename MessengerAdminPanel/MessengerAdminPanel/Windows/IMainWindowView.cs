using MessengerAdminPanel.ViewModels;
using System.Collections.Generic;

namespace MessengerAdminPanel.Windows
{
	public interface IMainWindowView
	{
		void UpdateDataGridLog(IEnumerable<EventLogViewModel> eventLog);
		void UpdateListViewAnnouncement(List<AnnouncementViewModel> list);
		void UpdateConversationData(string name, string creationDate, string countOfMessages, string countOfUsers);
		void UpdateConversationListViewWithUsersList(List<UserViewModel> list);
		void UpdateConversationListViewWithMessagesList(List<MessageViewModel> list);
	}
}
