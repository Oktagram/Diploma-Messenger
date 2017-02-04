using System.Linq;
using Messenger.Contexts;
using Messenger.Models;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace Messenger.Repositories
{
    public class UserRepository : EntityBaseRepository<User>, IUserRepository
    {
        private MessengerContext _context;
        public UserRepository(MessengerContext context)
            : base(context)
        {
            _context = context;
        }

        public void Update(int id,User userObj, User item)
        {
                userObj.Login = item.Login;
                if (item.Password != null) userObj.Password = item.Password;
                userObj.Email = item.Email;
                userObj.IsOnline = item.IsOnline;
                Commit(); 
        }

        public IEnumerable<User> FindUsers(string login)
        {
            return FindBy(u => u.Login.Contains(login));
        }
    }
}
