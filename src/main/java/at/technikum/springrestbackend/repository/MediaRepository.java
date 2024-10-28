package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.MediaModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends ListCrudRepository<MediaModel, String> {

    Optional<MediaModel> deleteAllByEvent(EventModel event);
    void deleteByFileURL(String fileURL);
    MediaModel findByFileURL(String fileURL);
    Optional<MediaModel> findByMediaIDAndEvent(String mediaId, EventModel eventId);

}
