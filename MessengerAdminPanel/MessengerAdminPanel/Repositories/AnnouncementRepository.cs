using MessengerAdminPanel.Repositories.Interfaces;
using System;

namespace MessengerAdminPanel.Repositories
{
	public class AnnouncementRepository : EntityBaseRepository<Announcement>, IAnnouncementRepository
	{
		public AnnouncementRepository(MessengerContext context) 
			: base(context)
		{ }

		public void Update(int id, Announcement announcement, Announcement newAnnouncement)
		{
			_context.Refresh();

			if (announcement.IsActive && !newAnnouncement.IsActive)
			{
				announcement.ClosingDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
			}

			if (!announcement.IsActive && newAnnouncement.IsActive)
			{
				announcement.ClosingDate = 0;
				announcement.CreationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();

				if (newAnnouncement.UserId != 0)
				{
	//				var user = _context.User.Find(newAnnouncement.UserId);

					announcement.UserId = newAnnouncement.UserId;
	//				announcement.User = user;
				}
			}

			announcement.Description = newAnnouncement.Description;
			announcement.IsActive = newAnnouncement.IsActive;
		}
	}
}
