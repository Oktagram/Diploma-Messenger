using MessengerAdminPanel.Mapping.EventLogEnums;
using MessengerAdminPanel.Services;
using MessengerAdminPanel.UnitOfWork;
using MessengerAdminPanel.ViewModels;
using System.Collections.Generic;

namespace MessengerAdminPanel.Mapping
{
	public class MapService
	{
		public static IEnumerable<AnnouncementViewModel> AnnouncementToViewModel(IEnumerable<Announcement> announcementsList, IUnitOfWork uow)
		{
			var result = new List<AnnouncementViewModel>();

			foreach (var item in announcementsList)
			{
				var announcementVM = new AnnouncementViewModel();
				var closingDate = DateService.DateTimeFromUnixTimestampMillis(item.ClosingDate);
				var createdDate = DateService.DateTimeFromUnixTimestampMillis(item.CreationDate);
				var user = uow.UserRepository.Find(item.UserId);
				
				announcementVM.Id = item.Id;
				announcementVM.ClosingDate = closingDate;
				announcementVM.CreationDate = createdDate;
				announcementVM.Description = item.Description;
				announcementVM.IsActive = item.IsActive;
				announcementVM.User = $"[{item.UserId}] {user.Login}";

				result.Add(announcementVM);
			}
			
			return result;
		}

		public static IEnumerable<EventLogViewModel> EventLogToViewModel(IEnumerable<EventLog> list)
		{
			var result = new List<EventLogViewModel>();

			foreach(var eventLog in list)
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
	}
}
