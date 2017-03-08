namespace MessengerAdminPanel.Repositories.Interfaces
{
	public interface IConversationRepository : IEntityBaseRepository<Conversation>
	{
		void Update(int id, Conversation convObj, Conversation item);
	}
}
