package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.UserModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends ListCrudRepository<UserModel, UUID> {

    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmail(String email);

    // Method to deactivate (soft delete) a user (sets isDeleted to true)
    default void softDeleteUser(UserModel user) {
        user.setDeleted(true);
        save(user);
    }

    // Method to restore a user (sets isDeleted to false)
    default void restoreUser(UserModel user) {
        user.setDeleted(false);
        save(user);
    }
}