using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.ViewModels
{
    public class PersonalInfoViewModel
    {
        public int Id { get; set; }
        public string Picture { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string PhoneNumber { get; set; }
        public long BirthDate { get; set; }
        public int UserId { get; set; }
    }
}
