using Messenger.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.Core
{
    public class Pagination<T> where T : class, IEntityBase, new()
    {
        public PaginationHeader Header { get; set; }
        public IEnumerable<T> PageOfItems { get; set; }

        public Pagination(IEnumerable<T> pageOfItems, PaginationHeader header)
        {           
            PageOfItems = pageOfItems;
            Header = header;
        }

        public Pagination(IEnumerable<T> pageOfItems, int currentPage, int itemsPerPage, int totalItems, int totalPages)
        {
            PageOfItems = pageOfItems;
            Header = new PaginationHeader(currentPage, itemsPerPage, totalItems, totalPages);                      
        }
    }
}
