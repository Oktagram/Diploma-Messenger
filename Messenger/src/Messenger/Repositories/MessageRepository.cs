﻿using Messenger.Contexts;
using Messenger.Models;
using System.Collections.Generic;
using System.Linq;

namespace Messenger.Repositories
{
	public class MessageRepository : EntityBaseRepository<Message>, IMessageRepository
    {
        public MessageRepository(MessengerContext context)
            : base(context)
        { }

        public void Update(int id, Message messObj, Message item)
        {
            messObj.UserId = item.UserId;
            messObj.ConversationId = item.ConversationId;
            messObj.Text = item.Text;
            messObj.Attachment = item.Attachment;
            Commit();
        }

        public IEnumerable<int> GetOrderedConversationIds()
        {
            IEnumerable<int> orderedConversationIds = GetAll().OrderByDescending(id => id.Id)
                                                              .Select(m => m.ConversationId)                                                           
                                                              .Distinct();
            return orderedConversationIds;
        }

        public void AddAttachments(int id, string files)
        {
            Message message = Find(id);
            message.Attachment = files;
            Commit();
        }
    }
}
