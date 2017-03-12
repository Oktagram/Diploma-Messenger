using System;
using System.Windows;

namespace MessengerAdminPanel
{
	public partial class App : Application
	{
		public App()
		{
			InitializeComponent();
		}

		[STAThread]
		public static void Main()
		{
			var application = new App();
			application.Run();
		}
	}
}
