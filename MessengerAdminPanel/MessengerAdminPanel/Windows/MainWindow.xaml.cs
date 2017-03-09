using MessengerAdminPanel.Extensions;
using MessengerAdminPanel.Mapping;
using MessengerAdminPanel.Services;
using MessengerAdminPanel.UnitOfWorks;
using MessengerAdminPanel.ViewModels;
using MessengerAdminPanel.Windows;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace MessengerAdminPanel
{
	public partial class MainWindow : Window, IMainWindowView
	{
		private readonly IMainWindowController _controller;
		private readonly IValidationService _validationService;
		private readonly Columns _columns;

		private const int UNDEFINED_ENUM_VALUE = -1;

		private string _messageAttachment;

		public MainWindow()
		{
			var context = new MessengerContext();
			var uof = new UnitOfWork(context);
			var fileService = new FileService();
			var mappingService = new MappingService(fileService);

			_controller = new MainWindowController(this, uof, mappingService, fileService);
			_columns = new Columns();
			_validationService = new ValidationService();
			
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

		public void UpdateConversationData(string name, string creationDate, string countOfMessages, string countOfUsers)
		{
			textBlockConversationName.Text = name;
			textBlockCreationDate.Text = creationDate;
			textBlockMessagesInConversationCount.Text = countOfMessages;
			textBlockUsersInConversationCount.Text = countOfUsers;
		}

		public void UpdateConversationListViewWithUsersList(List<UserViewModel> list)
		{
			listViewUsersInConversation.ItemsSource = list;
		}

		public void UpdateConversationListViewWithMessagesList(List<MessageViewModel> list)
		{
			listViewUsersInConversation.ItemsSource = list;
		}

		public void UpdateMessageData(string user, string text, string conversation, string sendDate, string attachmentName, string attachmentPath)
		{
			textBlockMessageUserSent.Text = user;
			textBlockMessageText.Text = text;
			textBlockMessageConversationName.Text = conversation;
			textBlockMessageSendDate.Text = sendDate;
			textBlockMessageAttachment.Text = attachmentName;

			_messageAttachment = attachmentPath;

			if (String.IsNullOrEmpty(attachmentName))
			{
				buttonOpenAttachment.IsEnabled = false;
				buttonOpenAttachment.Visibility = Visibility.Hidden;
			}
			else
			{
				buttonOpenAttachment.IsEnabled = true;
				buttonOpenAttachment.Visibility = Visibility.Visible;
			}
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
			listViewAnnouncements.RemoveColumnByHeader(_columns.AnnouncementClosedColumnHeader);
		}

		private void radioButtonClosedAnnouncement_Checked(object sender, RoutedEventArgs e)
		{
			radioButtonClosedAnnouncement.IsChecked = true;
			btnOpenAnnouncement.IsEnabled = true;
			btnCloseAnnouncement.IsEnabled = false;
			_controller.UpdateAnnouncementsListView(false);
			listViewAnnouncements.AddColumnByHeader(_columns.AnnouncementClosedColumnHeader, nameof(Announcement.ClosingDate));
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

		private void textBox_HandleAllExceptNumbers_PreviewTextInput(object sender, TextCompositionEventArgs e)
		{
			e.Handled = _validationService.DoesTextContainsOnlyNumbers(e.Text);
		}

		private void textBoxConversationId_TextChanged(object sender, TextChangedEventArgs e)
		{
			var conversationIdStr = textBoxConversationId.Text;

			if (radioButtonUsersInConversation.IsChecked.Value)
				_controller.UpdateListViewUsersInConversation(conversationIdStr);
			else
				_controller.UpdateListViewMessagesInConversation(conversationIdStr);

			_controller.UpdateConversationData(conversationIdStr);
		}

		private void radioButtonUsersInConversation_Checked(object sender, RoutedEventArgs e)
		{
			_controller.UpdateListViewUsersInConversation(textBoxConversationId.Text);

			listViewUsersInConversation.RemoveColumnsByHeaders(_columns.MessageViewModelColumns.Keys.ToList());
			listViewUsersInConversation.AddColumnsByHeaders(_columns.UserViewModelColumns);
		}

		private void radioButtonMessagesInConversation_Checked(object sender, RoutedEventArgs e)
		{
			_controller.UpdateListViewMessagesInConversation(textBoxConversationId.Text);

			listViewUsersInConversation.RemoveColumnsByHeaders(_columns.UserViewModelColumns.Keys.ToList());
			listViewUsersInConversation.AddColumnsByHeaders(_columns.MessageViewModelColumns);
		}

		private void textBoxMessageId_TextChanged(object sender, TextChangedEventArgs e)
		{
			var messageIdStr = textBoxMessageId.Text;
			_controller.UpdateMessageData(messageIdStr);
		}

		private void buttonOpenAttachment_Click(object sender, RoutedEventArgs e)
		{
			_controller.OpenFile(_messageAttachment);
		}
	}
}