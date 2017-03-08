using System;

namespace MessengerAdminPanel.ViewModels
{
	public class MessageViewModel
	{
		public int Id { get; set; }
		public string Attachment { get; set; }
		public string Conversation { get; set; }
		public DateTime SendDate { get; set; }
		public string Text { get; set; }
		public string User { get; set; }
	}
}
