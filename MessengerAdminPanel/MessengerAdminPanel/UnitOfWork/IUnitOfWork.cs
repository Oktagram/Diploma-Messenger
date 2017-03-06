using System;

namespace MessengerAdminPanel.UnitOfWork
{
	public interface IUnitOfWork : IDisposable
	{
		IEntityBaseRepository<EventLog> EventLogRepositry { get; }
		void Save();
		void Dispose(bool disposing);
	}
}