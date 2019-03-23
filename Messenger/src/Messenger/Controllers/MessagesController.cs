using System.Collections.Generic;
using Messenger.Models;
using Messenger.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Messenger.ViewModels;
using AutoMapper;
using System;
using Messenger.Core;
using Messenger.Paginations;
using Messenger.LogProvider;

namespace Messenger.Controllers
{
	[Route("api/[controller]")]
    public class MessagesController : Controller
    {
        private IMessageRepository _messageRepository;
        private IMessagePaginationService _messagePaginationService { get; set; }
		private readonly IEventLogRepository _logRepository;

        public MessagesController(IEventLogRepository eventLogRepository, IMessageRepository messageRepository, IMessagePaginationService messagePS)
        {
			_logRepository = eventLogRepository;
			_logRepository.LoggingEntity = LoggingEntity.MESSAGE;

            _messageRepository = messageRepository;
            _messagePaginationService = messagePS;
        }

        [Authorize]
        [HttpGet]
        public IActionResult GetAll()
        {           
            var messages = _messageRepository.GetAll();
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _messagePaginationService.MakePagination(messages, paginationInfo);

            Response.AddPagination(pagination.Header);

            var messagesVM = Mapper.Map<IEnumerable<Message>, IEnumerable<MessageViewModel>>(pagination.PageOfItems);
			return new OkObjectResult(messagesVM);
        }

        [Authorize]
        [HttpGet("{id}", Name = "GetMessage")]
        public IActionResult GetById(int id)
        {
            var message = _messageRepository.Find(id);

			if (message == null)
			{
				_logRepository.Add(LoggingEvents.GET_ITEM, $"Getting message by id {id}: Not Found.");
				return NotFound();
			}

            var messageVM = Mapper.Map<Message, MessageViewModel>(message);
            return new OkObjectResult(messageVM);
        }

        [Authorize]
        [HttpGet("byConversation/{id}")]
        public IActionResult GetByConversationId(int id)
        {
			var messages = _messageRepository.FindBy(m => m.ConversationId == id);
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _messagePaginationService.MakePagination(messages, paginationInfo);

            Response.AddPagination(pagination.Header);
			
            var messagesVM = Mapper.Map<IEnumerable<Message>, IEnumerable<MessageViewModel>>(pagination.PageOfItems);
            return new OkObjectResult(messagesVM);
        }

        [Authorize]
        [HttpPost]
        public IActionResult Create([FromBody] Message item)
        {
			if (item == null)
			{
				_logRepository.Add(LoggingEvents.CREATE_ITEM, $"Creating message: Bad Request.");
				return BadRequest();
			}

            item.SendDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            _messageRepository.Add(item);

			_logRepository.Add(LoggingEvents.CREATE_ITEM, $"Message {item.Id} created.");

			return CreatedAtRoute("GetMessage", new { Controller = "Messages", id = item.Id },
                Mapper.Map<Message, MessageViewModel>(item));
        }

        [Authorize]
        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] Message item)
        {
            if (item == null)
            {
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating message: Bad Request.");
				return BadRequest();
            }

            var messObj = _messageRepository.Find(id);

			if (messObj == null)
			{
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating message {id}: Not Found.");
				return NotFound();
			}

            _messageRepository.Update(id, messObj, item);
			_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Message {id} updated: [{item.Text}].");

			return new NoContentResult();
        }

        [Authorize]
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            _messageRepository.Remove(id);
			_logRepository.Add(LoggingEvents.DELETE_ITEM, $"Message {id} deleted.");
			
			return new NoContentResult();
        }
    }
}
