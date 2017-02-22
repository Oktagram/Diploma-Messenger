namespace Messenger.Models
{
	public class EventLog
    {
		public int Id { get; set; }
		public int? EventId { get; set; }
		public int? EntityId { get; set; }
		public string Message { get; set; }
		public long? CreatedTime { get; set; }
	}
}
