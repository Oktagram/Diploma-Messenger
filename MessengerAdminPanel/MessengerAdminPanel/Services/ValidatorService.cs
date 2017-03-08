using System.Text.RegularExpressions;

namespace MessengerAdminPanel.Services
{
	public class ValidatorService
	{
		public static bool DoesTextContainsOnlyNumbers(string text)
		{
			var regex = new Regex("[^0-9]+");
			return regex.IsMatch(text);
		}
	}
}
