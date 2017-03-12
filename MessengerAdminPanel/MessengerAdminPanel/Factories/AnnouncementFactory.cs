using System;

namespace MessengerAdminPanel.Factories
{
	public class AnnouncementFactory
	{
		public static Announcement Create(string description)
		{
			description = description.Trim();

			var item = new Announcement();

			item.CreationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
			item.IsActive = true;
			item.Description = description;
			item.UserId = CurrentUser.Id;

			return item;
		}
	}
}