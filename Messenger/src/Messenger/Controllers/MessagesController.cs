using System.Collections.Generic;
using Messenger.Models;
using Messenger.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Messenger.ViewModels;
using AutoMapper;
using System.Linq;
using System;
using Messenger.Core;
using Microsoft.AspNetCore.Http;
using Messenger.Paginations;

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace Messenger.Controllers
{
    [Route("api/[controller]")]
    public class MessagesController : Controller
    {
        private IMessageRepository _messageRepository;
        private IMessagePaginationService _messagePaginationService { get; set; }

        public MessagesController(IMessageRepository messageRepository, IMessagePaginationService messagePS)
        {
            _messageRepository = messageRepository;
            _messagePaginationService = messagePS;
        }

        [Authorize]
        [HttpGet]
        public IActionResult GetAll()
        {           
            IEnumerable<Message> messages = _messageRepository.GetAll();
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _messagePaginationService.MakePagination(messages, paginationInfo);
            Response.AddPagination(pagination.Header);
            IEnumerable<MessageViewModel> messagesVM = Mapper.Map<IEnumerable<Message>, IEnumerable<MessageViewModel>>(pagination.PageOfItems);
            return new OkObjectResult(messagesVM);
        }

        [Authorize]
        [HttpGet("{id}", Name = "GetMessage")]
        public IActionResult GetById(int id)
        {
            Message message = _messageRepository.Find(id);
            if (message == null)
            {
                return NotFound();
            }
            MessageViewModel messageVM = Mapper.Map<Message, MessageViewModel>(message);
            return new OkObjectResult(messageVM);
        }

        [Authorize]
        [HttpGet("byConversation/{id}")]
        public IActionResult GetByConversationId(int id)
        {
            IEnumerable<Message> messages = _messageRepository.FindBy(m => m.ConversationId == id);
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _messagePaginationService.MakePagination(messages, paginationInfo);
            Response.AddPagination(pagination.Header);
            IEnumerable<MessageViewModel> messagesVM = Mapper.Map<IEnumerable<Message>, IEnumerable<MessageViewModel>>(pagination.PageOfItems);
            return new OkObjectResult(messagesVM);
        }

        [Authorize]
        [HttpPost]
        public IActionResult Create([FromBody] Message item)
        {
            if (item == null)
            {
                return BadRequest();
            }
            item.SendDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            _messageRepository.Add(item);
            return CreatedAtRoute("GetMessage", new { Controller = "Message", id = item.Id },
                Mapper.Map<Message, MessageViewModel>(item));
        }

        [Authorize]
        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] Message item)
        {
            if (item == null)
            {
                return BadRequest();
            }
            var messObj = _messageRepository.Find(id);
            if (messObj == null)
            {
                return NotFound();
            }
            else
            {
                _messageRepository.Update(id, messObj, item);
            }
            return new NoContentResult();
        }

        [Authorize]
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            _messageRepository.Remove(id);
            return new NoContentResult();
        }
    }
}
