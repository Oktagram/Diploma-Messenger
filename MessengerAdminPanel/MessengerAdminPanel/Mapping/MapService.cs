using MessengerAdminPanel.Mapping.EventLogEnums;
using MessengerAdminPanel.Services;
using MessengerAdminPanel.ViewModels;
using System;
using System.Collections.Generic;

namespace MessengerAdminPanel.Mapping
{
	public class MapService
	{
		public static IEnumerable<EventLogViewModel> EventLogToViewModel(IEnumerable<EventLog> list)
		{
			var result = new List<EventLogViewModel>();

			foreach(var eventLog in list)
			{
				var mapped = new EventLogViewModel();
				
				mapped.Id = eventLog.Id;
				mapped.Message = eventLog.Message;

				var date = DateService.DateTimeFromUnixTimestampMillis(eventLog.CreatedTime.Value);

				mapped.CreatedTime = date;
				mapped.Entity = ((EventLogEntity)eventLog.EntityId.Value).ToString();
				mapped.Event = ((EventLogEvent)eventLog.EventId.Value).ToString();

				result.Add(mapped);
			}

			return result;
		}
	}
}
