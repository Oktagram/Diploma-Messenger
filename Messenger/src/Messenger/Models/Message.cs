using System;

namespace Messenger.Models
{
    public class Message : IEntityBase
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public int ConversationId { get; set; }
        public string Text { get; set; }
        public string Attachment { get; set; }
        public long SendDate { get; set; }

        public User User { get; set; }
        public Conversation Conversation { get; set; }
    }
}
