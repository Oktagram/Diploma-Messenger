using MessengerAdminPanel.Factories;
using MessengerAdminPanel.Mapping;
using MessengerAdminPanel.Mapping.EventLogEnums;
using MessengerAdminPanel.Services;
using MessengerAdminPanel.UnitOfWork;
using MessengerAdminPanel.ViewModels;
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
			_timer.Interval = new TimeSpan(0, 0, 0, 0, 500);
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
			var announcementsList = _uow.AnnouncementRepository.FindBy(a => a.IsActive == isActive);
			var mappedAnnouncements = MapService.AnnouncementToViewModel(announcementsList, _uow).ToList();
			_view.UpdateListViewAnnouncement(mappedAnnouncements);
		}

		public void CreateNewAnnouncement()
		{
			var prompt = new PromptWindow("Enter announcement description: ");

			if (!prompt.ShowDialog().Value) return;

			if (String.IsNullOrEmpty(prompt.ResponseText))
			{
				MessageBox.Show("Description cannot be empty!");
				return;
			}

			var announcement = AnnouncementFactory.Create(prompt.ResponseText);
			
			_uow.AnnouncementRepository.Add(announcement);
			_uow.Save();
		}

		public void EditAnnouncement(AnnouncementViewModel a, bool activity)
		{
			if (CheckForNullWithErrorMessage(a, "Choose announcement!")) return;

			var prompt = new PromptWindow("Edit announcement description: ");
			prompt.ResponseText = a.Description;

			if (!prompt.ShowDialog().Value) return;

			if (String.IsNullOrEmpty(prompt.ResponseText))
			{
				MessageBox.Show("Description cannot be empty!");
				return;
			}

			var newAnnouncement = AnnouncementFactory.Create(prompt.ResponseText);
			var announcement = _uow.AnnouncementRepository.Find(a.Id);

			_uow.AnnouncementRepository.Update(a.Id, announcement, newAnnouncement);
			_uow.Save();

			UpdateAnnouncementsListView(activity);
		}

		public void DeleteAnnouncement(AnnouncementViewModel a, bool activity)
		{
			if (CheckForNullWithErrorMessage(a, "Choose announcement!")) return;

			var dialogResult = MessageBox.Show("Are you sure?", "Delete announcement", MessageBoxButton.YesNo);
			if (dialogResult != MessageBoxResult.Yes) return;

			_uow.AnnouncementRepository.Remove(a.Id);
			_uow.Save();

			UpdateAnnouncementsListView(activity);
		}

		public void ChangeAnnouncementStatus(AnnouncementViewModel a, bool activity)
		{
			if (CheckForNullWithErrorMessage(a, "Choose announcement!")) return;

			a.IsActive = !a.IsActive;
			
			var announcement = _uow.AnnouncementRepository.Find(a.Id);
			var newAnnouncement = (Announcement)announcement.Clone();
			newAnnouncement.IsActive = !newAnnouncement.IsActive;

			_uow.AnnouncementRepository.Update(a.Id, announcement, newAnnouncement);
			_uow.Save();

			UpdateAnnouncementsListView(activity);
		}

		private bool CheckForNullWithErrorMessage(object obj, string nullMsg)
		{
			if (obj != null) return false;

			MessageBox.Show(nullMsg, "Message");
			return true;
		}
	}
}