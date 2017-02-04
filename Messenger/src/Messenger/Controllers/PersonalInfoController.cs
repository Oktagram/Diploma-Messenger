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

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace Messenger.Controllers
{
    [Route("api/[controller]")]
    public class PersonalInfoController : Controller
    {
        private IPersonalInfoRepository _personalInfoRepo;
        private IPersonalInfoPaginationService _personalInfoPaginationService;

        public PersonalInfoController(IPersonalInfoRepository _repo, IPersonalInfoPaginationService personalInfoPS)
        {
            _personalInfoRepo = _repo;
            _personalInfoPaginationService = personalInfoPS;
        }

        [Authorize]
        [HttpGet]
        public IActionResult GetAll()
        {
            IEnumerable<PersonalInfo>personalInfos = _personalInfoRepo.GetAll();
            var paginationInfo = Request.Headers["Pagination"];
            var pagination = _personalInfoPaginationService.MakePagination(personalInfos, paginationInfo);
            Response.AddPagination(pagination.Header);
            IEnumerable<PersonalInfoViewModel> persInfosVM = Mapper.Map<IEnumerable<PersonalInfo>, IEnumerable<PersonalInfoViewModel>>(pagination.PageOfItems);
            return new OkObjectResult(persInfosVM);
        }

        [Authorize]
        [HttpGet("{id}", Name = "GetPersonalInfo")]
        public IActionResult GetById(int id)
        {
            var persInfo = _personalInfoRepo.Find(id);
            if (persInfo == null)
            {
                return NotFound();
            }
            PersonalInfoViewModel persInfoVM = Mapper.Map<PersonalInfo, PersonalInfoViewModel>(persInfo);
            return new OkObjectResult(persInfoVM);
        }

        [Authorize]
        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] PersonalInfo item)
        {

            if (item == null)
            {
                return BadRequest();
            }
            var persInfoObj = _personalInfoRepo.Find(id);
            if (persInfoObj == null)
            {
                return NotFound();
            }
            else
            {
                _personalInfoRepo.Update(id, persInfoObj, item);
            }
            return new NoContentResult();
        }

        [Authorize]
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            _personalInfoRepo.Remove(id);
            return new NoContentResult();
        }
    }
}
