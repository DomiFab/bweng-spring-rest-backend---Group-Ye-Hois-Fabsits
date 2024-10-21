package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.ForumPostModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumPostRepository extends ListCrudRepository<ForumPostModel, String> {

    Optional<ForumPostModel> deleteAllByEvent(EventModel event);
}
