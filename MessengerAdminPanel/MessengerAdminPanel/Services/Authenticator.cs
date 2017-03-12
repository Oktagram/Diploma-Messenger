using System;
using MessengerAdminPanel.Services.Interfaces;
using MessengerAdminPanel.UnitOfWorks;
using MessengerAdminPanel.Exceptions;
using MessengerAdminPanel.Repositories.Interfaces;

namespace MessengerAdminPanel.Services
{
	public class Authenticator : IAuthenticator
	{
		private readonly IUnitOfWork _uow;
		private readonly IHashingService _hashingService;

		public Authenticator(IUnitOfWork uow, IHashingService hashingService)
		{
			_uow = uow;
			_hashingService = hashingService;
		}

		public void CheckCredentials(string username, string password)
		{
			if (String.IsNullOrEmpty(username))
				throw new ArgumentNullException(nameof(username), "Username cannot be empty");
			if (String.IsNullOrEmpty(password))
				throw new ArgumentNullException(nameof(password), "Password cannot be empty");

			var user = _uow.UserRepository.GetSingle(u => u.Login.Equals(username));

			if (user == null)
				throw new IllegalCredentialsException("User not found");

			var passwordHash = _hashingService.GetHash(password);

			if (!user.Password.Equals(passwordHash))
				throw new IllegalCredentialsException("Wrong password");

			if (!user.IsAdmin)
				throw new PermissionException("No permission");

			CurrentUser.Id = user.Id;
		}
	}
}