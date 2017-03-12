using MessengerAdminPanel.ViewModels;
using System.Collections.Generic;

namespace MessengerAdminPanel.Windows
{
	public interface IMainWindowView
	{
		void UpdateDataGridEventLog(IEnumerable<EventLogViewModel> eventLog);
		
		void UpdateListViewAnnouncement(List<AnnouncementViewModel> list);

		void UpdateConversationData(ConversationViewModel conversaionVM);
		void UpdateConversationListViewWithUsersList(List<UserViewModel> list);
		void UpdateConversationListViewWithMessagesList(List<MessageViewModel> list);
		
		void UpdateMessageData(MessageViewModel messageVM, string attachmentPath);

		void UpdateUserData(UserViewModel userVM, PersonalInfoViewModel infoVM, string profilePicturePath);
		void UpdateUserListViewWithConversationsList(List<ConversationViewModel> list);
		void UpdateUserListViewWithMessagesList(List<MessageViewModel> list);
		void UpdateUserListViewWithUsersList(List<UserViewModel> list);

		void ShowMessageBox(string message, string title = "Message");
		bool ShowMessageBoxYesNo(string message, string title);

		string ShowPromptWindow(string text, string defaultValue = "");
	}
}
