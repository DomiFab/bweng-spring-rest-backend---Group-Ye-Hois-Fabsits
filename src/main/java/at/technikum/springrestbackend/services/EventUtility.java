package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.EventDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventUtility {

    private final MediaServices mediaServices;

    @Autowired
    public EventUtility(MediaServices mediaServices) {
        this.mediaServices = mediaServices;
    }

    public EventDTO convertToDTO(EventModel event) {
        UserDTO userDTO = new UserDTO(
                        event.getCreator().getUserID(), event.getCreator().getUsername(),
                        event.getCreator().getEmail(), event.getCreator().getProfilePicture()
        );
        return new EventDTO(
                event.getEventID(), event.getEventName(),
                event.getEventLocation(),
                event.getEventDate(), event.getEventShortDescription(),
                event.getEventLongDescription(),
                event.isDeleted(),
                userDTO,
                mediaServices.getFrontPicture(event.getGalleryPictures())
        );
    }
}
