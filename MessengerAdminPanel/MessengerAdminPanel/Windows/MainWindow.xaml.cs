using MessengerAdminPanel.Contexts;
using MessengerAdminPanel.Extensions;
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
		private readonly IMainWindowController _controller;
		private const int UNDEFINED_ENUM_VALUE = -1;
		private const string CLOSED_COLUMN_HEADER = "Closed";
		
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
			radioButtonActiveAnnouncement.IsChecked = true;
			btnOpenAnnouncement.IsEnabled = false;
			btnCloseAnnouncement.IsEnabled = true;
			_controller.UpdateAnnouncementsListView(true);
			listViewAnnouncements.HideColumnByHeader(CLOSED_COLUMN_HEADER);
		}

		private void radioButtonClosedAnnouncement_Checked(object sender, RoutedEventArgs e)
		{
			radioButtonClosedAnnouncement.IsChecked = true;
			btnOpenAnnouncement.IsEnabled = true;
			btnCloseAnnouncement.IsEnabled = false;
			_controller.UpdateAnnouncementsListView(false);
			listViewAnnouncements.ShowColumnByHeader(CLOSED_COLUMN_HEADER, nameof(Announcement.ClosingDate));
		}

		private void btnNewAnnouncement_Click(object sender, RoutedEventArgs e)
		{
			_controller.CreateNewAnnouncement();
			radioButtonActiveAnnouncement_Checked(sender, e);
		}

		private void btnEditAnnouncement_Click(object sender, RoutedEventArgs e)
		{
			var announcementVM = (AnnouncementViewModel)listViewAnnouncements.SelectedValue;
			var activity = radioButtonActiveAnnouncement.IsChecked.Value;
			_controller.EditAnnouncement(announcementVM, activity);
		}

		private void btnDeleteAnnouncement_Click(object sender, RoutedEventArgs e)
		{
			var announcementVM = (AnnouncementViewModel)listViewAnnouncements.SelectedValue;
			var activity = radioButtonActiveAnnouncement.IsChecked.Value;
			_controller.DeleteAnnouncement(announcementVM, activity);
		}

		private void btnCloseAnnouncement_Click(object sender, RoutedEventArgs e)
		{
			var announcementVM = (AnnouncementViewModel)listViewAnnouncements.SelectedValue;
			_controller.ChangeAnnouncementStatus(announcementVM, true);
		}

		private void btnOpenAnnouncement_Click(object sender, RoutedEventArgs e)
		{
			var announcementVM = (AnnouncementViewModel)listViewAnnouncements.SelectedValue;
			_controller.ChangeAnnouncementStatus(announcementVM, false);
		}
	}
}