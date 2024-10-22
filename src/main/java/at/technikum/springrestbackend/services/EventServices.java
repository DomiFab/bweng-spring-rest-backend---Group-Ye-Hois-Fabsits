package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.EventDTO;
import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.EventRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventServices {
    private final EventRepository eventRepository;
    private final MediaRepository mediaRepository;
    private final FileService fileService;
    private final UserServices userServices;
    private final MediaServices mediaServices;
    private final EventMapper eventMapper;

    @Autowired
    public EventServices(EventRepository eventRepository,
                         MediaRepository mediaRepository,
                         FileService fileService, UserServices userServices,
                         MediaServices mediaServices, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.mediaRepository = mediaRepository;
        this.fileService = fileService;
        this.userServices = userServices;
        this.mediaServices = mediaServices;
        this.eventMapper = eventMapper;
    }

    public boolean idExists(UUID id) {
        return eventRepository.existsById(id);
    }

    public EventModel find(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
    }

    public List<EventModel> findAll() {
        return eventRepository.findAll();
    }

    public EventModel save(EventModel eventModel) {
        return eventRepository.save(eventModel);
    }

    public EventModel update(UUID id, EventDTO updatedEventDTO, List<MultipartFile> files, String userName) {
        if (!idExists(id)) {
            throw new EntityNotFoundException("Event with provided ID [" + id + "] not found.");
        }

        EventModel updatedEvent = find(id);

        if (!updatedEvent.getCreator().getUsername().equals(userName) &&
                !userServices.findByUsername(userName).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to update this event.");
        }

        updatedEvent.setEventName(updatedEventDTO.getEventName());
        updatedEvent.setEventLocation(updatedEventDTO.getEventLocation());
        updatedEvent.setEventDate(updatedEventDTO.getEventDate());
        updatedEvent.setEventShortDescription(updatedEventDTO.getEventShortDescription());
        updatedEvent.setEventLongDescription(updatedEventDTO.getEventLongDescription());

        List<MediaModel> mediaList = fileService.updateFrontPicture(files, updatedEvent);
        updatedEvent.getGalleryPictures().addAll(mediaList);

        return eventRepository.save(updatedEvent);
    }

    public EventModel delete(UUID eventID) {
        Optional<EventModel> eventOptional = eventRepository.findById(eventID);

        if (eventOptional.isPresent()) {
            EventModel event = eventOptional.get();
            eventRepository.softDeleteEvent(event);
            return event;
        } else {
            throw new RuntimeException("Event not found with ID: " + eventID);
        }
    }

    public EventModel deleteFinal(UUID eventID, String username) {
        EventModel event = find(eventID);
        if (!event.getCreator().getUsername().equals(username) &&
                !userServices.findByUsername(username).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to delete this event.");
        }
        eventRepository.delete(event);
        return event;
    }

    public EventModel addUserToEvent(UUID eventId, UUID userID) {
        EventModel event = find(eventId);
        UserModel user = userServices.find(userID);

        event.getAttendingUsers().add(user);
        user.getAttendingEvents().add(event);

        userServices.save(user);
        return save(event);
    }

    public EventModel removeUserFromEvent(UUID eventID, UUID userID, String username) {
        EventModel event = find(eventID);
        UserModel user = userServices.find(userID);

        if (!event.getCreator().getUsername().equals(username) &&
                !userServices.findByUsername(username).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to remove users from this event.");
        }

        event.getAttendingUsers().remove(user);
        user.getAttendingEvents().remove(event);

        userServices.save(user);
        return save(event);
    }

    public EventModel leaveEvent(UUID eventId, String username) {
        EventModel event = find(eventId);
        UserModel user = userServices.findByUsername(username);

        event.getAttendingUsers().remove(user);
        user.getAttendingEvents().remove(event);

        userServices.save(user);
        return save(event);
    }

    public boolean deletePictureFromGallery(UUID eventID, UUID mediaID, String userName) {
        MediaModel media = mediaServices.findByEventAndMedia(mediaID, eventID);
        EventModel event = media.getEvent();
        boolean isAdmin = userServices.findByUsername(userName).isAdmin();

        if (!media.getUploader().getUsername().equals(userName) &&
                !event.getCreator().getUsername().equals(userName) &&
                !isAdmin) {
            return false; // Not authorized
        }
        UserModel user = userServices.findByUsername(userName);
        user.getUploadedMedia().remove(media);
        userServices.save(user);
        event.getGalleryPictures().remove(media);
        save(event);
        fileService.deleteFile(media.getFileLocation());
        mediaRepository.delete(media);
        return true;
    }

    public EventDTO getEventDetails(UUID eventID) {
        EventModel event = find(eventID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return eventMapper.toFullDTO(event);
        } else {
            return eventMapper.toSimpleDTO(event);
        }
    }
}