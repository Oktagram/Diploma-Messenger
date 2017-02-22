using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Messenger.Models;
using Messenger.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using Messenger.ViewModels;
using AutoMapper;

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace Messenger.Controllers
{
    [Route("api/[controller]")]
    public class UserConversationController : Controller
    {
        private IUserConversationRepository _userConverRepo;
        private IConversationRepository _converRepo;

        public UserConversationController(IUserConversationRepository ucrepo, IConversationRepository crepo)
        {
            _userConverRepo = ucrepo;
            _converRepo = crepo;
        }

        [Authorize]
        [HttpPost]
        public IActionResult Create([FromBody]UserConversation item)
        {
            if (item == null ||
                _userConverRepo.GetSingle(uc => uc.UserId == item.UserId && uc.ConversationId == item.ConversationId) != null)
            {
                return BadRequest();
            }        

            _userConverRepo.Add(item);

            return new OkObjectResult(Mapper.Map<UserConversation, UserConversationViewModel>(item));
        }

        [Authorize]
        [HttpGet]
        public IActionResult GetAll()
        {
            IEnumerable<UserConversation> userConvs = _userConverRepo.GetAll();
            IEnumerable<UserConversationViewModel> userConvsVM = Mapper.Map<IEnumerable<UserConversation>, IEnumerable<UserConversationViewModel>>(userConvs);
            return new OkObjectResult(userConvsVM);
        }

        [Authorize]
        [HttpGet("byUserId/{id}")]
        public IActionResult GetByUserId(int id)
        {
            IEnumerable<UserConversation> userConvs = _userConverRepo.FindBy(u => u.UserId == id);
            IEnumerable<UserConversationViewModel> userConvsVM = Mapper.Map<IEnumerable<UserConversation>, IEnumerable<UserConversationViewModel>>(userConvs);
            return new OkObjectResult(userConvsVM);
        }

        [Authorize]
        [HttpGet("byConversationId/{id}")]
        public IActionResult GetByConversationId(int id)
        {
            IEnumerable<UserConversation> userConvs = _userConverRepo.FindBy(u => u.ConversationId == id);
            IEnumerable<UserConversationViewModel> userConvsVM = Mapper.Map<IEnumerable<UserConversation>, IEnumerable<UserConversationViewModel>>(userConvs);
            return new OkObjectResult(userConvsVM);
        }

        [Authorize]
        [HttpDelete("{userId}/{convId}")]
        public IActionResult Delete(int userId,int convId)
        {
            _userConverRepo.DeleteByIds(userId, convId);
            if(!_userConverRepo.FindBy(u => u.ConversationId == convId).Any())
            {
                _converRepo.Remove(convId);
            }
            return new NoContentResult();
        }
    }
}
