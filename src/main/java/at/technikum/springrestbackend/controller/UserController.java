package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.DisplayEventDTO;
import at.technikum.springrestbackend.dto.MediaDTO;
import at.technikum.springrestbackend.dto.ResetPasswordDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.mapper.UserMapper;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.security.SecurityUtil;
import at.technikum.springrestbackend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserMapper userMapper;
    private final UserServices userServices;
    private final FileService fileService;
    private final EventServices eventServices;
    private final MediaServices mediaServices;

    @Autowired
    public UserController(UserMapper userMapper, UserServices userServices,
                          FileService fileService, EventServices eventServices,
                          MediaServices mediaServices) {
        this.userMapper = userMapper;
        this.userServices = userServices;
        this.fileService = fileService;
        this.eventServices = eventServices;
        this.mediaServices = mediaServices;
    }

    //get UserDashboardDTO, without userID because username in jwt - on first Login
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserDTO read() {
        String username = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(username);
        return userMapper.toSimpleDTO(user);
    }

    //get specific UserDashboardDTO with username if needed
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO readByUsername(@PathVariable String username) {
        UserModel user = userServices.findByUsername(username);
        return userMapper.toSimpleDTO(user);
    }


    //update own user profile (username, email)
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@RequestBody UserDTO updatedUserDTO) {

        String jwtUsername = SecurityUtil.getCurrentUserName();
        return userMapper.toSimpleDTO(userServices.update(updatedUserDTO, jwtUsername));
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updatePassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {

        return userServices.changePassword(resetPasswordDTO);
    }

    @PutMapping(value = "/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public UserDTO uploadProfilePicture(@RequestPart(value = "file", required = false) MultipartFile file) {

        String authUser = SecurityUtil.getCurrentUserName();
        fileService.updateProfilePicture(file, userServices.findByUsername(authUser));
        return userMapper.toSimpleDTO(userServices.findByUsername(authUser));
    }

    @DeleteMapping("/{userID}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> delete(@PathVariable String userID) {

        String username = SecurityUtil.getCurrentUserName();
        return userServices.delete(userID, username);
    }

    //get attending and created events
    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<DisplayEventDTO> readEvents() {

        String username = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(username);
        return eventServices.getEventsFromUser(user);
    }

    //get list of uploaded media by user
    @GetMapping("/media")
    @ResponseStatus(HttpStatus.OK)
    public List<MediaDTO> readMedia() {

        String username = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(username);
        return mediaServices.getMediaFromUser(user);
    }
}


