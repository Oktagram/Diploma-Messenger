using MessengerAdminPanel.ViewModels;
using System.Collections.Generic;

namespace MessengerAdminPanel.Mapping
{
	public interface IMappingService
	{
		MessageViewModel MessageToViewModel(Message message);
		IEnumerable<MessageViewModel> MessageToViewModel(IEnumerable<Message> messages);
		IEnumerable<UserViewModel> UserToViewModel(IEnumerable<User> users);
		IEnumerable<AnnouncementViewModel> AnnouncementToViewModel(IEnumerable<Announcement> announcements);
		IEnumerable<EventLogViewModel> EventLogToViewModel(IEnumerable<EventLog> eventLogs);
	}
}
