using MessengerAdminPanel.Repositories.Interfaces;
using System.Collections.Generic;
using System.Linq;

namespace MessengerAdminPanel.Repositories
{
	public class UserRepository : EntityBaseRepository<User>, IUserRepository
	{
		public UserRepository(MessengerContext context) : base(context)
		{
		}

		public void Update(int id, User userObj, User item)
		{
			_context.Refresh();

			userObj.Login = item.Login;
			userObj.Email = item.Email;
			userObj.IsOnline = item.IsOnline;
			if (item.Password != null) userObj.Password = item.Password;
		}

		public IEnumerable<User> FindUsers(string login)
		{
			_context.Refresh();
			return FindBy(u => u.Login.Contains(login));
		}

		public IEnumerable<User> FindFriends(int userId)
		{
			var userConversations = Find(userId).Conversation;
			var friends = new List<User>();

			foreach (var conversation in userConversations)
			{
				var usersInConversations = conversation.User.Where(u => u.Id != userId);
				
				foreach (var friend in usersInConversations)
					friends.Add(friend);
			}
			
			return friends.Distinct();
		}
	}
}
