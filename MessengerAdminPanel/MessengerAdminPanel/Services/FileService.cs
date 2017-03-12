using System.Diagnostics;
using System.Windows.Forms;

namespace MessengerAdminPanel.Services
{
	public class FileService : IFileService
	{
		private const int LENGTH_OF_FILE_PREFIX = 36;

		public string GetFileNameByPath(string filePath)
		{
			var attachmentNameIndex = filePath.LastIndexOf('\\') + 1;
			return filePath.Substring(attachmentNameIndex + LENGTH_OF_FILE_PREFIX);
		}

		public string GetPath(string description)
		{
			var dialog = new FolderBrowserDialog();

			dialog.Description = description;
			dialog.ShowDialog();

			return dialog.SelectedPath;
		}

		public void OpenFile(string filePath)
		{
			Process.Start(filePath);
		}
	}
}