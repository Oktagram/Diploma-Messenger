using System;

namespace MessengerAdminPanel.Exceptions
{
	public class IllegalCredentialsException : Exception
	{
		public IllegalCredentialsException() { }
		public IllegalCredentialsException(string message) : base(message) { }
		public IllegalCredentialsException(string message, Exception innerException) 
			: base(message, innerException) { }
	}
}
