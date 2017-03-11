using MessengerAdminPanel.ViewModels;
using System.Collections.Generic;

namespace MessengerAdminPanel
{
	public class Columns
	{
		public Dictionary<string, string> UserViewModelColumns
		{
			get
			{
				return new Dictionary<string, string>()
				{
					{ "Id", nameof(UserViewModel.Id) },
					{ "Username", nameof(UserViewModel.Login) },
					{ "Online", nameof(UserViewModel.IsOnline) },
					{ "Admin", nameof(UserViewModel.IsAdmin) },
					{ "Banned", nameof(UserViewModel.IsBanned) },
					{ "Registration Date", nameof(UserViewModel.RegistrationDate) },
					{ "Email", nameof(UserViewModel.Email) }
				};
			}
		}

		public Dictionary<string, string> MessageViewModelColumns
		{
			get
			{
				return new Dictionary<string, string>()
				{
					{ "Id", nameof(MessageViewModel.Id) },
					{ "Text", nameof(MessageViewModel.Text) },
					{ "User", nameof(MessageViewModel.User) },
					{ "Send Date", nameof(MessageViewModel.SendDate) },
					{ "Conversation", nameof(MessageViewModel.Conversation) },
					{ "Attachment", nameof(MessageViewModel.Attachment) }
				};
			}
		}
		
		public Dictionary<string, string> ConversationViewModelColumns
		{
			get
			{
				return new Dictionary<string, string>()
				{
					{ "Id", nameof(ConversationViewModel.Id) },
					{ "Name", nameof(ConversationViewModel.Name) },
					{ "Created", nameof(ConversationViewModel.CreationDate) },
					{ "Count of messages", nameof(ConversationViewModel.CounfOfMessages) },
					{ "Count of users", nameof(ConversationViewModel.CountOfUsers) }
				};
			}
		}

		public string AnnouncementClosedColumnHeader
		{
			get
			{
				return "Closed";
			}
		}
	}
}
