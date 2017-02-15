using Messenger.Contexts;
using Messenger.Models;

namespace Messenger.Repositories
{
    public class ConversationRepository : EntityBaseRepository<Conversation>, IConversationRepository
    {
        public ConversationRepository(MessengerContext context)
            : base(context)
        { }

        public void Update(int id,Conversation convObj, Conversation item)
        {
            convObj.Name = item.Name;
            Commit();
        }
    }
}
