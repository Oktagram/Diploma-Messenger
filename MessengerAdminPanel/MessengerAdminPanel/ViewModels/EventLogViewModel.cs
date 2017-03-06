using System;

namespace MessengerAdminPanel.ViewModels
{
	public class EventLogViewModel
	{
		public int Id { get; set; }
		public DateTime CreatedTime { get; set; }
		public string Entity { get; set; }
		public string Event { get; set; }
		public string Message { get; set; }
	}
}
