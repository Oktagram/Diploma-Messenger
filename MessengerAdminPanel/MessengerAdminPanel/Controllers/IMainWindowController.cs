using System;
using System.Linq.Expressions;

namespace MessengerAdminPanel
{
	public interface IMainWindowController
	{
		void UpdateDataGridLog(Expression<Func<EventLog, bool>> predicate, int eventLogEntity, int eventLogEvent);
		void UpdateAnnouncementsListView(bool isActive);
		void CreateNewAnnouncement();
	}
}