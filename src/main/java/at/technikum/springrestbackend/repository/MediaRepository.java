package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.ForumPostModel;
import at.technikum.springrestbackend.model.ForumThreadModel;
import at.technikum.springrestbackend.model.MediaModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends ListCrudRepository<MediaModel, String> {

    Optional<MediaModel> deleteAllByEvent(EventModel event);
    Optional<MediaModel> deleteAllByPost(ForumPostModel post);
    Optional<MediaModel> deleteAllByComment(ForumThreadModel comment);

    Optional<MediaModel> findByMediaIDAndEvent(String mediaId, EventModel eventId);

}
