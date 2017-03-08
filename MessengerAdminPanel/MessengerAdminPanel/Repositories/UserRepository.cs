using MessengerAdminPanel.Repositories.Interfaces;
using System.Collections.Generic;

namespace MessengerAdminPanel.Repositories
{
	public class UserRepository : EntityBaseRepository<User>, IUserRepository
	{
		public UserRepository(MessengerContext context) : base(context)
		{
		}

		public void Update(int id, User userObj, User item)
		{
			userObj.Login = item.Login;

			if (item.Password != null) userObj.Password = item.Password;

			userObj.Email = item.Email;
			userObj.IsOnline = item.IsOnline;
		}

		public IEnumerable<User> FindUsers(string login)
		{
			return FindBy(u => u.Login.Contains(login));
		}
	}
}
