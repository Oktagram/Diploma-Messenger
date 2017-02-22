using Messenger.Contexts;
using Messenger.LogProvider;
using Messenger.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;

namespace Messenger.Repositories
{
	public class EventLogRepository : IEventLogRepository
	{
		public LoggingEntity LoggingEntity { get; set; }

		private MessengerContext _context;

		public EventLogRepository(MessengerContext context)
        {
			_context = context;
		}

		public void Add(LoggingEvents eventId, string message)
		{
			var item = new EventLog
			{
				EventId = (int)eventId,
				EntityId = (int)LoggingEntity,
				Message = message,
				CreatedTime = DateTimeOffset.Now.ToUnixTimeMilliseconds()
			};
			
			_context.Set<EventLog>().Add(item);
			_context.SaveChanges();
		}

		public virtual IEnumerable<EventLog> FindBy(Expression<Func<EventLog, bool>> predicate)
		{
			return _context.Set<EventLog>().Where(predicate);
		}

		public IEnumerable<EventLog> GetAll()
		{
			return _context.Set<EventLog>().AsEnumerable();
		}
	}
}
