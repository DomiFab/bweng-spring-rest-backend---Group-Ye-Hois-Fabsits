package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.*;
import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.exception.OwnerCannotLeaveEvent;
import at.technikum.springrestbackend.exception.UserAlreadyJoinedException;
import at.technikum.springrestbackend.mapper.CommentMapper;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.mapper.MediaMapper;
import at.technikum.springrestbackend.mapper.UserMapper;
import at.technikum.springrestbackend.model.CommentModel;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.*;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServices {

    private final EventRepository eventRepository;
    private final UserServices userServices;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final MediaMapper mediaMapper;
    @Autowired
    private UserMapper userMapper;
    private final CommentRepository commentRepository;
    private final MediaRepository mediaRepository;

    @Autowired
    public EventServices(EventRepository eventRepository, UserServices userServices, EventMapper eventMapper,
                         UserRepository userRepository, CommentMapper commentMapper, MediaMapper mediaMapper,
                         CommentRepository commentRepository,
                         MediaRepository mediaRepository) {
        this.eventRepository = eventRepository;
        this.userServices = userServices;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.mediaMapper = mediaMapper;
        this.commentRepository = commentRepository;
        this.mediaRepository = mediaRepository;
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

    public List<DisplayCommentDTO> getCommentsFromEvent(EventModel eventModel) {

        List<DisplayCommentDTO> comments = new ArrayList<>();
        for (CommentModel comment : eventModel.getEventComments()) {
            comments.add(commentMapper.toDisplayDTO(comment));
        }
        return comments;
    }

    public Integer getCommentCount(EventModel event) {
        return event.getEventComments().size();
    }

    public List<UserDTO> getAttendees(EventModel event) {

        List<UserDTO> attendees = new ArrayList<>();
        for (UserModel user : event.getAttendingUsers()) {
            attendees.add(userMapper.toSimpleDTO(user));
        }
        return attendees;
    }

    public Integer getAttendingCount(EventModel event) {
        return event.getAttendingUsers().size();
    }

    public List<MediaDTO> getMediaFromEvent(EventModel eventModel) {

        List<MediaDTO> mediaList = new ArrayList<>();
        for (MediaModel media : eventModel.getGalleryPictures()) {
            mediaList.add(mediaMapper.toDTO(media));
        }
        return mediaList;
    }

    public EventModel save(EventModel eventModel){

        return eventRepository.save(eventModel);
    }

    public DisplayEventDTO eventDisplay (EventModel event, UserModel user) {
        if (user.getUserID() == null || user.getUserID().isEmpty()) {
            return eventMapper.toNoAuthDTO(event);
        }
        return eventMapper.toDisplayDTO(event, user.getUserID());
    }

    public EventModel update(String eventID, CreateEventDTO updatedEventDTO, UserModel user) {

        //catching the case when an entity with the id does not exist
        if (!idExists(eventID)) {
            throw new EntityNotFoundException("Event with provided ID [" + eventID + "] not found.");
        }
        //get the existing Event from the DB and THEN set new values
        EventModel updatedEvent = find(eventID);

        isAuthorized(updatedEvent, user.getUserID());

        user.getCreatedEvents().remove(updatedEvent);

        updatedEvent.setEventName(updatedEventDTO.getEventName());
        updatedEvent.setEventLocation(updatedEventDTO.getEventLocation());
        updatedEvent.setEventDescription(updatedEventDTO.getEventDescription());
        updatedEvent.setEventStatus(updatedEventDTO.getEventStatus());

        user.getCreatedEvents().add(updatedEvent);
        userRepository.save(user);

        return save(updatedEvent);
    }

    public EventModel updateStatus(EventModel event, UserModel user, String status) {

        isAuthorized(event, user.getUserID());

        user.getCreatedEvents().remove(event);
        event.setEventStatus(status);
        user.getCreatedEvents().add(event);

        userRepository.save(user);

        return eventRepository.save(event);
    }

    public void addComment(EventModel event, CommentModel comment) {
        event.getEventComments().add(comment);
        save(event);
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

    public EventModel deleteFinal(String eventID, String userID) {
        //find event by ID
        EventModel event = find(eventID);
        isAuthorized(event, userID);

        for (UserModel user : event.getAttendingUsers()){
            user.getAttendingEvents().remove(event);
            userRepository.save(user);
        }

        commentRepository.deleteAllByEvent(event);
        mediaRepository.deleteAllByEvent(event);

        event.getCreator().getCreatedEvents().remove(event);
        userRepository.save(event.getCreator());

        eventRepository.delete(event);
        return event;
    }

    public void isAuthorized(EventModel event, String userID) {
        if (!event.getCreator().getUserID().equals(userID) &&
                !userServices.findByID(userID).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to perform this action.");
        }
    }

    public EventModel joinEvent(String eventId, String userID) {
        // Find the event by ID
        EventModel event = find(eventId);
        // Find the user by ID
        UserModel user = userServices.findByID(userID);

        if (event.getAttendingUsers().contains(user)) {
            throw new UserAlreadyJoinedException("You already joined this event.");
        }
        // Add user to event's attending users
        event.getAttendingUsers().add(user);
        // Add event to user's attending events
        user.getAttendingEvents().add(event);

        // Save the updated event and user
        userServices.save(user);
        return save(event);
    }

    //for event owners and admins to remove users from event
    public EventModel removeUserFromEvent(String eventID, String userID){
        // Find the event by ID
        EventModel event = find(eventID);
        // Find the user by ID
        UserModel user = userServices.findByID(userID);

        isAuthorized(event, userID);

        if (!event.getAttendingUsers().contains(user)){
            throw new EntityNotFoundException("The user is not attending this event.");
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
    public EventModel leaveEvent(String eventID, String userID) {
        // Find the event by ID
        EventModel event = find(eventID);
        // Find the user by ID
        UserModel user = userServices.findByID(userID);

        if (event.getCreator().equals(user)) {
            throw new OwnerCannotLeaveEvent("The owner cannot leave their own event...");
        }

        // Add user to event's attending users
        event.getAttendingUsers().remove(user);
        // Add event to user's attending events
        user.getAttendingEvents().remove(event);

        // Save the updated event and user
        userServices.save(user);
        return save(event);
    }

    public void createEvent(EventModel event, UserModel creator) {
        save(event);
        userServices.addCreatedEvent(creator, event);
    }
}

