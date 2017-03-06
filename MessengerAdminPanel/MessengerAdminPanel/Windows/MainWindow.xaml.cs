using MessengerAdminPanel.Contexts;
using MessengerAdminPanel.UnitOfWork;
using MessengerAdminPanel.ViewModels;
using MessengerAdminPanel.Windows;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;

namespace MessengerAdminPanel
{
	public partial class MainWindow : Window, IMainWindowView
	{
		private readonly MainWindowController _controller;
		private const int UNDEFINED_ENUM_VALUE = -1;
		
		public MainWindow()
		{
			var context = MessengerContextFactory.Create();
			var uof = UnitOfWorkFactory.Create(context);

			_controller = new MainWindowController(this, uof);

			InitializeComponent();

			Loaded += mainWindow_Loaded;
		}

		public void UpdateDataGridLog(IEnumerable<EventLogViewModel> eventLog)
		{
			dataGridLog.ItemsSource = eventLog;
		}

		public void UpdateListViewAnnouncement(List<AnnouncementViewModel> list)
		{
			listViewAnnouncements.ItemsSource = list;
		}

		private void mainWindow_Loaded(object sender, RoutedEventArgs e)
		{
			requestUpdatingDataGridLog();
			_controller.UpdateAnnouncementsListView(true);
		}

		private void textBox_TextChanged(object sender, TextChangedEventArgs e)
		{
			requestUpdatingDataGridLog();
		}

		private void requestUpdatingDataGridLog()
		{
			var logEntitySelected = comboBoxLogEntity != null ? comboBoxLogEntity.SelectedIndex : UNDEFINED_ENUM_VALUE;
			var logEventSelected = comboBoxLogEvent.SelectedIndex;

			_controller.UpdateDataGridLog(
				eventLog => eventLog.Message.Contains(textBoxEventLogSearch.Text),
				logEntitySelected,
				logEventSelected
			);
		}

		private void comboBoxEventLog_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			requestUpdatingDataGridLog();
		}

		private void radioButtonActiveAnnouncement_Checked(object sender, RoutedEventArgs e)
		{
			_controller.UpdateAnnouncementsListView(true);
		}

		private void radioButtonClosedAnnouncement_Checked(object sender, RoutedEventArgs e)
		{
			_controller.UpdateAnnouncementsListView(false);
		}

		private void btnNewAnnouncement_Click(object sender, RoutedEventArgs e)
		{
			_controller.CreateNewAnnouncement();
			radioButtonActiveAnnouncement_Checked(sender, e);
		}
	}
}