using System.Collections.Generic;

namespace MessengerAdminPanel.Repositories.Interfaces
{
	public interface IMessageRepository : IEntityBaseRepository<Message>
	{
		void Update(int id, Message messObj, Message item);
		void AddAttachments(int id, string files);
		IEnumerable<int> GetOrderedConversationIds();
	}
}
