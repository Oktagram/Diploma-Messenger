using MessengerAdminPanel.Repositories;
using System;
using MessengerAdminPanel.Repositories.Interfaces;

namespace MessengerAdminPanel.UnitOfWorks
{
	public class UnitOfWork : IUnitOfWork
	{
		private IEntityBaseRepository<EventLog> _eventLogRepository;
		public IEntityBaseRepository<EventLog> EventLogRepositry
		{
			get
			{
				if (_eventLogRepository == null)
					_eventLogRepository = new EntityBaseRepository<EventLog>(_context);
				return _eventLogRepository;
			}
		}

		private IAnnouncementRepository _announcementRepository;
		public IAnnouncementRepository AnnouncementRepository
		{
			get
			{
				if (_announcementRepository == null)
					_announcementRepository = new AnnouncementRepository(_context);
				return _announcementRepository;
			}
		}

		private IUserRepository _userRepository;
		public IUserRepository UserRepository
		{
			get
			{
				if (_userRepository == null)
					_userRepository = new UserRepository(_context);
				return _userRepository;
			}
		}

		private IConversationRepository _conversationRepository;
		public IConversationRepository ConversationRepository
		{
			get
			{
				if (_conversationRepository == null)
					_conversationRepository = new ConversationRepository(_context);
				return _conversationRepository;
			}
		}

		private IMessageRepository _messageRepository;
		public IMessageRepository MessageRepository
		{
			get
			{
				if (_messageRepository == null)
					_messageRepository = new MessageRepository(_context);
				return _messageRepository;
			}
		}

		private IPersonalInfoRepository _personalInfoRepository;
		public IPersonalInfoRepository PersonalInfoRepository
		{
			get
			{
				if (_personalInfoRepository == null)
					_personalInfoRepository = new PersonalInfoRepository(_context);
				return _personalInfoRepository;
			}
		}

		private readonly MessengerContext _context;
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