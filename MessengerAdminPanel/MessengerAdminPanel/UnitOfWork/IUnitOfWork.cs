using MessengerAdminPanel.Repositories.Interfaces;
using System;

namespace MessengerAdminPanel.UnitOfWork
{
	public interface IUnitOfWork : IDisposable
	{
		IEntityBaseRepository<EventLog> EventLogRepositry { get; }
		IAnnouncementRepository AnnouncementRepository { get; }
		IEntityBaseRepository<User> UserRepository { get; }
		void Save();
		void Dispose(bool disposing);
	}
}