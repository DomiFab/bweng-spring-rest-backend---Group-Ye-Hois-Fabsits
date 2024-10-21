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
import java.util.stream.Collectors;

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

    // Creating event
    public EventModel toEntity(EventDTO eventDTO, UserModel creator) {
        EventModel event = new EventModel(
                creator,
                eventDTO.getEventName(),
                eventDTO.getEventLocation(),
                eventDTO.getEventShortDescription()
        );
        event.setEventID(UUID.randomUUID()); // Generate UUID
        return event;
    }

    // Display for unauthorized user and dashboard
    public EventDTO toSimpleDTO(EventModel eventModel) {
        return new EventDTO(
                eventModel.getEventID(),
                eventModel.getEventName(),
                eventModel.getEventLocation(),
                eventModel.getEventDate(),
                eventModel.getEventShortDescription(),
                eventModel.getEventLongDescription(),
                eventModel.isDeleted(),
                userMapper.toSimpleDTO(eventModel.getCreator()),
                mediaServices.getFrontPicture(eventModel.getGalleryPictures())
        );
    }

    // For full event page
    public EventDTO toFullDTO(EventModel eventModel) {
        EventDTO displayedEvent = new EventDTO();
        displayedEvent.setEventPosts(eventModel.getEventPosts().stream()
                .map(forumPostMapper::toFullDTO)
                .collect(Collectors.toSet()));
        displayedEvent.setAttendingUsers(eventModel.getAttendingUsers().stream()
                .map(user -> new UserDTO(user.getUserID(), user.getUsername(), user.getEmail(), user.getProfilePicture()))
                .collect(Collectors.toSet()));
        displayedEvent.setGalleryPictures(eventModel.getGalleryPictures().stream()
                .map(mediaMapper::toSimpleDTO)
                .collect(Collectors.toSet()));

        return new EventDTO(
                eventModel.getEventID(),
                eventModel.getEventName(),
                eventModel.getEventLocation(),
                eventModel.getEventDate(),
                eventModel.getEventShortDescription(),
                eventModel.getEventLongDescription(),
                eventModel.isDeleted(),
                userMapper.toSimpleDTO(eventModel.getCreator()),
                displayedEvent.getAttendingUsers(),
                displayedEvent.getGalleryPictures(),
                displayedEvent.getEventPosts()
        );
    }

    // General method to convert EventModel to EventDTO
    public EventDTO toDTO(EventModel eventModel) {
        return new EventDTO(
                eventModel.getEventID(),
                eventModel.getEventName(),
                eventModel.getEventLocation(),
                eventModel.getEventDate(),
                eventModel.getEventShortDescription(),
                eventModel.getEventLongDescription(),
                eventModel.isDeleted(),
                userMapper.toSimpleDTO(eventModel.getCreator()),
                eventModel.getAttendingUsers().stream()
                        .map(user -> new UserDTO(user.getUserID(), user.getUsername(), user.getEmail(), user.getProfilePicture()))
                        .collect(Collectors.toSet()),
                eventModel.getGalleryPictures().stream()
                        .map(mediaMapper::toSimpleDTO)
                        .collect(Collectors.toSet()),
                eventModel.getEventPosts().stream()
                        .map(forumPostMapper::toFullDTO)
                        .collect(Collectors.toSet())
        );
    }
}