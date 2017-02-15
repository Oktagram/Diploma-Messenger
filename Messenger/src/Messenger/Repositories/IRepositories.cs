using System.Collections.Generic;
using Messenger.Models;

namespace Messenger.Repositories
{
	public interface IUserRepository : IEntityBaseRepository<User>
    {
        void Update(int id,User findById,User item);
        IEnumerable<User> FindUsers(string login);
    }

    public interface IConversationRepository : IEntityBaseRepository<Conversation>
    {
        void Update(int id, Conversation convObj, Conversation item);
    }

    public interface IMessageRepository : IEntityBaseRepository<Message>
    {
        void Update(int id, Message messObj, Message item);
        void AddAttachments(int id, string files);
        IEnumerable<int> GetOrderedConversationIds();
    }

    public interface IPersonalInfoRepository : IEntityBaseRepository<PersonalInfo>
    {
        void Update(int id, PersonalInfo messObj, PersonalInfo item);
        void AddPicture(int id, string files);
    }

    public interface IUserConversationRepository : IEntityBaseRepository<UserConversation>
    {
        void DeleteByIds(int UserId, int ConvId);
        IEnumerable<int> FindFriends(int userId);
    }

	public interface IAnnouncementRepository : IEntityBaseRepository<Announcement>
	{
		void Update(int id, Announcement convObj, Announcement item);
	}
}
