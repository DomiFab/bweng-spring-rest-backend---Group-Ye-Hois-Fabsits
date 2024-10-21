package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.EventDTO;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventUtility {

    private final UserMapper userMapper;
    private final MediaServices mediaServices;

    @Autowired
    public EventUtility(UserMapper userMapper, MediaServices mediaServices) {
        this.userMapper = userMapper;
        this.mediaServices = mediaServices;
    }

    public EventDTO convertToDTO(EventModel event) {
        return new EventDTO(
                event.getEventID(),
                event.getEventName(),
                event.getEventLocation(),
                event.getEventDate(),
                event.getEventShortDescription(),
                event.getEventLongDescription(),
                event.isDeleted(),
                userMapper.toSimpleDTO(event.getCreator()),
                mediaServices.getFrontPicture(event.getGalleryPictures())
        );
    }
}