using Microsoft.EntityFrameworkCore;
using Messenger.Models;
using System;

namespace Messenger.Contexts
{
    public class MessengerContext : DbContext
    {
        public MessengerContext(DbContextOptions<MessengerContext> options)
            : base(options) { }
        public MessengerContext() { }
        public DbSet<User> Users { get; set; }
        public DbSet<PersonalInfo> PersonalInfos { get; set; }
        public DbSet<Conversation> Conversations { get; set; }
        public DbSet<UserConversation> UserConversations { get; set; }
        public DbSet<Message> Messages { get; set; }
		public DbSet<Announcement> Announcements { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<User>()
                .ToTable("User");               
            modelBuilder.Entity<User>()
                .Property(u => u.Login)
                .IsRequired();                
            modelBuilder.Entity<User>()
                .Property(u => u.Password)
                .IsRequired();
            modelBuilder.Entity<User>()
                .Property(u => u.RegistrationDate)
                .HasDefaultValue(DateTimeOffset.Now.ToUnixTimeMilliseconds());

            modelBuilder.Entity<PersonalInfo>().ToTable("PersonalInfo");

            modelBuilder.Entity<Conversation>().ToTable("Conversation");
            modelBuilder.Entity<Conversation>()
                .Property(c => c.CreationDate)
                .HasDefaultValue(DateTimeOffset.Now.ToUnixTimeMilliseconds());
            modelBuilder.Entity<Conversation>()
                .Property(c => c.Name)
                .HasDefaultValue("Conversation");
            modelBuilder.Entity<Conversation>()
                .Property(c => c.Name)
                .IsRequired();

            modelBuilder.Entity<UserConversation>().ToTable("UserConversation");
            modelBuilder.Entity<UserConversation>()
                .HasKey(x => new { x.UserId, x.ConversationId });

            modelBuilder.Entity<Message>().ToTable("Message");
            modelBuilder.Entity<Message>()
                .Property(m => m.Text)
                .IsRequired();
            modelBuilder.Entity<Message>()
                .Property(m => m.SendDate)
                .HasDefaultValue(DateTimeOffset.Now.ToUnixTimeMilliseconds());

			modelBuilder.Entity<Announcement>().ToTable("Announcement");
			modelBuilder.Entity<Announcement>()
				.Property(a => a.CreationDate)
				.IsRequired();
			modelBuilder.Entity<Announcement>()
				.Property(a => a.Description)
				.IsRequired();
			modelBuilder.Entity<Announcement>()
				.Property(a => a.IsActive)
				.IsRequired();
		}
    }
}