package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.EventModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends ListCrudRepository<EventModel, String> {

    // Methode zum Suchen von Events nach Location
    List<EventModel> findByEventLocation(String eventAdress);

    // Methode zum Deaktivieren (löschen) eines Events (setzt isDeleted auf true)
    default void softDeleteEvent(EventModel event) {
        event.setDeleted(true);
        save(event);
    }

    // Methode zum Wiederherstellen eines Events (setzt isDeleted auf false)
    default void restoreEvent(EventModel event) {
        event.setDeleted(false);
        save(event);
    }
}
