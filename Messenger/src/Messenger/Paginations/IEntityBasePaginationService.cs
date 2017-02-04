using Messenger.Core;
using Messenger.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.Paginations
{
    public interface IEntityBasePaginationService<T> where T : class, IEntityBase, new()
    {
        Pagination<T> MakePagination(IEnumerable<T> items, string pagination);
    }
}
