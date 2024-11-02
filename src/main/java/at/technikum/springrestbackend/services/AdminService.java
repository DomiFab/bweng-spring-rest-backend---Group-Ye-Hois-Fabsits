package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.DisplayEventDTO;
import at.technikum.springrestbackend.dto.FullUserDTO;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final UserServices userServices;

    public AdminService(UserRepository userRepository, EventMapper eventMapper, UserServices userServices) {
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
        this.userServices = userServices;
    }

    public UserModel updateUserDetails(UserModel user, FullUserDTO userDTO) {
        if (userServices.usernameExists(userDTO.getUsername())) {
            throw new EntityExistsException("Username already exists: " + userDTO.getUsername());
        }
        if (userServices.emailExists(userDTO.getEmail())) {
            throw new EntityExistsException("Email already exists: " + userDTO.getEmail());
        }

        user.setAdmin(userDTO.isAdmin());

        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        return userRepository.save(user);
    }

    public List<DisplayEventDTO> getCreatedEventsByUser(UserModel user) {

        List<DisplayEventDTO> createdEvents = new ArrayList<>();
        for (EventModel event : user.getCreatedEvents()){
            createdEvents.add(eventMapper.toDisplayDTO(event, user.getUserID()));
        }

        return createdEvents;
    }

    public List<DisplayEventDTO> getAttendingEvents(UserModel user) {

        List<DisplayEventDTO> attendingEvents = new ArrayList<>();
        for (EventModel event : user.getAttendingEvents()){
            attendingEvents.add(eventMapper.toDisplayDTO(event, user.getUserID()));
        }

        return attendingEvents;
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
