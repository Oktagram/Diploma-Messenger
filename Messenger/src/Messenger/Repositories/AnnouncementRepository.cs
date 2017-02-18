using Messenger.Contexts;
using Messenger.Models;
using System;

namespace Messenger.Repositories
{
	public class AnnouncementRepository : EntityBaseRepository<Announcement>, IAnnouncementRepository
	{
		public AnnouncementRepository(MessengerContext context)
			: base(context)
		{ }

		public void Update(int id, Announcement announcement, Announcement newAnnouncement)
		{
			if (announcement.IsActive && !newAnnouncement.IsActive)
			{
				announcement.ClosingDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
			}

			if (!announcement.IsActive && newAnnouncement.IsActive)
			{
				announcement.ClosingDate = 0;
				announcement.CreationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();

				if (newAnnouncement.UserId != 0) announcement.UserId = newAnnouncement.UserId;
			}
			
			announcement.Description = newAnnouncement.Description;
			announcement.IsActive = newAnnouncement.IsActive;

			Commit();
		}
	}
}