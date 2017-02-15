using AutoMapper;
using Messenger.Models;
using Messenger.Repositories;
using Messenger.ViewModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Messenger.Controllers
{
	[Authorize]
	[Route("api/[controller]")]
	public class AnnouncementsController : Controller
	{
		private IAnnouncementRepository _announcementRepository;
		
		public AnnouncementsController(IAnnouncementRepository announcementRepository)
		{
			_announcementRepository = announcementRepository;
		}
		
		[HttpGet]
		public IActionResult GetAll()
		{
			var announcements = _announcementRepository.GetAll();

			var mappedAnnouncements = Mapper.Map<IEnumerable<Announcement>, IEnumerable<AnnouncementViewModel>>(announcements);
			var orderderedAnnouncements = mappedAnnouncements.OrderByDescending(a => a.ClosingDate);

			return new OkObjectResult(orderderedAnnouncements);
		}
	
		[HttpPost]
		public IActionResult Create([FromBody] Announcement item)
		{
			if (item == null || string.IsNullOrEmpty(item.Description) || item.UserId == 0)
			{
				return BadRequest();
			}

			item.CreationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
			item.IsActive = true;
			item.Description.Trim();

			_announcementRepository.Add(item);
			
			return new OkObjectResult(_announcementRepository.Find(item.Id));
		}

		[HttpPut("{id}")]
		public IActionResult Update(int id, [FromBody] Announcement item)
		{
			if (item == null) return BadRequest();
			
			var announcement = _announcementRepository.Find(id);

			if (announcement == null) return NotFound();
			else _announcementRepository.Update(id, announcement, item);

			return new OkObjectResult(_announcementRepository.Find(item.Id));
		}
	}
}