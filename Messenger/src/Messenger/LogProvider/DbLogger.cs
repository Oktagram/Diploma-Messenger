using Messenger.Contexts;
using Messenger.Models;
using Microsoft.Extensions.Logging;
using System;

namespace Messenger.Database
{
	public class DbLogger : ILogger
    {
		private string _categoryName;
		private Func<string, LogLevel, bool> _filter;
		private MessengerContext _context;
		private bool _selfException = false;

		public DbLogger(MessengerContext context, string categoryName, Func<string, LogLevel, bool> filter)
		{
			_categoryName = categoryName;
			_filter = filter;
			_context = context;
		}

		public void Log<TState>(LogLevel logLevel, EventId eventId, TState state, Exception exception, Func<TState, Exception, string> formatter)
		{
			if (!IsEnabled(logLevel)) return;

			if (_selfException)
			{
				_selfException = false;
				return;
			}

			_selfException = true;

			if (formatter == null) throw new ArgumentNullException(nameof(formatter));
	
			var message = formatter(state, exception);
			
			if (string.IsNullOrEmpty(message)) return;

			if (exception != null) message += "\n" + exception.ToString();
			
			try
			{
				message = message.Length > MessengerContext.MessageMaxLength ?
					message.Substring(0, MessengerContext.MessageMaxLength) : message;

				var eventLog = new EventLog
				{
					Message = message,
					EventId = eventId.Id,
					CreatedTime = DateTimeOffset.Now.ToUnixTimeMilliseconds()
				};

				_context.Set<EventLog>().Add(eventLog);
				_context.SaveChanges();
				_selfException = false;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.Message);
			}
		}

		public bool IsEnabled(LogLevel logLevel)
		{
			return (_filter == null || _filter(_categoryName, logLevel));
		}

		public IDisposable BeginScope<TState>(TState state)
		{
			return null;
		}
	}
}
