namespace MessengerAdminPanel.Repositories.Interfaces
{
	public interface IPersonalInfoRepository : IEntityBaseRepository<PersonalInfo>
	{
		void Update(int id, PersonalInfo messObj, PersonalInfo item);
		void AddPicture(int id, string files);
	}
}
