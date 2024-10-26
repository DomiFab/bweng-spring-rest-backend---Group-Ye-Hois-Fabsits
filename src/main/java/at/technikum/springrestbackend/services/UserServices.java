package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.exception.EntityNotFoundException;
import at.technikum.springrestbackend.mapper.UserMapper;
import at.technikum.springrestbackend.model.CommentModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServices(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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

    public UserModel find(String id) {
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

    public UserModel saveComment(UserModel user, List<MediaModel> mediaList, CommentModel comment){
        user.getCreatedComments().add(comment);
        user.getUploadedMedia().addAll(mediaList);
        return save(user);
    }

    public UserModel update(String userID, UserDTO updatedUserDTO, String username){

        //catching the case when an entity with the id does not exist
        if (!idExists(userID)){
            throw new EntityNotFoundException("User with provided ID [" + userID + "] not found.");
        }

        UserModel user = find(userID);

//        if (!user.getUsername().equals(username) &&
//                !findByUsername(username).isAdmin()) {
//            throw new AccessDeniedException("You do not have permission to update this user.");
//        }
//
//        user.setProfileDescription(updatedUserDTO.getProfileDescription());
//        if (usernameExists(updatedUserDTO.getUsername())) {
//            throw new EntityExistsException("Username already exists: " + updatedUserDTO.getUsername());
//        }
//        if (emailExists(updatedUserDTO.getEmail())) {
//            throw new EntityExistsException("Email already exists: " + updatedUserDTO.getEmail());
//        }
//        user.setUsername(updatedUserDTO.getUsername());
//        user.setEmail(updatedUserDTO.getEmail());

        return userRepository.save(user);
    }

    public UserModel delete(String userID, String username){
        UserModel user = find(userID);
        if (!user.getUsername().equals(username) &&
                !findByUsername(username).isAdmin()) {
            throw new AccessDeniedException("You do not have permission to update this user.");
        }
        userRepository.delete(user);
        return user;
    }

}
