using System.Collections.Generic;

namespace MessengerAdminPanel.Repositories.Interfaces
{
	public interface IUserRepository : IEntityBaseRepository<User>
	{
		void Update(int id, User findById, User item);
		IEnumerable<User> FindUsers(string login);
		IEnumerable<User> FindFriends(int userId);
	}
}
