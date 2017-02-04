using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Messenger.Models;
using Messenger.Repositories;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace Messenger.Controllers
{
    [Route("api/[controller]")]
    public class RegistrationController : Controller
    {
        private IUserRepository _userRepository;
        private IPersonalInfoRepository _persInfoRepository;
        private readonly Regex _regexPassword = new Regex(@"^(?=.*[a-z])(?=.*\d).{8,15}$");
        private readonly Regex _regexLogin = new Regex(@"^^(?=[A-Za-z0-9])(?!.*[._()\[\]-]{2})[A-Za-z0-9._()\[\]-]{3,15}$");
        private readonly Regex _regexEmail = new Regex(@"^[\w!#$%&'*+\-/=?\^_`{|}~]+(\.[\w!#$%&'*+\-/=?\^_`{|}~]+)*"
                                                       + "@"+ @"((([\-\w]+\.)+[a-zA-Z]{2,4})|(([0-9]{1,3}\.){3}[0-9]{1,3}))$");

        public RegistrationController(IUserRepository userRepository, IPersonalInfoRepository persInfoRepository)
        {
            _userRepository = userRepository;
            _persInfoRepository = persInfoRepository;
        }

        [HttpPost]
        public IActionResult Registration([FromBody] User item)
        {
            if (item == null)
            {
                return BadRequest("Empty fields!");
            }

            if (item.Login == null)
            {
                return BadRequest("Empty login field!");
            }

            if (item.Login.Length > 30)
            {
                return BadRequest("Login must have maximum 30 chars!");
            }
            else
            {
                Match matchLogin = _regexLogin.Match(item.Login);
                if (_userRepository.GetSingle(user => user.Login.Equals(item.Login)) != null)
                {
                    return BadRequest("User with this login already exist!");
                }
                if (!matchLogin.Success)
                {
                    return BadRequest("Invalid login!");
                }
            }

            if (item.Password == null)
            {
                return BadRequest("Empty password field!");
            }
            else
            {
                Match matchPassword = _regexPassword.Match(item.Password);
                if (item.Password.Length < 7)
                {
                    return BadRequest("Password should be not less 8 characters!");
                }
                if (!matchPassword.Success)
                {
                    return BadRequest("Password must have letters and numbers!");
                }
            }
            if (item.Email == null)
            {
                return BadRequest("Invalid email!");
            }
            else
            {
                Match matchEmail = _regexEmail.Match(item.Email);
                if (!matchEmail.Success)
                {
                    return BadRequest("Invalid email!");
                }
            }

            item.Password = HashService.GetHashString(item.Password);
            item.RegistrationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            _userRepository.Add(item);

            var info = new PersonalInfo()
            {
                UserId = item.Id
            };

            _persInfoRepository.Add(info);
            return Content("Success registration : " + item.Login);
        }      
    }
}
