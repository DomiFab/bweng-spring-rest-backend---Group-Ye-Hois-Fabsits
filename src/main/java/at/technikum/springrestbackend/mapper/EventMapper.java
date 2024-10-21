package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.EventDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.ForumPostModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.services.MediaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;
@Component
public class EventMapper {

    private final UserMapper userMapper;
    private final MediaServices mediaServices;
    private final ForumPostMapper forumPostMapper;
    private final MediaMapper mediaMapper;

    @Autowired
    public EventMapper(UserMapper userMapper, MediaServices mediaServices,
                       ForumPostMapper forumPostMapper, MediaMapper mediaMapper) {
        this.userMapper = userMapper;
        this.mediaServices = mediaServices;
        this.forumPostMapper = forumPostMapper;
        this.mediaMapper = mediaMapper;
    }

    //creating event
    public EventModel toEntity(EventDTO eventDTO, UserModel creator) {
        //DataBank entry requires the id as a primary key
        return new EventModel(
                UUID.randomUUID().toString(),
                creator,
                eventDTO.getEventName(),
                eventDTO.getEventLocation(), eventDTO.getEventDate(),
                eventDTO.getEventShortDescription(),
                eventDTO.getEventLongDescription(),
                new HashSet<>()
        );
    }

    //display for unauthorized user and dashboard
    public EventDTO toSimpleDTO(EventModel eventModel) {
        //creating a new DTO of Event to assign the values of the Entity to it
        return new EventDTO(
                eventModel.getEventID(), eventModel.getEventName(),
                eventModel.getEventLocation(),
                eventModel.getEventDate(), eventModel.getEventShortDescription(),
                eventModel.getEventLongDescription(),
                eventModel.isDeleted(),
                userMapper.toSimpleDTO(eventModel.getCreator()),
                mediaServices.getFrontPicture(eventModel.getGalleryPictures())
                );
    }

    //for full event page
    public EventDTO toFullDTO(EventModel eventModel) {


        EventDTO displayedEvent = new EventDTO();
        for (ForumPostModel post : eventModel.getEventPosts()){
            displayedEvent.getEventPosts().add(forumPostMapper.toFullDTO(post));
        }
        for (UserModel user : eventModel.getAttendingUsers()){
            displayedEvent.getAttendingUsers().add(new UserDTO(
                    user.getUserID(), user.getUsername(),
                    user.getEmail(), user.getProfilePicture()
                    )
            );
        }
        for (MediaModel media : eventModel.getGalleryPictures()){
            displayedEvent.getGalleryPictures().add(mediaMapper.toSimpleDTO(media));
        }

        return new EventDTO(
                eventModel.getEventID(), eventModel.getEventName(),
                eventModel.getEventLocation(),
                eventModel.getEventDate(), eventModel.getEventShortDescription(),
                eventModel.getEventLongDescription(), eventModel.isDeleted(),
                userMapper.toSimpleDTO(eventModel.getCreator()),
                displayedEvent.getAttendingUsers(), displayedEvent.getGalleryPictures(),
                displayedEvent.getEventPosts()
        );
    }


}
