using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.ViewModels
{
    public class MessageViewModel
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public int ConversationId { get; set; }
        public string Text { get; set; }
        public string Attachment { get; set; }
        public long SendDate { get; set; }
    }
}
