using MessengerAdminPanel.ViewModels;
using System.Collections.Generic;

namespace MessengerAdminPanel.Windows
{
	public interface IMainWindowView
	{
		void UpdateDataGridLog(IEnumerable<EventLogViewModel> eventLog);
	}
}
