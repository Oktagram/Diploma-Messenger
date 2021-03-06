﻿using MessengerAdminPanel.Repositories.Interfaces;
using System;

namespace MessengerAdminPanel.UnitOfWorks
{
	public interface IUnitOfWork : IDisposable
	{
		IEntityBaseRepository<EventLog> EventLogRepositry { get; }
		IAnnouncementRepository AnnouncementRepository { get; }
		IUserRepository UserRepository { get; }
		IConversationRepository ConversationRepository { get; }
		IMessageRepository MessageRepository { get; }
		IPersonalInfoRepository PersonalInfoRepository { get; }

		void Save();
		void Dispose(bool disposing);
	}
}