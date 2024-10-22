package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.ForumPostModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ForumPostRepository extends ListCrudRepository<ForumPostModel, UUID> {

    Optional<ForumPostModel> deleteAllByEvent(EventModel event);
}