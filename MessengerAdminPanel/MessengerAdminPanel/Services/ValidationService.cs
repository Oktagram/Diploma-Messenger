using System.Text.RegularExpressions;

namespace MessengerAdminPanel.Services
{
	public class ValidationService : IValidationService
	{
		public bool DoesTextContainsOnlyNumbers(string text)
		{
			var regex = new Regex("[^0-9]+");
			return regex.IsMatch(text);
		}
	}
}
