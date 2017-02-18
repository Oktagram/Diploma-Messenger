using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using System.IO;
using Messenger.Repositories;
using System.Net.Http;
using System.Net;
using Microsoft.Net.Http.Headers;
using Microsoft.AspNetCore.Authorization;
using System.Text;

// For more information on enabling Web API for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace Messenger.Controllers
{
    [Route("api/[controller]")]
    public class FilesController : Controller
    {
        private IHostingEnvironment _env;
        private IMessageRepository _messageRepository;
        private IPersonalInfoRepository _persInfoRepository;

        public FilesController(IHostingEnvironment env, IMessageRepository messageRepository, IPersonalInfoRepository persInfoRepository)
        {
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
                return DownloadFile(message.Attachment);
            }
            catch
            {
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
                return DownloadFile(persInfo.Picture);
            }
            catch
            {
                return NotFound();
            }
        }

        [Authorize]
        [HttpPost]
        [Route("uploadAttachment/{messageId}")]
        public IActionResult UploadAttachment(List<IFormFile> files, int messageId)
        {
            string attachmentPath = UploadFile(files);
            _messageRepository.AddAttachments(messageId, attachmentPath);

            return new OkObjectResult("File Uploaded");
        }

        [Authorize]
        [HttpPost]
        [Route("uploadPicture/{personalInfoId}")]
        public IActionResult UploadPicture(List<IFormFile> files, int personalInfoId)
        {
            string picturePath = UploadFile(files);
            _persInfoRepository.AddPicture(personalInfoId, picturePath);

            return new OkObjectResult("File Uploaded");
        }

        private IActionResult DownloadFile(string path)
        {

            string fileName = Path.GetFileName(path).Substring(36);
            string contentType = MimeTypeMap.GetMimeType(Path.GetExtension(fileName));
            byte[] fileBytes = System.IO.File.ReadAllBytes(path);
            return File(fileBytes, contentType, fileName);
        }

        private string UploadFile(List<IFormFile> files)
        {
			System.IO.Directory.CreateDirectory(_env.ContentRootPath + "/Files");
            var uploads = Path.Combine(_env.ContentRootPath, "Files");
            List<string> filepath = new List<string>();
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
