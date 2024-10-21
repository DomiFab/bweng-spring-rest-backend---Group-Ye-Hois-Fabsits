package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.EventRepository;
import at.technikum.springrestbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public AdminService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public String deleteUser(Long userId, String reason) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        user.setDeleted(true);
        userRepository.save(user);

        // Deactivate all events created by the user
        List<EventModel> userEvents = eventRepository.findByCreator(user);
        for (EventModel event : userEvents) {
            event.setDeleted(true);
            eventRepository.save(event);
        }

        return "User with ID " + userId + " was deleted for reason: " + reason;
    }

    public String restoreUser(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        user.setDeleted(false);
        userRepository.save(user);

        // Restore all events created by the user
        List<EventModel> userEvents = eventRepository.findByCreator(user);
        for (EventModel event : userEvents) {
            event.setDeleted(false);
            eventRepository.save(event);
        }

        return "User with ID " + userId + " was restored.";
    }

    public String deleteEvent(Long eventId, String reason) {
        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        event.setDeleted(true);
        eventRepository.save(event);
        return "Event with ID " + eventId + " was deleted for reason: " + reason;
    }

    public String restoreEvent(Long eventId) {
        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        event.setDeleted(false);
        eventRepository.save(event);
        return "Event with ID " + eventId + " was restored.";
    }
}