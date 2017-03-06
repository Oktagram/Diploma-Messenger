namespace MessengerAdminPanel.UnitOfWork
{
	public class UnitOfWorkFactory
	{
		public static UnitOfWork Create(MessengerContext context)
		{
			return new UnitOfWork(context);
		}
	}
}