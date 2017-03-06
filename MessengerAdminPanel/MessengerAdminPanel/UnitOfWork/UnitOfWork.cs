using MessengerAdminPanel.Repositories;
using System;

namespace MessengerAdminPanel.UnitOfWork
{
	public class UnitOfWork : IUnitOfWork
	{
		public IEntityBaseRepository<EventLog> EventLogRepositry
		{
			get
			{
				if (_eventLogRepository == null)
					_eventLogRepository = new EntityBaseRepository<EventLog>(_context);
				return _eventLogRepository;
			}
		}

		private readonly MessengerContext _context;
		private IEntityBaseRepository<EventLog> _eventLogRepository;
		private bool _disposed;

		public UnitOfWork(MessengerContext context)
		{
			_context = context;
			_disposed = false;
		}

		public void Dispose()
		{
			Dispose(true);
			GC.SuppressFinalize(this);
		}

		public virtual void Dispose(bool disposing)
		{
			if (!_disposed)
			{
				if (disposing) _context.Dispose();
			
				_disposed = true;
			}
		}

		public void Save()
		{
			_context.SaveChanges();
		}
	}
}