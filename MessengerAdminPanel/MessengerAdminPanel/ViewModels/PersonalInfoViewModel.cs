using System;

namespace MessengerAdminPanel.ViewModels
{
	public class PersonalInfoViewModel
	{
		public int Id { get; set; }
		public DateTime BirthDate { get; set; }
		public string FirstName { get; set; }
		public string LastName { get; set; }
		public string PhoneNumber { get; set; }
		public string Picture { get; set; }
		public string User { get; set; }
	}
}
