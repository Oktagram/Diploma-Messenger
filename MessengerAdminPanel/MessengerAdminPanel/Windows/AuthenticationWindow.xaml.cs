using MessengerAdminPanel.Services;
using MessengerAdminPanel.Services.Interfaces;
using MessengerAdminPanel.UnitOfWorks;
using System;
using System.Windows;

namespace MessengerAdminPanel.Windows
{
	public partial class AuthenticationWindow : Window
	{
		public bool AccessAllowed { get; set; }

		private readonly IAuthenticator _authenticator;
		private readonly IUnitOfWork _uow;

		public AuthenticationWindow()
		{
			InitializeComponent();

			var context = new MessengerContext();
			_uow = new UnitOfWork(context);

			var hashingService = new MD5HashingService();
			_authenticator = new Authenticator(_uow, hashingService);
		}
		
		private void Login_Click(object sender, RoutedEventArgs e)
		{
			var username = textBoxUsername.Text;
			var password = textBoxPassword.Password;

			try
			{
				_authenticator.CheckCredentials(username, password);

				new MainWindow().Show();
				this.Close();
			}
			catch (Exception ex)
			{
				MessageBox.Show(ex.Message);
			}
		}
	}
}