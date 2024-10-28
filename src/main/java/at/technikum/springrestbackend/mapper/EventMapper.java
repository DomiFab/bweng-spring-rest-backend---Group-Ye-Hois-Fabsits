package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.CreateEventDTO;
import at.technikum.springrestbackend.dto.DisplayEventDTO;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.services.MediaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventMapper {

    private final UserMapper userMapper;
    private final MediaServices mediaServices;
    private final CommentMapper forumPostMapper;
    private final MediaMapper mediaMapper;

    @Autowired
    public EventMapper(UserMapper userMapper, MediaServices mediaServices,
                       CommentMapper forumPostMapper, MediaMapper mediaMapper) {
        this.userMapper = userMapper;
        this.mediaServices = mediaServices;
        this.forumPostMapper = forumPostMapper;
        this.mediaMapper = mediaMapper;
    }

    public DisplayEventDTO toDisplayDTO (EventModel eventModel, String username) {

        boolean isCreator = eventModel.getCreator().getUsername().equals(username);

        return new DisplayEventDTO(
                eventModel.getEventID(), eventModel.getEventName(),
                eventModel.getEventLocation(), eventModel.getEventDate(),
                eventModel.getEventStatus(), eventModel.getEventDescription(),
                eventModel.getEventPicture(), eventModel.isDeleted(),
                userMapper.toSimpleDTO(eventModel.getCreator()),
                isCreator, (long) eventModel.getAttendingUsers().size()
        );
    }

    //creating event
    public EventModel toEntity(CreateEventDTO eventDTO, UserModel creator) {
        //DataBank entry requires the id as a primary key
        return new EventModel(
                UUID.randomUUID().toString(),
                eventDTO.getEventName(), eventDTO.getEventLocation(),
                eventDTO.getEventDate(), eventDTO.getEventDescription(),
                creator, eventDTO.getEventStatus()
        );
    }

    //display for unauthorized user and dashboard
    public CreateEventDTO toSimpleDTO(EventModel eventModel) {
        //creating a new DTO of Event to assign the values of the Entity to it
        return new CreateEventDTO(
//                eventModel.getEventID(), eventModel.getEventName(),
//                eventModel.getEventLocation(),
//                eventModel.getEventDate(), eventModel.getEventShortDescription(),
//                eventModel.getEventLongDescription(),
//                eventModel.isDeleted(),
//                userMapper.toSimpleDTO(eventModel.getCreator()),
//                mediaServices.getFrontPicture(eventModel.getGalleryPictures())
        );
    }

    //for full event page
    public CreateEventDTO toFullDTO(EventModel eventModel) {


//        CreateEventDTO displayedEvent = new CreateEventDTO();
//        for (CommentModel post : eventModel.getEventPosts()){
//            displayedEvent.getEventPosts().add(forumPostMapper.toFullDTO(post));
//        }
//        for (UserModel user : eventModel.getAttendingUsers()){
//            displayedEvent.getAttendingUsers().add(new UserDTO(
//                    user.getUserID(), user.getUsername(),
//                    user.getEmail(), user.getProfilePicture()
//                    )
//            );
//        }
//        for (MediaModel media : eventModel.getGalleryPictures()){
//            displayedEvent.getGalleryPictures().add(mediaMapper.toSimpleDTO(media));
//        }

        return new CreateEventDTO(
//                eventModel.getEventID(), eventModel.getEventName(),
//                eventModel.getEventLocation(),
//                eventModel.getEventDate(), eventModel.getEventShortDescription(),
//                eventModel.getEventLongDescription(), eventModel.isDeleted(),
//                userMapper.toSimpleDTO(eventModel.getCreator()),
//                displayedEvent.getAttendingUsers(), displayedEvent.getGalleryPictures(),
//                displayedEvent.getEventPosts()
        );
    }


}
