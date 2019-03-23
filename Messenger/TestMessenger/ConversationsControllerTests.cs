using AutoMapper;
using Messenger.Controllers;
using Messenger.Models;
using Messenger.Paginations;
using Messenger.Repositories;
using Messenger.ViewModels;
using Messenger.ViewModels.Mappings;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.DependencyInjection;
using Moq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using Xunit;

namespace TestMessenger
{
    public class ConversationsControllerTests
    {

        [Fact]
        public void GetReturnsConversationWithSameId()
        {
            // Arrange
            var mockConvRepository = new Mock<IConversationRepository>();
            var mockUserConvRepository = new Mock<IUserConversationRepository>();
            var mockMessRepository = new Mock<IMessageRepository>();
            var mockConvPagService = new Mock<IConversationPaginationService>();
            var mockEvenLogRepository = new Mock<IEventLogRepository>();

            Mapper.Initialize(m => m.AddProfile<MappingProfile>());

            mockConvRepository.Setup(x => x.Find(2))
                .Returns(new Conversation { Id = 2 });

            var controller = new ConversationsController(mockEvenLogRepository.Object, mockConvRepository.Object,
                mockUserConvRepository.Object, mockMessRepository.Object, mockConvPagService.Object);

            // Act
            OkObjectResult objectResult = controller.GetById(2) as OkObjectResult;
            var conversation = objectResult.Value as ConversationViewModel;

            // Assert
            Assert.NotNull(objectResult);
            Assert.Equal(2, conversation.Id);
        }

        [Fact]
        public void CreateReturnsBadRequest()
        {
            // Arrange & Act
            var mockConvRepository = new Mock<IConversationRepository>();
            var mockUserConvRepository = new Mock<IUserConversationRepository>();
            var mockMessRepository = new Mock<IMessageRepository>();
            var mockConvPagService = new Mock<IConversationPaginationService>();
            var mockEventLogRepoisotry = new Mock<IEventLogRepository>();

            var controller = new ConversationsController(mockEventLogRepoisotry.Object, mockConvRepository.Object, 
                mockUserConvRepository.Object, mockMessRepository.Object, mockConvPagService.Object);
            controller.ModelState.AddModelError("error", "some error");

            // Act
            var result = controller.Create(null);

            // Assert
            Assert.IsType<BadRequestResult>(result);
        }
    }
}
