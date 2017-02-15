using System.Collections.Generic;
using Messenger.Models;
using Messenger.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using AutoMapper;
using Messenger.ViewModels;
using System;
using System.Linq;
using Messenger.Core;
using Messenger.Paginations;

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace Messenger.Controllers
{
	[Route("api/[controller]")]
    public class ConversationsController : Controller
    {
        private IConversationRepository _converRepo;
        private IUserConversationRepository _userConvRepo;
        private IMessageRepository _messageRepo;
        private IConversationPaginationService _conversationPaginationService;

        public ConversationsController(IConversationRepository repo, IUserConversationRepository ucrepo,
            IMessageRepository messageRepo, IConversationPaginationService conversationPS)
        { 
            _converRepo = repo;
            _userConvRepo = ucrepo;
            _messageRepo = messageRepo;
            _conversationPaginationService = conversationPS;
        }

        [Authorize]
        [HttpGet]
        public IActionResult GetAll()
        {
            IEnumerable<Conversation> conversations = _converRepo.GetAll();
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _conversationPaginationService.MakePagination(conversations, paginationInfo);
            Response.AddPagination(pagination.Header);
            IEnumerable<ConversationViewModel> conversationsVM = Mapper.Map<IEnumerable<Conversation>, IEnumerable<ConversationViewModel>>(pagination.PageOfItems);
            return new OkObjectResult(conversationsVM);
        }

        [Authorize]
        [HttpGet("{id}", Name = "GetConversation")]
        public IActionResult GetById(int id)
        {
            var conv = _converRepo.Find(id);
            if (conv == null)
            {
                return NotFound();
            }
            ConversationViewModel conversationVM = Mapper.Map<Conversation, ConversationViewModel>(conv);
            return new OkObjectResult(conversationVM);
        }

        [Authorize]
        [HttpGet("byUser/{id}")]
        public IActionResult GetByUserId(int id)
        {
            List<int> userConvs = _userConvRepo.FindBy(uc => uc.UserId == id)
                                                      .Select(uc => uc.ConversationId)
                                                      .ToList();
            List<int> orderedConversations = _messageRepo.GetOrderedConversationIds()
                                                                .Intersect(userConvs)
                                                                .ToList();
            orderedConversations.AddRange(userConvs.Except(orderedConversations));
            IList<Conversation> conversations = new List<Conversation>();
            foreach (var convId in orderedConversations)
            {                
                conversations.Add(_converRepo.Find(convId));
            }
            IEnumerable<ConversationViewModel> conversationsVM = Mapper.Map<IEnumerable<Conversation>, IEnumerable<ConversationViewModel>>(conversations);
            return new OkObjectResult(conversationsVM);
        }

        [Authorize]
        [HttpPost]
        public IActionResult Create([FromBody] Conversation item)
        {
            if (item == null)
            {
                return BadRequest();
            }
            item.CreationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            _converRepo.Add(item);
            return CreatedAtRoute("GetConversation", new { Controller = "Conversation", id = item.Id },
                Mapper.Map<Conversation, ConversationViewModel>(item));
        }

        [Authorize]
        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] Conversation item)
        {
            if (item == null)
            {
                return BadRequest();
            }

            var convObj = _converRepo.Find(id);
            if (convObj == null)
            {
                return NotFound();
            }
            else
            {
                _converRepo.Update(id,convObj,item);
            }
            return new NoContentResult();
        }

        [Authorize]
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            _converRepo.Remove(id);
            return new NoContentResult();
        }
    }
}
