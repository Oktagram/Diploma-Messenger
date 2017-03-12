namespace MessengerAdminPanel.Services.Interfaces
{
	public interface IAuthenticator
	{
		void CheckCredentials(string username, string password);
	}
}
