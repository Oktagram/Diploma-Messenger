using System;

namespace Messenger.Models
{
    public class PersonalInfo : IEntityBase
    {
        public int Id { get; set; }
        public string Picture { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string PhoneNumber { get; set; }
        public long? BirthDate { get; set; }

        public int UserId { get; set; }
        public User User { get; set; }
    }
}
