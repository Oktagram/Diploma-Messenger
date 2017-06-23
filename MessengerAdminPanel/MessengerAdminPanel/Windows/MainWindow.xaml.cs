using MessengerAdminPanel.Extensions;
using MessengerAdminPanel.Mapping;
using MessengerAdminPanel.Services;
using MessengerAdminPanel.UnitOfWorks;
using MessengerAdminPanel.ViewModels;
using MessengerAdminPanel.Windows;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using static System.String;

namespace MessengerAdminPanel
{
	public enum PeriodOfTime
	{
		Week,
		Month,
		HalfYear,
		Year
	}

	public partial class MainWindow : Window, IMainWindowView
	{
		private readonly IMainWindowController _controller;
		private readonly IValidationService _validationService;
		private readonly Columns _columns;

		private const int UndefinedEnumValue = -1;

		private string _messageAttachmentPath;
		private string _profilePicturePath;

		private bool _firstLoadRequest;
		private bool _secondLoadRequest;

		public MainWindow()
		{
			var context = new MessengerContext();
			var uow = new UnitOfWork(context);
			var fileService = new FileService();
			var mappingService = new MappingService(fileService);
			var reportsSaver = new PdfReportsSaver();

			_controller = new MainWindowController(this, uow, mappingService, fileService, reportsSaver);
			_columns = new Columns();
			_validationService = new ValidationService();

			_firstLoadRequest = true;
			_secondLoadRequest = true;

			InitializeComponent();

			Loaded += mainWindow_Loaded;
		}

		public void UpdateDataGridEventLog(IEnumerable<EventLogViewModel> eventLog)
		{
			dataGridEventLog.ItemsSource = eventLog;
		}

		public void UpdateListViewAnnouncement(List<AnnouncementViewModel> list)
		{
			listViewAnnouncements.ItemsSource = list;
		}

		public void UpdateConversationData(ConversationViewModel conversaionVM)
		{
			if (conversaionVM == null)
			{
				textBlockConversationName.Text = Empty;
				textBlockCreationDate.Text = Empty;
				textBlockMessagesInConversationCount.Text = Empty;
				textBlockUsersInConversationCount.Text = Empty;

				buttonEditConversationName.IsEnabled = false;
				buttonDeleteConversation.IsEnabled = false;

				return;
			}

			textBlockConversationName.Text = conversaionVM.Name;
			textBlockCreationDate.Text = conversaionVM.CreationDate.ToString();
			textBlockMessagesInConversationCount.Text = conversaionVM.CounfOfMessages;
			textBlockUsersInConversationCount.Text = conversaionVM.CountOfUsers;
			
			buttonEditConversationName.IsEnabled = true;
			buttonDeleteConversation.IsEnabled = true;
		}

		public void UpdateConversationListViewWithUsersList(List<UserViewModel> list)
		{
			listViewConversationReferences.RemoveAllColumns();
			listViewConversationReferences.AddColumnsByHeaders(_columns.UserViewModelColumns);
	
			listViewConversationReferences.ItemsSource = list;
		}

		public void UpdateConversationListViewWithMessagesList(List<MessageViewModel> list)
		{
			listViewConversationReferences.RemoveAllColumns();
			listViewConversationReferences.AddColumnsByHeaders(_columns.MessageViewModelColumns);

			listViewConversationReferences.ItemsSource = list;
		}

		public void UpdateMessageData(MessageViewModel messageVM, string attachmentPath)
		{
			if (IsNullOrEmpty(attachmentPath))
			{
				buttonOpenAttachment.IsEnabled = false;
				buttonOpenAttachment.Visibility = Visibility.Hidden;
			}
			else
			{
				buttonOpenAttachment.IsEnabled = true;
				buttonOpenAttachment.Visibility = Visibility.Visible;
			}

			if (messageVM == null)
			{
				textBlockMessageUserSent.Text = Empty;
				textBlockMessageText.Text = Empty;
				textBlockMessageConversationName.Text = Empty;
				textBlockMessageSendDate.Text = Empty;
				textBlockMessageAttachment.Text = Empty;

				buttonDeleteMessage.IsEnabled = false;

				return;
			}

			textBlockMessageUserSent.Text = messageVM.User;
			textBlockMessageText.Text = messageVM.Text;
			textBlockMessageConversationName.Text = messageVM.Conversation;
			textBlockMessageSendDate.Text = messageVM.SendDate.ToString();
			textBlockMessageAttachment.Text = messageVM.Attachment;

			buttonDeleteMessage.IsEnabled = true;

			_messageAttachmentPath = attachmentPath;
		}

		public void UpdateUserData(UserViewModel userVM, PersonalInfoViewModel infoVM, string profilePicturePath)
		{
			if (userVM == null || infoVM == null)
			{
				textBlockUser.Text = "Not Found";
				textBlockEmail.Text = Empty;
				textBlockRegistrationDate.Text = Empty;
				textBlockIsOnline.Text = Empty;

				textBlockBirthdate.Text = Empty;
				textBlockFirstName.Text = Empty;
				textBlockLastName.Text = Empty;
				textBlockPhoneNumber.Text = Empty;

				radioButtonBannedTrue.IsChecked = false;
				radioButtonBannedFalse.IsChecked = false;

				radioButtonAdminTrue.IsChecked = false;
				radioButtonAdminFalse.IsChecked = false;

				buttonOpenProfilePicture.IsEnabled = false;
				buttonEditUsername.IsEnabled = false;

				return;
			}
			
			textBlockUser.Text = userVM.Login;
			textBlockEmail.Text = userVM.Email;
			textBlockRegistrationDate.Text = userVM.RegistrationDate.ToString();
			textBlockIsOnline.Text = userVM.IsOnline.ToString();

			var birthDate = infoVM.BirthDate;
			var birthDateStr = (birthDate == null) ? Empty : birthDate.ToString();
			textBlockBirthdate.Text = birthDateStr.Split(' ')[0];
			textBlockFirstName.Text = infoVM.FirstName;
			textBlockLastName.Text = infoVM.LastName;
			textBlockPhoneNumber.Text = infoVM.PhoneNumber;

			_profilePicturePath = profilePicturePath;

			if (userVM.IsBanned)
				radioButtonBannedTrue.IsChecked = true;
			else
				radioButtonBannedFalse.IsChecked = true;

			if (userVM.IsAdmin)
				radioButtonAdminTrue.IsChecked = true;
			else
				radioButtonAdminFalse.IsChecked = true;

			if (IsNullOrEmpty(infoVM.Picture))
				buttonOpenProfilePicture.IsEnabled = false;
			else
				buttonOpenProfilePicture.IsEnabled = true;

			buttonEditUsername.IsEnabled = true;
		}

		public void UpdateUserListViewWithConversationsList(List<ConversationViewModel> list)
		{
			listViewUserReferences.RemoveAllColumns();
			listViewUserReferences.AddColumnsByHeaders(_columns.ConversationViewModelColumns);

			listViewUserReferences.ItemsSource = list;
		}

		public void UpdateUserListViewWithMessagesList(List<MessageViewModel> list)
		{
			listViewUserReferences.RemoveAllColumns();
			listViewUserReferences.AddColumnsByHeaders(_columns.MessageViewModelColumns);
	
			listViewUserReferences.ItemsSource = list;
		}

		public void UpdateUserListViewWithUsersList(List<UserViewModel> list)
		{
			listViewUserReferences.RemoveAllColumns();
			listViewUserReferences.AddColumnsByHeaders(_columns.UserViewModelColumns);
			
			listViewUserReferences.ItemsSource = list;
		}

		public void ShowMessageBox(string message, string title = "Message")
		{
			MessageBox.Show(message, title);
		}

		public bool ShowMessageBoxYesNo(string message, string title)
		{
			return MessageBox.Show(message, title, MessageBoxButton.YesNo) == MessageBoxResult.Yes;
		}

		public string ShowPromptWindow(string text, string defaultValue = "")
		{
			var prompt = new PromptWindow(text, defaultValue);

			if (!prompt.ShowDialog().Value) return null;

			return prompt.ResponseText;
		}

		private string getUsernameByUsernameView(string usernameView)
		{
			return textBlockUser.Text.Substring(4);
		}

		private void mainWindow_Loaded(object sender, RoutedEventArgs e)
		{
			_controller.UpdateAnnouncementsListView(true);
		}

		private void textBox_TextChanged(object sender, TextChangedEventArgs e)
		{
			requestUpdatingDataGridLog();
		}

		private void requestUpdatingDataGridLog()
		{
			var logEntitySelected = (comboBoxLogEntity != null) ? comboBoxLogEntity.SelectedIndex : UndefinedEnumValue;
			var logEventSelected = comboBoxLogEvent.SelectedIndex;

			_controller.UpdateDataGridEventLog(
				eventLog => eventLog.Message.Contains(textBoxEventLogSearch.Text),
				logEntitySelected,
				logEventSelected
			);
		}

		private void comboBoxEventLog_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			if (!_firstLoadRequest && !_secondLoadRequest)
				requestUpdatingDataGridLog();
			else
			{
				if (!_firstLoadRequest)
					_secondLoadRequest = false;

				_firstLoadRequest = false;
			}
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
		}

		private void radioButtonMessagesInConversation_Checked(object sender, RoutedEventArgs e)
		{
			_controller.UpdateListViewMessagesInConversation(textBoxConversationId.Text);
		}

		private void textBoxMessageId_TextChanged(object sender, TextChangedEventArgs e)
		{
			var messageIdStr = textBoxMessageId.Text;
			_controller.UpdateMessageData(messageIdStr);
		}

		private void buttonOpenAttachment_Click(object sender, RoutedEventArgs e)
		{
			_controller.OpenFile(_messageAttachmentPath);
		}

		private void textBoxUserId_TextChanged(object sender, TextChangedEventArgs e)
		{
			if (!IsNullOrEmpty(textBoxUserId.Text)) textBoxUsername.Text = Empty;
		
			var userIdStr = textBoxUserId.Text;
			_controller.UpdateUserDataById(userIdStr);

			comboBoxUserListView_SelectionChanged(sender, null);
		}

		private void textBoxUsername_TextChanged(object sender, TextChangedEventArgs e)
		{
			if (!IsNullOrEmpty(textBoxUsername.Text)) textBoxUserId.Text = Empty;

			var username = textBoxUsername.Text;
			_controller.UpdateUserDataByUsername(username);

			comboBoxUserListView_SelectionChanged(sender, null);
		}

		private void buttonShowProfilePicture_Click(object sender, RoutedEventArgs e)
		{
			_controller.OpenFile(_profilePicturePath);
		}

		private void buttonEditUsername_Click(object sender, RoutedEventArgs e)
		{
			var username = getUsernameByUsernameView(textBlockUser.Text);
			_controller.ChangeUsername(username);
		}

		private void radioButtonBannedTrue_Checked(object sender, RoutedEventArgs e)
		{
			var username = getUsernameByUsernameView(textBlockUser.Text);
			_controller.ChangeUserBanStatus(username, true);
		}

		private void radioButtonBannedFalse_Checked(object sender, RoutedEventArgs e)
		{
			var username = getUsernameByUsernameView(textBlockUser.Text);
			_controller.ChangeUserBanStatus(username, false);
		}

		private void radioButtonAdminTrue_Checked(object sender, RoutedEventArgs e)
		{
			var username = getUsernameByUsernameView(textBlockUser.Text);
			_controller.ChangeUserAdminStatus(username, true);
		}

		private void radioButtonAdminFalse_Checked(object sender, RoutedEventArgs e)
		{
			var username = getUsernameByUsernameView(textBlockUser.Text);
			_controller.ChangeUserAdminStatus(username, false);
		}

		private void buttonEditConversationName_Click(object sender, RoutedEventArgs e)
		{
			_controller.ChangeConversationName(textBoxConversationId.Text, textBlockConversationName.Text);
		}

		private void buttonDeleteConversation_Click(object sender, RoutedEventArgs e)
		{
			_controller.DeleteConversation(textBoxConversationId.Text);
		}

		private void buttonDeleteMessage_Click(object sender, RoutedEventArgs e)
		{
			_controller.DeleteMessage(textBoxMessageId.Text);
		}

		private void comboBoxUserListView_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			var userId = textBoxUserId.Text;
			var username = textBoxUsername.Text;
			var selectedTypeIndex = comboBoxUserListView.SelectedIndex;

			_controller.UpdateUserListView(userId, username, selectedTypeIndex);
		}

		private void buttonUpdateEventLog_Click(object sender, RoutedEventArgs e)
		{
			requestUpdatingDataGridLog();
		}

		private void buttonPrintEventLog_Click(object sender, RoutedEventArgs e)
		{
			_controller.SaveDataGridToPdf(dataGridEventLog, "EventLogReport");
		}

		private void Button_Click(object sender, RoutedEventArgs e)
		{
			var window = new ClearingDataWindow();
			window.ShowDialog();

			if (window.Completed)
				_controller.ClearFiles(window.PeriodOfTime);
		}

		private void Button_Click_1(object sender, RoutedEventArgs e)
		{
			var window = new ClearingDataWindow();
			window.ShowDialog();

			if (window.Completed)
				_controller.ClearLogs(window.PeriodOfTime);
		}
	}
}