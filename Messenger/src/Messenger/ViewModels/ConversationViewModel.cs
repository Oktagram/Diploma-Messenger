using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.ViewModels
{
    public class ConversationViewModel
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public long CreationDate { get; set; }
    }
}
