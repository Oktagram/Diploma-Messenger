using System.Windows.Controls;
using System.Windows.Data;

namespace MessengerAdminPanel.Extensions
{
	public static class ListViewExtension
	{
		public static void HideColumnByHeader(this ListView listView, string header)
		{
			var gridView = (GridView)listView.View;

			foreach (var item in gridView.Columns)
			{
				if (item.Header.ToString().Equals(header))
				{
					gridView.Columns.Remove(item);
					return;
				}
			}
		}

		public static void ShowColumnByHeader(this ListView listView, string header, string bindingPath)
		{
			var gridView = (GridView)listView.View;

			var column = new GridViewColumn() {
				Header = header,
				DisplayMemberBinding = new Binding(bindingPath)
			};

			gridView.Columns.Insert(1, column);
		}
	}
}