using Messenger.Contexts;
using Messenger.Models;
using System;
using System.Linq;

namespace Messenger
{
    public static class DbInitializer
    {
        public static void Initialize(MessengerContext context)
        {
            context.Database.EnsureCreated();

            // Look for any users.
            if (context.Users.Any())
            {
                return;   // DB has been seeded
            }           

            var users = new User[]
            {
                new User{ Login="Ananas", Password="1234", Email="aa@i.ua", RegistrationDate = 345435343, IsOnline = false, IsAdmin = true},
                new User{ Login="Orange", Password="qwerty", Email="aa@i.ua", RegistrationDate = 54236324532, IsOnline = false, IsAdmin = true},
                new User{ Login="Pineapple", Password="pass", Email="aa@i.ua", RegistrationDate = 542354325423, IsOnline = false, IsAdmin = false},
            };
            foreach (User u in users)
            {
                u.Password = HashService.GetHashString(u.Password);
                context.Users.Add(u);
            }
            context.SaveChanges();

            var persInfos = new PersonalInfo[]
            {
                new PersonalInfo(){ FirstName = "Petro", LastName= "AAA", UserId = 1},
                new PersonalInfo(){ FirstName = "Ivan", LastName= "asd", UserId = 2},
                new PersonalInfo(){ FirstName = "Oleksandr", LastName= "Ods", UserId = 3},
            };
            foreach (PersonalInfo pi in persInfos)
            {
                context.PersonalInfos.Add(pi);
            }
            context.SaveChanges();

            var conversations = new Conversation[]
            {
                new Conversation(){ Name = "Football", CreationDate = 543246575},
                new Conversation(){ Name = "Eleks", CreationDate = 123456784},
            };
            foreach (Conversation c in conversations)
            {
                context.Conversations.Add(c);
            }
            context.SaveChanges();

            var userConversations = new UserConversation[]
            {
                new UserConversation(){ UserId = 1, ConversationId = 1},
                new UserConversation(){ UserId = 2, ConversationId = 1},
                new UserConversation(){ UserId = 3, ConversationId = 1},
                new UserConversation(){ UserId = 2, ConversationId = 2},
                new UserConversation(){ UserId = 3, ConversationId = 2},
            };
            foreach (UserConversation uc in userConversations)
            {
                context.UserConversations.Add(uc);
            }
            context.SaveChanges();

            var messages = new Message[]
            {
                new Message(){ UserId = 1, ConversationId = 1, Text = "asdf", SendDate = 789524},
                new Message(){ UserId = 2, ConversationId = 1, Text = "aaaa", SendDate = 456},
                new Message(){ UserId = 1, ConversationId = 1, Text = "bvnc", SendDate = 458963},
                new Message(){ UserId = 3, ConversationId = 2, Text = "text", SendDate = 789321},
            };
            foreach (Message m in messages)
            {
                context.Messages.Add(m);
            }

			var announcements = new Announcement[]
			{
				new Announcement() {
					Description = "This is the first announcement.",
					User = users[0],
					IsActive = true,
					CreationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds()
				},
				new Announcement() {
					Description = "This is the second announcement.",
					User = users[1],
					IsActive = true,
					CreationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds()
				}
			};
			foreach (var announcement in announcements)
			{
				context.Announcements.Add(announcement);
			}

			context.SaveChanges();
        }
    }
}
