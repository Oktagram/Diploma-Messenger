using MessengerAdminPanel.Exceptions;
using MessengerAdminPanel.Factories;
using MessengerAdminPanel.Mapping;
using MessengerAdminPanel.Mapping.EventLogEnums;
using MessengerAdminPanel.Services;
using MessengerAdminPanel.UnitOfWorks;
using MessengerAdminPanel.ViewModels;
using MessengerAdminPanel.Windows;
using System;
using System.ComponentModel;
using System.IO;
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
		private readonly IMappingService _mappingService;
		private readonly IFileService _fileService;

		private Expression<Func<EventLog, bool>> _predicate;
		private int _eventLogEntity;
		private int _eventLogEvent;

		public MainWindowController(IMainWindowView view, IUnitOfWork uof, IMappingService mappingService, IFileService fileService)
		{
			_view = view;
			_uow = uof;
			_mappingService = mappingService;
			_fileService = fileService;

			_timer = new DispatcherTimer();
			_timer.Interval = new TimeSpan(0, 0, 0, 0, 500);
			_timer.Tick += dispatcherTimer_Tick;
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

			var mappedEventLogList = _mappingService.EventLogToViewModel(eventLogList);
			_view.UpdateDataGridLog(mappedEventLogList);
			_timer.Stop();
		}

		public void UpdateAnnouncementsListView(bool isActive)
		{
			var announcementsList = _uow.AnnouncementRepository.FindBy(a => a.IsActive == isActive);
			var mappedAnnouncements = _mappingService.AnnouncementToViewModel(announcementsList).ToList();
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

		private void fillConversationDataWithEmptiness()
		{
			_view.UpdateConversationData(String.Empty, String.Empty, String.Empty, String.Empty);
			_view.UpdateConversationListViewWithUsersList(null);
		}
		
		private Conversation findConversation(string conversationIdStr)
		{
			int conversationId;
			if (!int.TryParse(conversationIdStr, out conversationId))
			{
				fillConversationDataWithEmptiness();
				throw new ArgumentException($"{conversationIdStr} cannot be resolved as conversation id.");
			}

			var conversation = _uow.ConversationRepository.Find(conversationId);
			if (conversation == null)
			{
				fillConversationDataWithEmptiness();
				throw new NotFoundException($"Conversation with id {conversationId} not found.");
			}

			return conversation;
		}

		public void UpdateConversationData(string conversationIdStr)
		{
			try
			{
				var conversation = findConversation(conversationIdStr);
				var creationDateStr = DateService.DateTimeFromUnixTimestampMillis(conversation.CreationDate).ToString();
				var countOfMessagesStr = conversation.Message.Count.ToString();
				var countOfUsersStr = conversation.User.Count.ToString();

				_view.UpdateConversationData(conversation.Name, creationDateStr, countOfMessagesStr, countOfUsersStr);
			}
			catch (NotFoundException) { }
			catch (ArgumentException) { }
		}

		public void UpdateListViewUsersInConversation(string conversationIdStr)
		{
			try
			{
				var conversation = findConversation(conversationIdStr);
				var users = conversation.User.ToList();
				var mappedUsers = _mappingService.UserToViewModel(users).ToList();
				
				_view.UpdateConversationListViewWithUsersList(mappedUsers);
			}
			catch (NotFoundException) { }
			catch (ArgumentException) { }
		}

		public void UpdateListViewMessagesInConversation(string conversationIdStr)
		{
			try
			{
				var conversation = findConversation(conversationIdStr);
				var messages = conversation.Message.ToList();
				var mappedMessages = _mappingService.MessageToViewModel(messages).ToList();
		
				_view.UpdateConversationListViewWithMessagesList(mappedMessages);
			}
			catch (NotFoundException) { }
			catch (ArgumentException) { }
		}

		public void UpdateMessageData(string messageId)
		{
			int id;
			if (!int.TryParse(messageId, out id))
			{
				_view.UpdateMessageData(null, null);
				return;
			}
		
			var message = _uow.MessageRepository.Find(id);
			if (message == null)
			{
				_view.UpdateMessageData(null, null);
				return;
			}

			var messageVM = _mappingService.MessageToViewModel(message);
			_view.UpdateMessageData(messageVM, message.Attachment);
		}

		public void OpenFile(string fileName)
		{
			try
			{
				_fileService.OpenFile(fileName);
			} 
			catch (Exception ex) when (ex is Win32Exception || ex is FileNotFoundException)
			{
				MessageBox.Show("File not found.", "Error");
			}
		}

		public void UpdateUserDataById(string userId)
		{
			int id;
			if (!int.TryParse(userId, out id))
			{
				_view.UpdateUserData(null, null, null);
				return;
			}

			var user = _uow.UserRepository.Find(id);
			var info = _uow.PersonalInfoRepository.Find(id);
			if (user == null || info == null)
			{
				_view.UpdateUserData(null, null, null);
				return;
			}
			
			var messageVM = _mappingService.UserToViewModel(user);
			var infoVM = _mappingService.PersonalInfoToViewModel(info);
			_view.UpdateUserData(messageVM, infoVM, info.Picture);
		}

		public void UpdateUserDataByUsername(string username)
		{
			username = username.ToLower();
			var userEnumerable = _uow.UserRepository.FindBy(u => u.Login.Equals(username.ToLower()));
			
			if (userEnumerable.Count() == 0)
			{
				_view.UpdateUserData(null, null, null);
				return;
			}

			var user = userEnumerable.First();
			var info = _uow.PersonalInfoRepository.Find(user.Id);

			var messageVM = _mappingService.UserToViewModel(user);
			var infoVM = _mappingService.PersonalInfoToViewModel(info);
			_view.UpdateUserData(messageVM, infoVM, info.Picture);
		}
	}
}