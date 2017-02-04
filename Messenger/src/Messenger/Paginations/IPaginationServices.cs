using Messenger.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.Paginations
{
    public interface IUserPaginationService : IEntityBasePaginationService<User> { }

    public interface IConversationPaginationService : IEntityBasePaginationService<Conversation> { }

    public interface IMessagePaginationService : IEntityBasePaginationService<Message> { }

    public interface IPersonalInfoPaginationService : IEntityBasePaginationService<PersonalInfo> { }

}
