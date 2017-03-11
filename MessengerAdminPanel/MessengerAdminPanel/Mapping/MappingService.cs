using MessengerAdminPanel.Mapping.EventLogEnums;
using MessengerAdminPanel.Services;
using MessengerAdminPanel.ViewModels;
using System;
using System.Collections.Generic;

namespace MessengerAdminPanel.Mapping
{
	public class MappingService : IMappingService
	{
		private readonly IFileService _fileWorker;

		public MappingService(IFileService fileWorker)
		{
			_fileWorker = fileWorker;
		}

		public PersonalInfoViewModel PersonalInfoToViewModel(PersonalInfo info)
		{
			var infoVM = new PersonalInfoViewModel();
			var picture = String.Empty;

			if (!String.IsNullOrEmpty(info.Picture))
				picture = _fileWorker.GetFileNameByPath(info.Picture);

			infoVM.Picture = picture;
			infoVM.Id = info.Id;

			if (info.BirthDate != null)
			{
				var birthDate = info.BirthDate.Value;
				infoVM.BirthDate = DateService.DateTimeFromUnixTimestampMillis(birthDate);
			}
			else
				infoVM.BirthDate = null;
			
			infoVM.FirstName = info.FirstName;
			infoVM.LastName = info.LastName;
			infoVM.PhoneNumber = info.PhoneNumber;
			infoVM.User = $"[{info.UserId}] {info.User.Login}";

			return infoVM;
		}

		public MessageViewModel MessageToViewModel(Message message)
		{
			var messageVM = new MessageViewModel();
			var attachment = String.Empty;

			if (!String.IsNullOrEmpty(message.Attachment))
				attachment = _fileWorker.GetFileNameByPath(message.Attachment);
			
			messageVM.Attachment = attachment;
			messageVM.Conversation = $"[{message.Conversation.Id}] {message.Conversation.Name}";
			messageVM.Id = message.Id;
			messageVM.SendDate = DateService.DateTimeFromUnixTimestampMillis(message.SendDate);
			messageVM.Text = message.Text;
			messageVM.User = $"[{message.UserId}] {message.User.Login}";

			return messageVM;
		}

		public IEnumerable<MessageViewModel> MessageToViewModel(IEnumerable<Message> messages)
		{
			var result = new List<MessageViewModel>();

			foreach (var message in messages)
				result.Add(MessageToViewModel(message));
			
			return result;
		}

		public UserViewModel UserToViewModel(User user)
		{
			var userVM = new UserViewModel();

			userVM.Id = user.Id;
			userVM.Email = user.Email;
			userVM.IsAdmin = user.IsAdmin;
			userVM.IsBanned = user.IsBanned;
			userVM.IsOnline = user.IsOnline;
			userVM.Login = $"[{user.Id}] {user.Login}";
			userVM.RegistrationDate = DateService.DateTimeFromUnixTimestampMillis(user.RegistrationDate);

			return userVM;
		}

		public IEnumerable<UserViewModel> UserToViewModel(IEnumerable<User> users)
		{
			var result = new List<UserViewModel>();

			foreach (var user in users)
				result.Add(UserToViewModel(user));
			
			return result;
		}

		public IEnumerable<AnnouncementViewModel> AnnouncementToViewModel(IEnumerable<Announcement> announcements)
		{
			var result = new List<AnnouncementViewModel>();

			foreach (var announcement in announcements)
			{
				var announcementVM = new AnnouncementViewModel();
				var closingDate = DateService.DateTimeFromUnixTimestampMillis(announcement.ClosingDate);
				var createdDate = DateService.DateTimeFromUnixTimestampMillis(announcement.CreationDate);
			
				announcementVM.Id = announcement.Id;
				announcementVM.ClosingDate = closingDate;
				announcementVM.CreationDate = createdDate;
				announcementVM.Description = announcement.Description;
				announcementVM.IsActive = announcement.IsActive;
				announcementVM.User = $"[{announcement.UserId}] {announcement.User.Login}";

				result.Add(announcementVM);
			}
			
			return result;
		}

		public IEnumerable<EventLogViewModel> EventLogToViewModel(IEnumerable<EventLog> eventLogs)
		{
			var result = new List<EventLogViewModel>();

			foreach(var eventLog in eventLogs)
			{
				var mapped = new EventLogViewModel();
				var createdDate = DateService.DateTimeFromUnixTimestampMillis(eventLog.CreatedTime.Value);

				mapped.Id = eventLog.Id;
				mapped.Message = eventLog.Message;
				mapped.CreatedTime = createdDate;
				mapped.Entity = ((EventLogEntity)eventLog.EntityId.Value).ToString();
				mapped.Event = ((EventLogEvent)eventLog.EventId.Value).ToString();

				result.Add(mapped);
			}

			return result;
		}

		public ConversationViewModel ConversationToViewModel(Conversation conversation)
		{
			var conversationVM = new ConversationViewModel();

			conversationVM.Name = conversation.Name;
			conversationVM.CreationDate = DateService.DateTimeFromUnixTimestampMillis(conversation.CreationDate).ToString();
			conversationVM.CounfOfMessages = conversation.Message.Count.ToString();
			conversationVM.CountOfUsers = conversation.User.Count.ToString();

			return conversationVM;
		}
	}
}