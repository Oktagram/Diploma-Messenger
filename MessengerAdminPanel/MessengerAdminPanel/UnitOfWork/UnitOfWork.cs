using MessengerAdminPanel.Repositories;
using System;
using MessengerAdminPanel.Repositories.Interfaces;

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

		public IAnnouncementRepository AnnouncementRepository
		{
			get
			{
				if (_announcementRepository == null)
					_announcementRepository = new AnnouncementRepository(_context);
				return _announcementRepository;
			}
		}

		public IEntityBaseRepository<User> UserRepository
		{
			get
			{
				if (_userRepository == null)
					_userRepository = new EntityBaseRepository<User>(_context);
				return _userRepository;
			}
		}

		private readonly MessengerContext _context;
		private IEntityBaseRepository<EventLog> _eventLogRepository;
		private IAnnouncementRepository _announcementRepository;
		private IEntityBaseRepository<User> _userRepository;
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