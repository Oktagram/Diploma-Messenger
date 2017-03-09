using MessengerAdminPanel.Repositories.Interfaces;

namespace MessengerAdminPanel.Repositories
{
	public class ConversationRepository : EntityBaseRepository<Conversation>, IConversationRepository
	{
		public ConversationRepository(MessengerContext context)
			: base(context)
		{ }

		public void Update(int id, Conversation convObj, Conversation item)
		{
			_context.Refresh();
			convObj.Name = item.Name;
		}
	}
}
