using MessengerAdminPanel.Repositories.Interfaces;
using System.Security.Cryptography;
using System.Text;

namespace MessengerAdminPanel.Services
{
	public class MD5HashingService : IHashingService
	{
		public string GetHash(string input)
		{
			var md5 = MD5.Create();
			var hash = md5.ComputeHash(Encoding.UTF8.GetBytes(input));
			var stringBuilder = new StringBuilder();

			for (var i = 0; i < hash.Length; ++i)
				stringBuilder.Append(hash[i].ToString("x2"));

			return stringBuilder.ToString();
		}
	}
}
