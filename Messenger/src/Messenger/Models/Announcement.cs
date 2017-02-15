namespace Messenger.Models
{
	public class Announcement : IEntityBase
	{
		public int Id { get; set; }
		public int UserId { get; set; }
		public string Description { get; set; }
		public long CreationDate { get; set; }
		public long ClosingDate { get; set; }
		public bool IsActive { get; set; }

		public User User { get; set; }
	}
}