package at.technikum.springrestbackend.mapper;

import at.technikum.springrestbackend.dto.FullUserDTO;
import at.technikum.springrestbackend.dto.RegisterDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

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

    public UserModel toEntity(RegisterDTO registerDTO) {
        //DataBank entry requires the id as a primary key
        return new UserModel(
                UUID.randomUUID().toString(),
                registerDTO.getUsername(),
                passwordEncoder.encode(registerDTO.getPassword()),
                registerDTO.getEmail()
        );
    }

    public FullUserDTO toFullDTO(UserModel user) {

        FullUserDTO displayedUser = new FullUserDTO(user.isAdmin());

        displayedUser.setUserID(user.getUserID());
        displayedUser.setEmail(user.getEmail());
        displayedUser.setUsername(user.getUsername());
        displayedUser.setProfilePicture(user.getProfilePicture());

        return displayedUser;
    }

}
