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
using Messenger.LogProvider;

namespace Messenger.Controllers
{
	[Route("api/[controller]")]
    public class ConversationsController : Controller
    {
        private IConversationRepository _converRepo;
        private IUserConversationRepository _userConvRepo;
        private IMessageRepository _messageRepo;
        private IConversationPaginationService _conversationPaginationService;
		private readonly IEventLogRepository _logRepository;
		
		public ConversationsController(IEventLogRepository eventLogRepository, IConversationRepository repo, IUserConversationRepository ucrepo,
            IMessageRepository messageRepo, IConversationPaginationService conversationPS)
        {
			_logRepository = eventLogRepository;
			_logRepository.LoggingEntity = LoggingEntity.CONVERSATION;

            _converRepo = repo;
            _userConvRepo = ucrepo;
            _messageRepo = messageRepo;
            _conversationPaginationService = conversationPS;
        }

        [Authorize]
        [HttpGet]
        public IActionResult GetAll()
        {
            var conversations = _converRepo.GetAll();
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _conversationPaginationService.MakePagination(conversations, paginationInfo);

            Response.AddPagination(pagination.Header);
			
            var conversationsVM = Mapper.Map<IEnumerable<Conversation>, IEnumerable<ConversationViewModel>>(pagination.PageOfItems);
            return new OkObjectResult(conversationsVM);
        }

        [Authorize]
        [HttpGet("{id}", Name = "GetConversation")]
        public IActionResult GetById(int id)
        {
            var conv = _converRepo.Find(id);

			if (conv == null)
			{
				_logRepository.Add(LoggingEvents.GET_ITEM, $"Getting conversation by id {id}: Not Found.");
				return NotFound();
			}
			
            var conversationVM = Mapper.Map<Conversation, ConversationViewModel>(conv);
            return new OkObjectResult(conversationVM);
        }

        [Authorize]
        [HttpGet("byUser/{id}")]
        public IActionResult GetByUserId(int id)
        {
            var userConvs = _userConvRepo.FindBy(uc => uc.UserId == id)
                                                      .Select(uc => uc.ConversationId)
                                                      .ToList();

			var orderedConversations = _messageRepo.GetOrderedConversationIds()
                                                                .Intersect(userConvs)
                                                                .ToList();

			var conversations = new List<Conversation>();

			orderedConversations.AddRange(userConvs.Except(orderedConversations));
			
            foreach (var convId in orderedConversations)
            {                
                conversations.Add(_converRepo.Find(convId));
            }
			
            var conversationsVM = Mapper.Map<IEnumerable<Conversation>, IEnumerable<ConversationViewModel>>(conversations);
            return new OkObjectResult(conversationsVM);
        }

        [Authorize]
        [HttpPost]
        public IActionResult Create([FromBody] Conversation item)
        {
			if (item == null)
			{
				_logRepository.Add(LoggingEvents.CREATE_ITEM, $"Creating conversation: Bad Request.");
				return BadRequest();
			}

            item.CreationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            _converRepo.Add(item);

			_logRepository.Add(LoggingEvents.CREATE_ITEM, $"Convnersation {item.Id} [{item.Name}] created.");

            return CreatedAtRoute("GetConversation", new { Controller = "Conversations", id = item.Id },
                Mapper.Map<Conversation, ConversationViewModel>(item));
        }

        [Authorize]
        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] Conversation item)
        {
			if (item == null)
			{
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating announcement {item.Id}: Bad Request.");
				return BadRequest();
			}

            var convObj = _converRepo.Find(id);

			if (convObj == null)
			{
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating announcement {item.Id}: Not Found.");
				return NotFound();
			}

            _converRepo.Update(id,convObj,item);
			_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Conversation {id} [{convObj.Name}] to [{item.Name}] updated.");
			
			return new NoContentResult();
        }

        [Authorize]
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            _converRepo.Remove(id);
			_logRepository.Add(LoggingEvents.DELETE_ITEM, $"Conversation {id} deleted.");

            return new NoContentResult();
        }
    }
}
