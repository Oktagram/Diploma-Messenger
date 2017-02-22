using Messenger.Contexts;
using Messenger.Database;
using Microsoft.Extensions.Logging;
using System;

namespace Messenger.LogProvider
{
	public class DbLoggerProvider : ILoggerProvider
    {
		private readonly Func<string, LogLevel, bool> _filter;
		private readonly MessengerContext _context;

		public DbLoggerProvider(MessengerContext context, Func<string, LogLevel, bool> filter)
		{
			_context = context;
			_filter = filter;
		}
		public ILogger CreateLogger(string categoryName)
		{
			return new DbLogger(_context, categoryName, _filter);
		}

		public void Dispose()
		{

		}
	}
}
