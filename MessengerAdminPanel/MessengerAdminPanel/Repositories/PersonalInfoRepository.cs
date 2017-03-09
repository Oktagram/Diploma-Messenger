using MessengerAdminPanel.Repositories.Interfaces;

namespace MessengerAdminPanel.Repositories
{
	public class PersonalInfoRepository : EntityBaseRepository<PersonalInfo>, IPersonalInfoRepository
	{
		public PersonalInfoRepository(MessengerContext context)
			: base(context)
		{ }

		public void Update(int id, PersonalInfo persInfoObj, PersonalInfo item)
		{
			_context.Refresh();

			persInfoObj.FirstName = item.FirstName;
			persInfoObj.LastName = item.LastName;
			persInfoObj.PhoneNumber = item.PhoneNumber;
			persInfoObj.BirthDate = item.BirthDate;
			persInfoObj.Picture = item.Picture;
		}

		public void AddPicture(int id, string files)
		{
			_context.Refresh();

			var persInfo = Find(id);
			persInfo.Picture = files;
		}
	}
}
