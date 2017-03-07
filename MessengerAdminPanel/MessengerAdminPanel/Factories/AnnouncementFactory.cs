using MessengerAdminPanel.Services;

namespace MessengerAdminPanel.Factories
{
	public class AnnouncementFactory
	{
		public static Announcement Create(string description)
		{
			var item = new Announcement();

			item.CreationDate = DateService.GetCurrentUnixTimestampMillis();
			item.IsActive = true;
			item.Description = description;
			item.Description = item.Description.Trim();
			item.UserId = 3; // Current user

			return item;
		}
	}
}