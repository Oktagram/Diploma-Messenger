using System.Collections;

namespace MessengerAdminPanel.Services
{
	public interface IReportsSaver
	{
		bool Save(string path, string name, IEnumerable data);
	}
}