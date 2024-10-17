package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.repository.UserRepository;
import at.technikum.springrestbackend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public String deleteUser(String userId) {
        Optional<UserModel> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UserModel userModel = user.get();
            userModel.setDeleted(true);
            userRepository.save(userModel);
            return "User mit ID " + userId + " wurde gelöscht.";
        } else {
            return "User mit ID " + userId + " wurde nicht gefunden.";
        }
    }

    public String restoreUser(String userId) {
        Optional<UserModel> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UserModel userModel = user.get();
            userModel.setDeleted(false);
            userRepository.save(userModel);
            return "User mit ID " + userId + " wurde wiederhergestellt.";
        } else {
            return "User mit ID " + userId + " wurde nicht gefunden.";
        }
    }

    public String deleteEvent(String eventId) {
        Optional<EventModel> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            EventModel eventModel = event.get();
            eventModel.setDeleted(true);
            eventRepository.save(eventModel);
            return "Event mit ID " + eventId + " wurde gelöscht.";
        } else {
            return "Event mit ID " + eventId + " wurde nicht gefunden.";
        }
    }

    public String restoreEvent(String eventId) {
        Optional<EventModel> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            EventModel eventModel = event.get();
            eventModel.setDeleted(false);
            eventRepository.save(eventModel);
            return "Event mit ID " + eventId + " wurde wiederhergestellt.";
        } else {
            return "Event mit ID " + eventId + " wurde nicht gefunden.";
        }
    }
}
