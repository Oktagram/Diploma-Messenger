using System;

namespace MessengerAdminPanel.ViewModels
{
	public class UserViewModel
	{
		public int Id { get; set; }
		public string Email { get; set; }
		public bool IsAdmin { get; set; }
		public bool IsBanned { get; set; }
		public bool IsOnline { get; set; }
		public string Login { get; set; }
		public DateTime RegistrationDate { get; set; }
	}
}
