using System;

namespace MessengerAdminPanel.ViewModels
{
	public class AnnouncementViewModel
	{
		public int Id { get; set; }
		public DateTime ClosingDate { get; set; }
		public DateTime CreationDate { get; set; }
		public string Description { get; set; }
		public bool IsActive { get; set; }
		public string User { get; set; }
	}
}
