namespace MessengerAdminPanel.Services
{
	public interface IFileService
	{
		void OpenFile(string filePath);
		string GetPath(string description);
		string GetFileNameByPath(string filePath);
	}
}
