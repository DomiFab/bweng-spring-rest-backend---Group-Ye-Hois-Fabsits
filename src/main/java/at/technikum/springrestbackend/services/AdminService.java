package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.DisplayEventDTO;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public AdminService(UserRepository userRepository, EventMapper eventMapper) {
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
    }

    public UserModel setUserAdmin(UserModel user) {
        user.setAdmin(true);
        return userRepository.save(user);
    }

    public List<DisplayEventDTO> getCreatedEventsByUser(UserModel user) {

        List<DisplayEventDTO> createdEvents = new ArrayList<>();
        for (EventModel event : user.getCreatedEvents()){
            createdEvents.add(eventMapper.toDisplayDTO(event, user.getUserID()));
        }

        return createdEvents;
    }

    public String deleteUser(Long userId) {
        // Hier sollte die Logik stehen, um den Benutzer in der Datenbank zu deaktivieren
        return "User mit ID " + userId + " wurde gelöscht.";
    }

    public String restoreUser(Long userId) {
        // Hier sollte die Logik stehen, um den Benutzer in der Datenbank wiederherzustellen
        return "User mit ID " + userId + " wurde wiederhergestellt.";
    }

    public String deleteEvent(Long eventId) {
        // Hier sollte die Logik stehen, um das Event in der Datenbank zu deaktivieren
        return "Event mit ID " + eventId + " wurde gelöscht.";
    }

    public String restoreEvent(Long eventId) {
        // Hier sollte die Logik stehen, um das Event in der Datenbank wiederherzustellen
        return "Event mit ID " + eventId + " wurde wiederhergestellt.";
    }
}
