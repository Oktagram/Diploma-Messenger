using AutoMapper;
using Messenger.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.ViewModels.Mappings
{
    public class MappingProfile : Profile
    {
        public MappingProfile()
        {
            CreateMap<Message, MessageViewModel>();
            CreateMap<User, UserViewModel>();
            CreateMap<Conversation, ConversationViewModel>();
            CreateMap<PersonalInfo, PersonalInfoViewModel>();
            CreateMap<UserConversation, UserConversationViewModel>();
        }
    }
}
