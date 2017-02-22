using Messenger.Contexts;
using Microsoft.Extensions.Logging;
using System;

namespace Messenger.LogProvider
{
	public static class DbLoggerExtensions
    {
		public static ILoggerFactory AddContext(this ILoggerFactory factory, MessengerContext context, Func<string, LogLevel, bool> filter = null)
		{
			factory.AddProvider(new DbLoggerProvider(context, filter));
			return factory;
		}

		public static ILoggerFactory AddContext(this ILoggerFactory factory, MessengerContext context, LogLevel minLevel)
		{
			return AddContext(
				factory, context,
				(_, logLevel) => logLevel >= minLevel);
		}
	}
}
