package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.MediaDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.services.EventUtility;
import at.technikum.springrestbackend.services.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserUtility userUtility;
    @Autowired
    private EventUtility eventUtility;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public UserDTO toSimpleDTO(UserModel userModel) {

        return new UserDTO(
                userModel.getUserID(), userModel.getUsername(),
                userModel.getEmail(), userModel.getProfilePicture()
        );
    }

    public UserDTO toFullDTO(UserModel user) {
        UserDTO displayedUser = new UserDTO();
        for (EventModel event : user.getAttendingEvents()){
            displayedUser.getAttendingEvents().add(eventUtility.convertToDTO(event));
        }
        for (EventModel event : user.getCreatedEvents()){
            displayedUser.getCreatedEvents().add(eventUtility.convertToDTO(event));
        }
        for (MediaModel media : user.getUploadedMedia()){
            displayedUser.getUploadedMedia().add(new MediaDTO(media.getMediaID(), media.getFileLocation(),
                    media.getEvent().getEventID(),
                    userUtility.toSimpleDTO(media.getUploader()),
                    media.isFrontPic()
                    )
            );
        }
        return new UserDTO(user.getUserID(), user.getUsername(),
                user.getEmail(),user.getProfileDescription(),
                user.getProfilePicture(), displayedUser.getAttendingEvents(),
                displayedUser.getCreatedEvents(), displayedUser.getUploadedMedia()
        );
    }

    public UserModel toEntity(UserDTO userDTO) {
        //DataBank entry requires the id as a primary key
        return new UserModel(
                    UUID.randomUUID().toString(),
                    userDTO.getUsername(),
                    passwordEncoder.encode(userDTO.getPassword()),
                    userDTO.getEmail()
        );

    }
}
