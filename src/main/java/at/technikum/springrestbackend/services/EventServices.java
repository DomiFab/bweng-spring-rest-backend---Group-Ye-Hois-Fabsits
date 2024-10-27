package at.technikum.springrestbackend.services;


import at.technikum.springrestbackend.dto.CreateEventDTO;
import at.technikum.springrestbackend.dto.DisplayEventDTO;
import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.*;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    public boolean idExists(String id){
        return eventRepository.existsById(id);
    }

    public EventModel find(String id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Event not found with id: " + id));
    }

    public List<EventModel> findAll (){
        return eventRepository.findAll();
    }

    public List<DisplayEventDTO> getEventsFromUser (UserModel userModel){

        List<DisplayEventDTO> events = new ArrayList<>();
        //attending events
        for (EventModel event : userModel.getAttendingEvents()){
            events.add(eventMapper.toDisplayDTO(event, userModel.getUserID()));
        }
        for (EventModel event : userModel.getCreatedEvents()){
            events.add(eventMapper.toDisplayDTO(event, userModel.getUserID()));
        }

        return events;
    }

    public EventModel save(EventModel eventModel){
        return eventRepository.save(eventModel);
    }

    public EventModel update(String id, CreateEventDTO updatedEventDTO, List<MultipartFile> files, String userName) {

        //catching the case when an entity with the id does not exist
        if (!idExists(id)) {
            throw new EntityNotFoundException("Event with provided ID [" + id + "] not found.");
        }
        //get the existing Event from the DB and THEN set new values
        EventModel updatedEvent = find(id);

        if (!updatedEvent.getCreator().getUsername().equals(userName) &&
                !userServices.findByUsername(userName).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to update this event.");
        }

        updatedEvent.setEventName(updatedEventDTO.getEventName());
        updatedEvent.setEventLocation(updatedEventDTO.getEventLocation());
        updatedEvent.setEventDate(updatedEventDTO.getEventDate());

//        List<MediaModel> mediaList = fileService.updateFrontPicture(files, updatedEvent);
//        updatedEvent.getGalleryPictures().addAll(mediaList);

        return eventRepository.save(updatedEvent);
    }

    //set isDeleted flag for SoftDelete
    public EventModel delete(String eventID){
        Optional<EventModel> eventOptional = eventRepository.findById(eventID);

        if (eventOptional.isPresent()) {
            EventModel event = eventOptional.get();
            eventRepository.softDeleteEvent(event);
            return event;
        } else {
            throw new RuntimeException("Event not found with ID: " + eventID);
        }
    }

    public EventModel deleteFinal(String eventID, String username) {
        //find event by ID
        EventModel event = find(eventID);
        if (!event.getCreator().getUsername().equals(username) &&
                !userServices.findByUsername(username).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to update this event.");
        }
        eventRepository.delete(event);
        return event;
    }

    public EventModel joinEvent(String eventId, String userID) {
        // Find the event by ID
        EventModel event = find(eventId);
        // Find the user by ID
        UserModel user = userServices.findByID(userID);

        // Add user to event's attending users
        event.getAttendingUsers().add(user);
        // Add event to user's attending events
        user.getAttendingEvents().add(event);

        // Save the updated event and user
        userServices.save(user);
        return save(event);
    }

    //for event owners and admins to block users from event
    public EventModel removeUserFromEvent(String eventID, String userID, String username){

        // Find the event by ID
        EventModel event = find(eventID);
        // Find the user by ID
        UserModel user = userServices.findByID(userID);

        if (!event.getCreator().getUsername().equals(username) &&
                !userServices.findByUsername(username).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to update this event.");
        }

        // Add user to event's attending users
        event.getAttendingUsers().remove(user);
        // Add event to user's attending events
        user.getAttendingEvents().remove(event);

        // Save the updated event and user
        userServices.save(user);
        return save(event);
    }

    //for registered users to leave event by themselves
    public EventModel leaveEvent(String eventId, String userID) {
        // Find the event by ID
        EventModel event = find(eventId);
        // Find the user by ID
        UserModel user = userServices.findByID(userID);

        // Add user to event's attending users
        event.getAttendingUsers().remove(user);
        // Add event to user's attending events
        user.getAttendingEvents().remove(event);

        // Save the updated event and user
        userServices.save(user);
        return save(event);
    }

    public CreateEventDTO getEventDetails(String eventID){
        EventModel event = find(eventID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Return full DTO if authenticated
            return eventMapper.toFullDTO(event);
        } else {
            // Return simple DTO if not authenticated
            return eventMapper.toSimpleDTO(event);
        }
    }


}

