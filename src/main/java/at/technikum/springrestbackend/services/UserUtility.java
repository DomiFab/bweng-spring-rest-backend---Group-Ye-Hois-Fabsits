package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.model.UserModel;
import org.springframework.stereotype.Service;

@Service
public class UserUtility {

    public UserDTO toSimpleDTO(UserModel userModel) {
        return new UserDTO(
                userModel.getUserID(), userModel.getUsername(),
                userModel.getEmail(), userModel.getProfilePicture()
        );
    }
}