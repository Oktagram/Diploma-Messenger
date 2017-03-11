using MessengerAdminPanel.ViewModels;
using System.Collections.Generic;

namespace MessengerAdminPanel.Windows
{
	public interface IMainWindowView
	{
		void UpdateDataGridLog(IEnumerable<EventLogViewModel> eventLog);
		void UpdateListViewAnnouncement(List<AnnouncementViewModel> list);
		void UpdateConversationData(ConversationViewModel conversaionVM);
		void UpdateConversationListViewWithUsersList(List<UserViewModel> list);
		void UpdateConversationListViewWithMessagesList(List<MessageViewModel> list);
		void UpdateMessageData(MessageViewModel messageVM, string attachmentPath);
		void UpdateUserData(UserViewModel userVM, PersonalInfoViewModel infoVM, string profilePicturePath);
	}
}
