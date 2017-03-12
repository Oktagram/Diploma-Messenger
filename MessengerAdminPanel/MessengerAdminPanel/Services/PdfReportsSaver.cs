using iTextSharp.text;
using iTextSharp.text.pdf;
using System.Collections;
using System.IO;

namespace MessengerAdminPanel.Services
{
	public class PdfReportsSaver : IReportsSaver
	{
		private const string _extension = ".pdf";

		public bool Save(string path, string name, IEnumerable data)
		{
			try
			{
				var doc = new Document();
				var suffix = 0;

				if (File.Exists($"{path}\\{name}{_extension}"))
					while (File.Exists($"{path}\\{name + ++suffix}{_extension}")) ;

				if (suffix != 0) name += suffix;

				var fs = new FileStream($"{path}\\{name}{_extension}", FileMode.Create);

				PdfWriter.GetInstance(doc, fs);

				doc.Open();

				foreach (var item in data)
					doc.Add(new Paragraph(item.ToString()));

				doc.Close();

				return true;
			}
			catch
			{
				return false;
			}
		}
	}
}