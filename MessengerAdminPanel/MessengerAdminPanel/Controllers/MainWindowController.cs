using MessengerAdminPanel.Mapping;
using MessengerAdminPanel.UnitOfWork;
using MessengerAdminPanel.Windows;
using System;
using System.Linq.Expressions;
using System.Windows.Threading;

namespace MessengerAdminPanel
{
	public class MainWindowController : IMainWindowController
	{
		private readonly IMainWindowView _view;
		private readonly IUnitOfWork _uof;
		private readonly DispatcherTimer _timer;
		private Expression<Func<EventLog, bool>> _query;

		public MainWindowController(IMainWindowView view, IUnitOfWork uof)
		{
			_view = view;
			_uof = uof;

			_timer = new DispatcherTimer();
			_timer.Tick += dispatcherTimer_Tick;
			_timer.Interval = new TimeSpan(0,0,0,0,500);
		}

		public void UpdateDataGridLog(Expression<Func<EventLog, bool>> predicate)
		{
			_timer.Stop();
			_query = predicate;
			_timer.Start();
		}

		private void dispatcherTimer_Tick(object sender, EventArgs e)
		{
			var eventLogList = _uof.EventLogRepositry.FindBy(_query);
			var mappedEventLogList = MapService.EventLogToViewModel(eventLogList);
			
			_view.UpdateDataGridLog(mappedEventLogList);
			_timer.Stop();
		}
	}
}