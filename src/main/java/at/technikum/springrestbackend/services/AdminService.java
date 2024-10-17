package at.technikum.springrestbackend.services;

import org.springframework.stereotype.Service;

@Service
public class AdminService {

    public String deleteUser(String userId) {
        // Hier sollte die Logik stehen, um den Benutzer in der Datenbank zu deaktivieren
        return "User mit ID " + userId + " wurde gelöscht.";
    }

    public String restoreUser(String userId) {
        // Hier sollte die Logik stehen, um den Benutzer in der Datenbank wiederherzustellen
        return "User mit ID " + userId + " wurde wiederhergestellt.";
    }

    public String deleteEvent(String eventId) {
        // Hier sollte die Logik stehen, um das Event in der Datenbank zu deaktivieren
        return "Event mit ID " + eventId + " wurde gelöscht.";
    }

    public String restoreEvent(String eventId) {
        // Hier sollte die Logik stehen, um das Event in der Datenbank wiederherzustellen
        return "Event mit ID " + eventId + " wurde wiederhergestellt.";
    }
}
