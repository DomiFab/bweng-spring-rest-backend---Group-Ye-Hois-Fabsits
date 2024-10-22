package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.ForumPostModel;
import at.technikum.springrestbackend.model.ForumThreadModel;
import at.technikum.springrestbackend.model.MediaModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MediaRepository extends ListCrudRepository<MediaModel, UUID> {

    Optional<MediaModel> deleteAllByEvent(EventModel event);
    Optional<MediaModel> deleteAllByPost(ForumPostModel post);
    Optional<MediaModel> deleteAllByComment(ForumThreadModel comment);

    Optional<MediaModel> findByMediaIDAndEvent(UUID mediaId, EventModel event);
}