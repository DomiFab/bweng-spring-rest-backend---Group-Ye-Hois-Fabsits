package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventModel, UUID> { //ListCrudRepository

    // Method to find events by location
    List<EventModel> findByEventLocation(String eventLocation);

    // Method to soft delete an event (sets isDeleted to true)
    default void softDeleteEvent(EventModel event) {
        event.setDeleted(true);
        save(event);
    }

    // Method to restore an event (sets isDeleted to false)
    default void restoreEvent(EventModel event) {
        event.setDeleted(false);
        save(event);
    }
}