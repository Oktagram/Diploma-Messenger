using System;
using System.Collections.Generic;

namespace Messenger.Models
{
    public class Conversation : IEntityBase
    {
        public Conversation()
        {
            UserConversations = new List<UserConversation>();
            Messages = new List<Message>();
        }

        public int Id { get; set; }
        public string Name { get; set; }
        public long CreationDate { get; set; }

        public virtual ICollection<UserConversation> UserConversations { get; set; }
        public virtual ICollection<Message> Messages { get; set; }
    }
}