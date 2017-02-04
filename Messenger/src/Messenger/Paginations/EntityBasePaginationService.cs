using Messenger.Contexts;
using Messenger.Core;
using Messenger.Models;
using Messenger.Paginations;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.Controllers
{
    public class EntityBasePaginationService<T> : IEntityBasePaginationService<T> where T : class, IEntityBase, new()
    {
        private Pagination<T> _pagination;
        int page = 1;
        int pageSize = 10;

        public Pagination<T> MakePagination(IEnumerable<T> items, string pagination)
        {
            int currentPage;
            int currentPageSize;
            if (!string.IsNullOrEmpty(pagination))
            {
                try
                {
                    string[] vals = pagination.ToString().Split(',');
                    int.TryParse(vals[0], out page);
                    int.TryParse(vals[1], out pageSize);
                }
                catch { }
                currentPageSize = pageSize;
            }
            else
            {
                currentPageSize = items.Count();
            }

            currentPage = page;
            var totalitems = items.Count();
            var totalPages = (int)Math.Ceiling((double)totalitems / pageSize);
            
            items = items.OrderByDescending(i => i.Id)
                         .Skip((currentPage - 1) * currentPageSize)
                         .Take(currentPageSize)
                         .ToList();

            _pagination = new Pagination<T>(items, page, pageSize, totalitems, totalPages);

            return _pagination;
        }
    }
}
