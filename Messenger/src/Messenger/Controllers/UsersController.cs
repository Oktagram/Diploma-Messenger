using Messenger.Models;
using Messenger.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using Messenger.ViewModels;
using AutoMapper;
using System;
using System.IdentityModel.Tokens.Jwt;
using Messenger.Core;
using System.Linq;
using Messenger.Paginations;
using Messenger.LogProvider;

namespace Messenger.Controllers
{
	[Route("api/[controller]")]
    public class UsersController : Controller
    {
        private IUserRepository _userRepo;
        private IUserConversationRepository _userConvRepo;
        private IUserPaginationService _userPaginationService;
		private readonly IEventLogRepository _logRepository;

        public UsersController(IEventLogRepository eventLogRepository, IUserRepository repo, IUserConversationRepository ucrepo, IUserPaginationService userPS)
        {
			_logRepository = eventLogRepository;
			_logRepository.LoggingEntity = LoggingEntity.USER;

            _userRepo = repo;
            _userConvRepo = ucrepo;
            _userPaginationService = userPS;
        }

        [Authorize]
        [HttpGet("identifyUser")]
        public IActionResult IdentifyUser()
        {
            var authenticationHeader = Request.Headers["Authorization"];
            var token = authenticationHeader.FirstOrDefault().Split(' ')[1];
            var jwt = new JwtSecurityToken(token);
            var subject = jwt.Subject;
            var user = _userRepo.GetSingle(use => use.Login.Equals(subject));

			if (user == null)
			{
				_logRepository.Add(LoggingEvents.GET_ITEM, $"Authorizing user {user.Id}: Not Found.");
				return NotFound();
			}

			_logRepository.Add(LoggingEvents.GET_ITEM, $"User {user.Id} [{user.Login}] authorized.");

            var userVM = Mapper.Map<User, UserViewModel>(user);
            return new OkObjectResult(userVM);
        }

        [Authorize]
        [HttpGet]
        public IActionResult GetAll()
        {
            var users = _userRepo.GetAll();
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _userPaginationService.MakePagination(users, paginationInfo);

			Response.AddPagination(pagination.Header);

			var usersVM = Mapper.Map<IEnumerable<User>, IEnumerable<UserViewModel>>(pagination.PageOfItems);
			return new OkObjectResult(usersVM);
        }

        [Authorize]
        [HttpGet("{id}", Name = "GetUser")]
        public IActionResult GetById(int id)
        {
            var user = _userRepo.Find(id);

			if (user == null)
			{
				_logRepository.Add(LoggingEvents.GET_ITEM, $"Getting user by id {id}: Not Found.");
				return NotFound();
			}

            var userVM = Mapper.Map<User, UserViewModel>(user);
            return new OkObjectResult(userVM);
        }

        [Authorize]
        [HttpGet("byConversation/{id}")]
        public IActionResult GetByConversationId(int id)
        {
            var userConvs = _userConvRepo.FindBy(u => u.ConversationId == id);
            var users = new List<User>();

            foreach (var uc in userConvs)
            {
                users.Add(_userRepo.Find(uc.UserId));
            }

            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _userPaginationService.MakePagination(users, paginationInfo);

            Response.AddPagination(pagination.Header);
			
            var usersVM = Mapper.Map<IEnumerable<User>, IEnumerable<UserViewModel>>(pagination.PageOfItems);
            return new OkObjectResult(usersVM);
        }

        [Authorize]
        [HttpGet("{id}/friends")]
        public IActionResult GetFriends(int id)
        {
            var users = new List<User>();
            var friends = _userConvRepo.FindFriends(id);

            foreach (var friend in friends)
            {
                users.Add(_userRepo.Find(friend));
            }

            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _userPaginationService.MakePagination(users, paginationInfo);

            Response.AddPagination(pagination.Header);
			
            var usersVM = Mapper.Map<IEnumerable<User>, IEnumerable<UserViewModel>>(pagination.PageOfItems);
            return new OkObjectResult(usersVM);
        }

        [Authorize]
        [HttpGet("find/{login}")]
        public IActionResult FindUser(string login)
        {
            var users = _userRepo.FindBy(u => u.Login.Contains(login));
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _userPaginationService.MakePagination(users, paginationInfo);

            pagination.PageOfItems = pagination.PageOfItems.OrderBy(u => u.Login.Length);
            Response.AddPagination(pagination.Header);

			_logRepository.Add(LoggingEvents.GET_ITEM, $"Searched for user {login}.");

            var usersVM = Mapper.Map<IEnumerable<User>, IEnumerable<UserViewModel>>(pagination.PageOfItems);
            return new OkObjectResult(usersVM);
        }

        [Authorize]
        [HttpPost]
        public IActionResult Create([FromBody] User item)
        {
			if (item == null)
			{
				_logRepository.Add(LoggingEvents.CREATE_ITEM, $"Creating user: Bad Request.");
				return BadRequest();
			}

            item.RegistrationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            item.Password = HashService.GetHashString(item.Password);
            _userRepo.Add(item);

			_logRepository.Add(LoggingEvents.CREATE_ITEM, $"User {item.Id} [{item.Login}] created.");

			return CreatedAtRoute("GetUser", new { Controller = "User", id = item.Id }, 
                Mapper.Map<User, UserViewModel>(item));
        }

        [Authorize]
        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] User item)
        {
			if (item == null)
			{
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating user {id}: Bad Request.");
				return BadRequest();
			}
        
            var userObj = _userRepo.Find(id);

			if (userObj == null)
			{
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating user {id}: Not Found.");
				return NotFound();
			}

			if (item.Password != null) item.Password = HashService.GetHashString(item.Password);

			_userRepo.Update(id, userObj, item);
			_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"User {id} [{userObj.Login}] updated.");

            return new NoContentResult();
        }

        [Authorize]
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            _userRepo.Remove(id);
			_logRepository.Add(LoggingEvents.DELETE_ITEM, $"User {id} deleted.");

            return new NoContentResult();
        }
    }
}
