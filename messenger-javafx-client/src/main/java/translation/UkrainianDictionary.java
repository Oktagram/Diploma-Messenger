package translation;

public class UkrainianDictionary extends Dictionary {
	
	@Override
	public String getChooseConversaion() {
		return "Оберіть бесіду!";
	}
	@Override
	public String getEmptyMessage() {
		return "Неможливо відправити порожнє повідомлення!";
	}
	@Override
	public String getNewConversation() {
		return "Нова бесіда";
	}
	@Override
	public String getNameOfTheConversation() {
		return "Назва бесіди:";
	}
	@Override
	public String getNameOfTheConversationCannotBeEmpty() {
		return "Назва бесіди не може бути порожня!";
	}
	@Override
	public String getSendFirstMessage() {
		return "Відправте перше повідомлення у цю бесіду!";
	}
	@Override
	public String getNewAnnouncement() {
		return "Нове Оголошення";
	}
	@Override
	public String getAnnouncementDescription() {
		return "Опис оголошення:";
	}
	@Override
	public String getAnnouncementDescriptionEmpty() {
		return "Опис оголошення не може бути порожній!";
	}
	@Override
	public String getMessage() {
		return "Повідомлення";
	}
	@Override
	public String getRemove() {
		return "Видалити";
	}
}
