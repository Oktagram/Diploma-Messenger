namespace MessengerAdminPanel.Services
{
	public interface IFileService
	{
		void OpenFile(string filePath);
		string GetFileNameByPath(string filePath);
	}
}
