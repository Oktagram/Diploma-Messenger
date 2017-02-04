using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Messenger.Contexts;
using Messenger.Models;

namespace Messenger.Repositories
{
    public class UserConversationRepository : EntityBaseRepository<UserConversation>, IUserConversationRepository
    {
        private MessengerContext _context { get; set; }
        public UserConversationRepository(MessengerContext context)
            : base(context)
        {
            _context = context;
        }

        public void DeleteByIds(int userId, int convId)
        {
            var item = GetSingle(uc => uc.UserId == userId && uc.ConversationId == convId);
            if (item != null)
            {
                _context.UserConversations.Remove(item);
                Commit();
            }          
        }

        public IEnumerable<int> FindFriends(int userId)
        {
            var userConvs = FindBy(uc => uc.UserId == userId);
            List<int> friendsIds = new List<int>();
            foreach (var uc in userConvs)
            {
                var friendsConvs = FindBy(fc => fc.ConversationId == uc.ConversationId)
                                    .Where(u => u.UserId != userId);
                foreach(var fc in friendsConvs)
                {
                    friendsIds.Add(fc.UserId);
                }                                   
            }
            return friendsIds.Distinct().ToList();
        }
    }
}