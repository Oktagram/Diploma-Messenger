using System;

namespace MessengerAdminPanel.ViewModels
{
	public class ConversationViewModel
	{
		public int Id { get; set; }
		public string Name { get; set; }
		public DateTime CreationDate { get; set; }
		public string CounfOfMessages { get; set; }
		public string CountOfUsers { get; set; }
	}
}
