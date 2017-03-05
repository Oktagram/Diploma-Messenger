using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using System.IO;
using Messenger.Repositories;
using Microsoft.AspNetCore.Authorization;
using Messenger.LogProvider;

namespace Messenger.Controllers
{
	[Route("api/[controller]")]
    public class FilesController : Controller
    {
        private IHostingEnvironment _env;
        private IMessageRepository _messageRepository;
        private IPersonalInfoRepository _persInfoRepository;
		private readonly IEventLogRepository _logRepository;

        public FilesController(IEventLogRepository eventLogRepository, IHostingEnvironment env, IMessageRepository messageRepository, IPersonalInfoRepository persInfoRepository)
        {
			_logRepository = eventLogRepository;
			_logRepository.LoggingEntity = LoggingEntity.FILE;

            _env = env;
            _messageRepository = messageRepository;
            _persInfoRepository = persInfoRepository;
        }

        [HttpGet]
        [Route("downloadAttachment/{messageId}")]
        public IActionResult DownloadAttachment(int messageId)
        {
            var message = _messageRepository.Find(messageId);

            try
            {
				var result = DownloadFile(message.Attachment);

				_logRepository.Add(LoggingEvents.DOWNLOAD, $"Downloaded attachment \"{message.Attachment}\".");

				return result;
            }
            catch
            {
				_logRepository.Add(LoggingEvents.DOWNLOAD, $"Downloading atachment {messageId}: Not Found.");
				return NotFound();
            }                      
        }

        
        [HttpGet]
        [Route("downloadPicture/{personalInfoId}")]
        public IActionResult DownloadPicture(int personalInfoId)
        {
            var persInfo = _persInfoRepository.Find(personalInfoId);
            try
            {
				var result = DownloadFile(persInfo.Picture);
				return result;
            }
            catch
            {
				_logRepository.Add(LoggingEvents.DOWNLOAD, $"Downloading profile picture {personalInfoId}: Not Found.");
				return NotFound();
            }
        }

        [Authorize]
        [HttpPost]
        [Route("uploadAttachment/{messageId}")]
        public IActionResult UploadAttachment(List<IFormFile> files, int messageId)
        {
			if (files == null)
			{
				_logRepository.Add(LoggingEvents.DOWNLOAD, $"Uploading attachment for message {messageId}: Bad Request.");
				return BadRequest();
			}

            var attachmentPath = UploadFile(files);
            _messageRepository.AddAttachments(messageId, attachmentPath);

			_logRepository.Add(LoggingEvents.UPLOAD, $"Uploaded attachment \"{files[0].FileName}\".");

            return new OkObjectResult("File Uploaded");
        }

        [Authorize]
        [HttpPost]
        [Route("uploadPicture/{personalInfoId}")]
        public IActionResult UploadPicture(List<IFormFile> files, int personalInfoId)
        {
            var picturePath = UploadFile(files);
            _persInfoRepository.AddPicture(personalInfoId, picturePath);

			_logRepository.Add(LoggingEvents.UPLOAD, $"Uploaded profile picture \"{files[0].FileName}\".");

			return new OkObjectResult("File Uploaded");
        }

        private IActionResult DownloadFile(string path)
        {
            var fileName = Path.GetFileName(path).Substring(36);
            var contentType = MimeTypeMap.GetMimeType(Path.GetExtension(fileName));
            var fileBytes = System.IO.File.ReadAllBytes(path);

			return File(fileBytes, contentType, fileName);
        }

        private string UploadFile(List<IFormFile> files)
        {
			Directory.CreateDirectory(_env.ContentRootPath + "/Files");
            var uploads = Path.Combine(_env.ContentRootPath, "Files");
            var filepath = new List<string>();

			foreach (var file in files)
            {
                if (file.Length > 0)
                {
                    var uniqueFileName = Guid.NewGuid() + file.FileName;
                    filepath.Add(Path.Combine(uploads, uniqueFileName));
                    using (var fileStream = new FileStream(Path.Combine(uploads, uniqueFileName), FileMode.Create))
                    {
                        file.CopyTo(fileStream);
                    }
                }
            }

            var attachment = string.Join(",", filepath);
            return attachment;
        }


    }
}
