using AutoMapper;
using Messenger.LogProvider;
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
		private readonly IEventLogRepository _logRepository;
		
		public AnnouncementsController(IEventLogRepository eventLogRepository, IAnnouncementRepository announcementRepository)
		{
			_logRepository = eventLogRepository;
			_logRepository.LoggingEntity = LoggingEntity.ANNOUNCEMENT;

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
				_logRepository.Add(LoggingEvents.CREATE_ITEM, $"Creating announcement: Bad Request.");
				return BadRequest();
			}

			item.CreationDate = DateTimeOffset.Now.ToUnixTimeMilliseconds();
			item.IsActive = true;
			item.Description.Trim();
			
			_announcementRepository.Add(item);
			_logRepository.Add(LoggingEvents.CREATE_ITEM, $"Announcement {item.Id} [{item.Description}] created.");

			return new OkObjectResult(_announcementRepository.Find(item.Id));
		}

		[HttpPut("{id}")]
		public IActionResult Update(int id, [FromBody] Announcement item)
		{
			if (item == null)
			{
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating announcement {id}: Bad Request.");
				return BadRequest();
			}
			
			var announcement = _announcementRepository.Find(id);

			if (announcement == null)
			{
				_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Updating announcement {id}: Not Found.");
				return NotFound();
			}
			else _announcementRepository.Update(id, announcement, item);
			
			var temp = _announcementRepository.Find(id);

			_logRepository.Add(LoggingEvents.UPDATE_ITEM, $"Announcement {id} updated: {temp}.");

			return new OkObjectResult(temp);
		}
	}
}