using System.Collections.Generic;
using System.Windows.Controls;
using System.Windows.Data;

namespace MessengerAdminPanel.Extensions
{
	public static class ListViewExtension
	{
		public static void AddColumnsByHeaders(this ListView listView, Dictionary<string, string> headersWithPath)
		{
			foreach (var pair in headersWithPath)
			{
				listView.AddColumnByHeader(pair.Key, pair.Value);
			}
		}

		public static void RemoveColumnsByHeaders(this ListView listView, List<string> headers)
		{
			foreach (var header in headers)
			{
				listView.RemoveColumnByHeader(header);
			}
		}

		public static void RemoveColumnByHeader(this ListView listView, string header)
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

		public static void AddColumnByHeader(this ListView listView, string header, string bindingPath)
		{
			var gridView = (GridView)listView.View;

			var column = new GridViewColumn() {
				Header = header,
				DisplayMemberBinding = new Binding(bindingPath)
			};

			gridView.Columns.Add(column);
		}
	}
}