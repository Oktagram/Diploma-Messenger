using MessengerAdminPanel.Repositories.Interfaces;
using System.Collections.Generic;
using System.Linq;

namespace MessengerAdminPanel.Repositories
{
	public class MessageRepository : EntityBaseRepository<Message>, IMessageRepository
	{
		public MessageRepository(MessengerContext context)
			: base(context)
		{ }

		public void Update(int id, Message messObj, Message item)
		{
			_context.Refresh();

			messObj.UserId = item.UserId;
			messObj.ConversationId = item.ConversationId;
			messObj.Text = item.Text;
			messObj.Attachment = item.Attachment;
		}

		public IEnumerable<int> GetOrderedConversationIds()
		{
			_context.Refresh();

			IEnumerable<int> orderedConversationIds = GetAll().OrderByDescending(id => id.Id)
															  .Select(m => m.ConversationId)
															  .Distinct();
			return orderedConversationIds;
		}

		public void AddAttachments(int id, string files)
		{
			_context.Refresh();

			var message = Find(id);
			message.Attachment = files;
		}
	}
}
