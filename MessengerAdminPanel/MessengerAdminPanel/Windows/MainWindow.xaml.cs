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

		private string _messageAttachmentPath;
		private string _profilePicturePath;

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

		public void UpdateConversationData(ConversationViewModel conversaionVM)
		{
			if (conversaionVM == null)
			{
				textBlockConversationName.Text = String.Empty;
				textBlockCreationDate.Text = String.Empty;
				textBlockMessagesInConversationCount.Text = String.Empty;
				textBlockUsersInConversationCount.Text = String.Empty;

				buttonEditConversationName.IsEnabled = false;

				return;
			}

			textBlockConversationName.Text = conversaionVM.Name;
			textBlockCreationDate.Text = conversaionVM.CreationDate;
			textBlockMessagesInConversationCount.Text = conversaionVM.CounfOfMessages;
			textBlockUsersInConversationCount.Text = conversaionVM.CountOfUsers;
			
			buttonEditConversationName.IsEnabled = true;
		}

		public void UpdateConversationListViewWithUsersList(List<UserViewModel> list)
		{
			listViewUsersInConversation.ItemsSource = list;
		}

		public void UpdateConversationListViewWithMessagesList(List<MessageViewModel> list)
		{
			listViewUsersInConversation.ItemsSource = list;
		}

		public void UpdateMessageData(MessageViewModel messageVM, string attachmentPath)
		{
			if (String.IsNullOrEmpty(attachmentPath))
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
				textBlockMessageUserSent.Text = String.Empty;
				textBlockMessageText.Text = String.Empty;
				textBlockMessageConversationName.Text = String.Empty;
				textBlockMessageSendDate.Text = String.Empty;
				textBlockMessageAttachment.Text = String.Empty;

				return;
			}

			textBlockMessageUserSent.Text = messageVM.User;
			textBlockMessageText.Text = messageVM.Text;
			textBlockMessageConversationName.Text = messageVM.Conversation;
			textBlockMessageSendDate.Text = messageVM.SendDate.ToString();
			textBlockMessageAttachment.Text = messageVM.Attachment;

			_messageAttachmentPath = attachmentPath;
		}

		public void UpdateUserData(UserViewModel userVM, PersonalInfoViewModel infoVM, string profilePicturePath)
		{
			if (userVM == null || infoVM == null)
			{
				textBlockUser.Text = "Not Found";
				textBlockEmail.Text = String.Empty;
				textBlockRegistrationDate.Text = String.Empty;
				textBlockIsOnline.Text = String.Empty;

				textBlockBirthdate.Text = String.Empty;
				textBlockFirstName.Text = String.Empty;
				textBlockLastName.Text = String.Empty;
				textBlockPhoneNumber.Text = String.Empty;

				radioButtonBannedTrue.IsChecked = false;
				radioButtonBannedFalse.IsChecked = false;

				radioButtonAdminTrue.IsChecked = false;
				radioButtonAdminFalse.IsChecked = false;

				buttonShowProfilePicture.IsEnabled = false;
				buttonEditUsername.IsEnabled = false;

				return;
			}
			
			textBlockUser.Text = userVM.Login;
			textBlockEmail.Text = userVM.Email;
			textBlockRegistrationDate.Text = userVM.RegistrationDate.ToString();
			textBlockIsOnline.Text = userVM.IsOnline.ToString();

			var birthDate = infoVM.BirthDate;
			var birthDateStr = (birthDate == null) ? String.Empty : birthDate.ToString();
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

			if (String.IsNullOrEmpty(infoVM.Picture))
				buttonShowProfilePicture.IsEnabled = false;
			else
				buttonShowProfilePicture.IsEnabled = true;

			buttonEditUsername.IsEnabled = true;
		}
		
		private string getUsernameByUsernameView(string usernameView)
		{
			return textBlockUser.Text.Substring(4);
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
			_controller.OpenFile(_messageAttachmentPath);
		}

		private void textBoxUserId_TextChanged(object sender, TextChangedEventArgs e)
		{
			if (!String.IsNullOrEmpty(textBoxUserId.Text)) textBoxUsername.Text = String.Empty;
		
			var userIdStr = textBoxUserId.Text;
			_controller.UpdateUserDataById(userIdStr);
		}

		private void textBoxUsername_TextChanged(object sender, TextChangedEventArgs e)
		{
			if (!String.IsNullOrEmpty(textBoxUsername.Text)) textBoxUserId.Text = String.Empty;

			var username = textBoxUsername.Text;
			_controller.UpdateUserDataByUsername(username);
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
	}
}