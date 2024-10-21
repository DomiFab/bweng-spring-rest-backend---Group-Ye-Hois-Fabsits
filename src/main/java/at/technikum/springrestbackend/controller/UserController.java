package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.mapper.UserMapper;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.UserRepository;
import at.technikum.springrestbackend.security.SecurityUtil;
import at.technikum.springrestbackend.services.FileService;
import at.technikum.springrestbackend.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserMapper userMapper;
    private final UserServices userServices;
    private final UserRepository userRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    public UserController(
                        UserMapper userMapper,
                        UserServices userServices,
                        UserRepository userRepository) {

        this.userMapper = userMapper;
        this.userServices = userServices;
        this.userRepository = userRepository;
    }

  
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.FOUND)
    public UserDTO read(@PathVariable String userId) {
        UserModel user = userServices.find(userId);
        return userMapper.toFullDTO(user);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@PathVariable String userId, @RequestBody UserDTO updatedUserDTO){

        String username = SecurityUtil.getCurrentUserName();
        return userMapper.toFullDTO(userServices.update(userId, updatedUserDTO, username));
    }

    @PutMapping(value = "/{userID}/media", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public UserDTO uploadProfilePicture(@PathVariable String userID,
                                        @RequestPart("file")MultipartFile file){

        String authUser = SecurityUtil.getCurrentUserName();
        fileService.uploadProfilePicture(userID, file, authUser);
        UserModel updatedUser = userServices.find(userID);
        return userMapper.toFullDTO(updatedUser);
    }


    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.FOUND)
    public UserDTO delete(@PathVariable String userId){

        String username = SecurityUtil.getCurrentUserName();
        UserModel deletedUser = userServices.delete(userId, username);
        return userMapper.toFullDTO(deletedUser);
    }

}


