using System;

namespace MessengerAdminPanel.Services
{
	public class DateService
	{
		private static readonly DateTime _unixEpoch = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Local);

		public static long GetCurrentUnixTimestampMillis()
		{
			return (long)(DateTime.Now - _unixEpoch).TotalMilliseconds;
		}

		public static DateTime DateTimeFromUnixTimestampMillis(long millis)
		{
			return _unixEpoch.AddMilliseconds(millis);
		}
	}
}
