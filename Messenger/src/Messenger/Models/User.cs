using System;
using System.Collections.Generic;

namespace Messenger.Models
{
    public class User : IEntityBase
    {
        public User()
        {
            UserConversations = new List<UserConversation>();
            Messages = new List<Message>();
        }

        public int Id { get; set; }
        public string Login { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }        
        public long RegistrationDate { get; set; }
        public bool IsOnline { get; set; }
        public bool IsAdmin { get; set; }
        public bool IsBanned { get; set; }

        public PersonalInfo PersonalInfo { get; set; }
        public virtual ICollection<UserConversation> UserConversations { get; set; }
        public virtual ICollection<Message> Messages { get; set; }
    }
}
