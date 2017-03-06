namespace MessengerAdminPanel.Repositories.Interfaces
{
	public interface IAnnouncementRepository : IEntityBaseRepository<Announcement>
	{
		void Update(int id, Announcement convObj, Announcement item);
	}
}
