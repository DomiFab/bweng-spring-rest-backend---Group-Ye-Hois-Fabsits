package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.ForumPostModel;
import at.technikum.springrestbackend.model.ForumThreadModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumThreadRepository extends ListCrudRepository<ForumThreadModel, String> {

    Optional<ForumThreadModel> deleteAllByPost(ForumPostModel post);

}
