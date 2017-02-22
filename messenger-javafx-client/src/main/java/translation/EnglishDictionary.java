package translation;

public class EnglishDictionary extends Dictionary {

	@Override
	public String getChooseConversaion() {
		return "Choose conversation!";
	}
	@Override
	public String getEmptyMessage() {
		return "Cannot send empty message!";
	}
	@Override
	public String getNewConversation() {
		return "New Conversation";
	}
	@Override
	public String getNameOfTheConversation() {
		return "Name of the Conversation:";
	}
	@Override
	public String getNameOfTheConversationCannotBeEmpty() {
		return "Name of the conversation cannot be empty!";
	}
	@Override
	public String getSendFirstMessage() {
		return "Send first message in this conversation!";
	}
	@Override
	public String getNewAnnouncement() {
		return "New Announcement";
	}
	@Override
	public String getAnnouncementDescription() {
		return "Announcement description:";
	}
	@Override
	public String getAnnouncementDescriptionEmpty() {
		return "Announcement's description cannot be empty!";
	}
	@Override
	public String getMessage() {
		return "Message";
	}
	@Override
	public String getRemove() {
		return "Remove";
	}
}
