using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.ViewModels
{
    public class UserViewModel
    {
        public int Id { get; set; }
        public string Login { get; set; }
        public string Email { get; set; }
        public long RegistrationDate { get; set; }
        public bool IsOnline { get; set; }
    }
}
