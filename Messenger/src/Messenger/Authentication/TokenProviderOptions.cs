using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Messenger.Authentication
{
    public class TokenProviderOptions
    {
        public string Path { get; set; } = "/api/token";

        public TimeSpan Expiration { get; set; } = TimeSpan.FromMinutes(150);

        public SigningCredentials SigningCredentials { get; set; }
    }
}
