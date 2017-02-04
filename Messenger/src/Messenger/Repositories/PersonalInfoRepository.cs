using System;
using Messenger.Contexts;
using Messenger.Models;

namespace Messenger.Repositories
{
    public class PersonalInfoRepository : EntityBaseRepository<PersonalInfo>, IPersonalInfoRepository
    {
        public PersonalInfoRepository(MessengerContext context)
            : base(context)
        { }

        public void Update(int id, PersonalInfo persInfoObj, PersonalInfo item)
        {
            persInfoObj.FirstName = item.FirstName;
            persInfoObj.LastName = item.LastName;
            persInfoObj.PhoneNumber = item.PhoneNumber;
            persInfoObj.BirthDate = item.BirthDate;
            persInfoObj.Picture = item.Picture;
            Commit();
        }

        public void AddPicture(int id, string files)
        {
            PersonalInfo persInfo = Find(id);
            persInfo.Picture = files;
            Commit();
        }
    }
}
