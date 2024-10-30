package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.ResetPasswordDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.mapper.UserMapper;
import at.technikum.springrestbackend.model.CommentModel;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.CommentRepository;
import at.technikum.springrestbackend.repository.EventRepository;
import at.technikum.springrestbackend.repository.MediaRepository;
import at.technikum.springrestbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EventRepository eventRepository;
    private final MediaRepository mediaRepository;

    @Autowired
    public UserServices(UserRepository userRepository, UserMapper userMapper,
                        MediaRepository mediaRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.mediaRepository = mediaRepository;
    }

    public boolean idExists(String id){
        return userRepository.existsById(id);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserModel findByID(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("User not found with id: " + id));
    }

    public UserModel findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    public List<UserModel> findAll (){
        return userRepository.findAll();
    }

    public UserModel save(UserModel userModel){
        return userRepository.save(userModel);
    }

//    public UserModel saveComment(UserModel user, List<MediaModel> mediaList, CommentModel comment){
//        user.getCreatedComments().add(comment);
//        user.getUploadedMedia().addAll(mediaList);
//        return save(user);
//    }

    public UserModel update(UserDTO updatedUserDTO, String username){

        UserModel user = findByUsername(username);

        if (usernameExists(updatedUserDTO.getUsername())) {
            throw new EntityExistsException("Username already exists: " + updatedUserDTO.getUsername());
        }
        if (emailExists(updatedUserDTO.getEmail())) {
            throw new EntityExistsException("Email already exists: " + updatedUserDTO.getEmail());
        }

        for (EventModel attending : user.getAttendingEvents()) {
            attending.getAttendingUsers().remove(user);
            eventRepository.save(attending);
        }

        if (!updatedUserDTO.getUsername().isEmpty()) {
            user.setUsername(updatedUserDTO.getUsername());
        }
        if (!updatedUserDTO.getEmail().isEmpty()) {
            user.setEmail(updatedUserDTO.getEmail());
        }

        updateUserRelated(user);

        return userRepository.save(user);
    }

    private void updateUserRelated(UserModel user) {
        for (CommentModel comment : user.getCreatedComments()) {
            comment.setAuthor(user);
            commentRepository.save(comment);
        }

        for (EventModel created : user.getCreatedEvents()) {
            created.setCreator(user);
            eventRepository.save(created);
        }

        for (MediaModel media : user.getUploadedMedia()) {
            media.setUploader(user);
            mediaRepository.save(media);
        }

        for (EventModel attending : user.getAttendingEvents()) {
            attending.getAttendingUsers().add(user);
            eventRepository.save(attending);
        }
    }

    public void addCreatedEvent(UserModel user, EventModel event) {
        user.getCreatedEvents().add(event);
        save(user);
    }

    public void addCreatedComment(UserModel user, CommentModel comment) {
        user.getCreatedComments().add(comment);
        save(user);
    }

    public ResponseEntity<?> changePassword (ResetPasswordDTO resetPasswordDTO){
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Get current user's username

        // Retrieve the current user from the database
        UserModel currentUser = findByUsername(currentUsername);

        // Check if the old password matches
        if (!passwordEncoder.matches(resetPasswordDTO.getOldPassword(), currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect.");
        }

        // Encode the new password and update the user record
        currentUser.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        save(currentUser);

        return ResponseEntity.ok("Password updated successfully.");
    }

    public ResponseEntity<?> delete(String userID, String username){
        UserModel user = findByID(userID);
        if (!user.getUsername().equals(username) &&
                !findByUsername(username).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to delete this user.");
        }
        for (EventModel event : user.getCreatedEvents()) {
            event.setCreator(null);
            eventRepository.save(event);
        }
        userRepository.delete(user);
        return ResponseEntity.ok("User was successfully deleted.");
    }

}
