using MessengerAdminPanel.Mapping;
using MessengerAdminPanel.Mapping.EventLogEnums;
using MessengerAdminPanel.Services;
using MessengerAdminPanel.UnitOfWork;
using MessengerAdminPanel.Windows;
using System;
using System.Linq;
using System.Linq.Expressions;
using System.Windows;
using System.Windows.Threading;

namespace MessengerAdminPanel
{
	public class MainWindowController : IMainWindowController
	{
		private readonly IMainWindowView _view;
		private readonly IUnitOfWork _uow;
		private readonly DispatcherTimer _timer;
		private Expression<Func<EventLog, bool>> _predicate;
		private int _eventLogEntity;
		private int _eventLogEvent;

		public MainWindowController(IMainWindowView view, IUnitOfWork uof)
		{
			_view = view;
			_uow = uof;

			_timer = new DispatcherTimer();
			_timer.Tick += dispatcherTimer_Tick;
			_timer.Interval = new TimeSpan(0,0,0,0,500);
		}

		public void UpdateDataGridLog(Expression<Func<EventLog, bool>> predicate, int eventLogEntity, int eventLogEvent)
		{
			_timer.Stop();
			_timer.Start();

			_predicate = predicate;
			_eventLogEntity = eventLogEntity;
			_eventLogEvent = eventLogEvent;
		}

		private void dispatcherTimer_Tick(object sender, EventArgs e)
		{
			var eventLogList = _uow.EventLogRepositry.FindBy(_predicate);

			if (Enum.IsDefined(typeof(EventLogEntity), _eventLogEntity))
				eventLogList = eventLogList.Where(log => log.EntityId == _eventLogEntity);
			if (Enum.IsDefined(typeof(EventLogEvent), _eventLogEvent))
				eventLogList = eventLogList.Where(log => log.EventId == _eventLogEvent);

			var mappedEventLogList = MapService.EventLogToViewModel(eventLogList);
			_view.UpdateDataGridLog(mappedEventLogList);
			_timer.Stop();
		}

		public void UpdateAnnouncementsListView(bool isActive)
		{
			var list = _uow.AnnouncementRepository.FindBy(a => a.IsActive == isActive);
			var mappedAnnouncements = MapService.AnnouncementToViewModel(list, _uow).ToList();
			_view.UpdateListViewAnnouncement(mappedAnnouncements);
		}

		public void CreateNewAnnouncement()
		{
			var prompt = new PromptWindow("Enter announcement description: ");

			if (prompt.ShowDialog().Value && !String.IsNullOrEmpty(prompt.ResponseText))
			{
				var item = new Announcement();
				
				item.CreationDate = DateService.GetCurrentUnixTimestampMillis();
				item.IsActive = true;
				item.Description = prompt.ResponseText;
				item.Description.Trim();
				item.UserId = 3;

				_uow.AnnouncementRepository.Add(item);
				_uow.Save();
			}
		}
	}
}