using System.Collections.Generic;
using Messenger.Models;
using Messenger.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using Messenger.ViewModels;
using AutoMapper;
using Messenger.Core;
using System;
using System.Linq;
using Messenger.Paginations;
using Messenger.LogProvider;

namespace Messenger.Controllers
{
    [Route("api/[controller]")]
    public class PersonalInfoController : Controller
    {
        private IPersonalInfoRepository _personalInfoRepo;
        private IPersonalInfoPaginationService _personalInfoPaginationService;
		private readonly IEventLogRepository _logRepository;

		public PersonalInfoController(IEventLogRepository eventLogRepository, IPersonalInfoRepository _repo, IPersonalInfoPaginationService personalInfoPS)
        {
			_logRepository = eventLogRepository;
			_logRepository.LoggingEntity = LoggingEntity.PERSONAL_INFO;

            _personalInfoRepo = _repo;
            _personalInfoPaginationService = personalInfoPS;
        }

        [Authorize]
        [HttpGet]
        public IActionResult GetAll()
        {
            var personalInfos = _personalInfoRepo.GetAll();
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _personalInfoPaginationService.MakePagination(personalInfos, paginationInfo);

			Response.AddPagination(pagination.Header);

			var persInfosVM = Mapper.Map<IEnumerable<PersonalInfo>, IEnumerable<PersonalInfoViewModel>>(pagination.PageOfItems);

			return new OkObjectResult(persInfosVM);
        }

        [Authorize]
        [HttpGet("{id}", Name = "GetPersonalInfo")]
        public IActionResult GetById(int id)
        {
            var persInfo = _personalInfoRepo.Find(id);

			if (persInfo == null)
			{
				_logRepository.Add(LoggingEvents.GET_ITEM, $"Getting personal info by id {id}: Not Found.");
				return NotFound();
			}

			var persInfoVM = Mapper.Map<PersonalInfo, PersonalInfoViewModel>(persInfo);

			return new OkObjectResult(persInfoVM);
        }

        [Authorize]
        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] PersonalInfo item)
        {
			if (item == null)
			{
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating personal info {id}: Bad Request.");
				return BadRequest();
			}

            var persInfoObj = _personalInfoRepo.Find(id);

			if (persInfoObj == null)
			{
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating personal info {id}: Not Found.");
				return NotFound();
			}

            _personalInfoRepo.Update(id, persInfoObj, item);
			_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Personal info {id} updated: {item}.");

            return new NoContentResult();
        }

        [Authorize]
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            _personalInfoRepo.Remove(id);
			_logRepository.Add(LoggingEvents.DELETE_ITEM, $"Personal info {id} deleted.");

            return new NoContentResult();
        }
    }
}
