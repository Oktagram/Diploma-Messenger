using MessengerAdminPanel.Contexts;
using MessengerAdminPanel.Mapping.EventLogEnums;
using MessengerAdminPanel.UnitOfWork;
using MessengerAdminPanel.ViewModels;
using MessengerAdminPanel.Windows;
using System.Collections;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;

namespace MessengerAdminPanel
{
	public partial class MainWindow : Window, IMainWindowView
	{
		private readonly MainWindowController _controller;
		
		public MainWindow()
		{
			var context = MessengerContextFactory.Create();
			var uof = UnitOfWorkFactory.Create(context);

			_controller = new MainWindowController(this, uof);

			InitializeComponent();

			Loaded += MainWindow_Loaded;
		}

		private void MainWindow_Loaded(object sender, RoutedEventArgs e)
		{
			_controller.UpdateDataGridLog(x => true);
		}

		private void textBox_TextChanged(object sender, TextChangedEventArgs e)
		{
			var tbx = sender as TextBox;
			_controller.UpdateDataGridLog(eventLog => eventLog.Message.Contains(tbx.Text));
		}

		public void UpdateDataGridLog(IEnumerable<EventLogViewModel> eventLog)
		{
			dataGridLog.ItemsSource = eventLog;
		}

		private void comboBoxLogEntity_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			
		}

		private void comboBoxLogEntity_Copy_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{

		}
	}
}
