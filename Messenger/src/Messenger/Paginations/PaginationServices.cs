using Messenger.Contexts;
using Messenger.Controllers;
using Messenger.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.Paginations
{
    public class UserPaginationService : EntityBasePaginationService<User>, IUserPaginationService
    {
        public UserPaginationService(): base() { }
    }

    public class ConversationPaginationService : EntityBasePaginationService<Conversation>, IConversationPaginationService
    {
        public ConversationPaginationService(): base() { }
    }

    public class MessagePaginationService : EntityBasePaginationService<Message>, IMessagePaginationService
    {
        public MessagePaginationService() : base() { }
    }

    public class PersonalInfoPaginationService : EntityBasePaginationService<PersonalInfo>, IPersonalInfoPaginationService
    {
        public PersonalInfoPaginationService() : base() { }
    }
}
