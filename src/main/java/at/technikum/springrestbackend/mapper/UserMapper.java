package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.services.EventUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final MediaMapper mediaMapper;
    private final EventUtility eventUtility;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder, MediaMapper mediaMapper, EventUtility eventUtility) {
        this.passwordEncoder = passwordEncoder;
        this.mediaMapper = mediaMapper;
        this.eventUtility = eventUtility;
    }

    public UserDTO toSimpleDTO(UserModel userModel) {
        return new UserDTO(
                userModel.getUserID(),
                userModel.getUsername(),
                userModel.getEmail(),
                userModel.getProfilePicture()
        );
    }

    public UserDTO toFullDTO(UserModel user) {
        UserDTO displayedUser = new UserDTO();
        for (EventModel event : user.getAttendingEvents()) {
            displayedUser.getAttendingEvents().add(eventUtility.convertToDTO(event));
        }
        for (EventModel event : user.getCreatedEvents()) {
            displayedUser.getCreatedEvents().add(eventUtility.convertToDTO(event));
        }
        for (MediaModel media : user.getUploadedMedia()) {
            displayedUser.getUploadedMedia().add(mediaMapper.toSimpleDTO(media));
        }
        return new UserDTO(
                user.getUserID(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileDescription(),
                user.getProfilePicture(),
                displayedUser.getAttendingEvents(),
                displayedUser.getCreatedEvents(),
                displayedUser.getUploadedMedia()
        );
    }

    public UserModel toEntity(UserDTO userDTO) {
        UserModel userModel = new UserModel();
        userModel.setUsername(userDTO.getUsername());
        userModel.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userModel.setEmail(userDTO.getEmail());
        userModel.setProfileDescription(userDTO.getProfileDescription());
        userModel.setProfilePicture(userDTO.getProfilePicture());
        return userModel;
    }
}