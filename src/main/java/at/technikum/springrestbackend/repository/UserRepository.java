package at.technikum.springrestbackend.repository;

import at.technikum.springrestbackend.model.UserModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository<UserModel, String> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByEmail(String email);

    // Methode zum Deaktivieren (löschen) eines Benutzers (setzt isDeleted auf true)
    default void softDeleteUser(UserModel user) {
        user.setDeleted(true);
        save(user);
    }

    // Methode zum Wiederherstellen eines Benutzers (setzt isDeleted auf false)
    default void restoreUser(UserModel user) {
        user.setDeleted(false);
        save(user);
    }

}
