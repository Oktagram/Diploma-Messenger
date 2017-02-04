using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.Core
{
    public static class Extensions
    {    
        public static void AddApplicationError(this HttpResponse response, string message)
        {
            response.Headers.Add("Application-Error", message);
            // CORS
            response.Headers.Add("access-control-expose-headers", "Application-Error");
        }

        public static void AddPagination(this HttpResponse response, PaginationHeader paginationHeader)
        {           
            response.Headers.Add("Pagination",
                Newtonsoft.Json.JsonConvert.SerializeObject(paginationHeader));
            // CORS
            response.Headers.Add("access-control-expose-headers", "Pagination");
        }
    }
}
