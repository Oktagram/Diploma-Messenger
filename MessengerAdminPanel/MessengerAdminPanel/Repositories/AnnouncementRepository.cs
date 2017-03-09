using MessengerAdminPanel.Repositories.Interfaces;
using MessengerAdminPanel.Services;

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
				announcement.ClosingDate = DateService.GetCurrentUnixTimestampMillis();
			}

			if (!announcement.IsActive && newAnnouncement.IsActive)
			{
				announcement.ClosingDate = 0;
				announcement.CreationDate = DateService.GetCurrentUnixTimestampMillis();

				if (newAnnouncement.UserId != 0) announcement.UserId = newAnnouncement.UserId;
			}

			announcement.Description = newAnnouncement.Description;
			announcement.IsActive = newAnnouncement.IsActive;
		}
	}
}
