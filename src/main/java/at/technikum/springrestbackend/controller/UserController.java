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

import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserMapper userMapper;
    private final UserServices userServices;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Autowired
    public UserController(UserMapper userMapper, UserServices userServices, UserRepository userRepository, FileService fileService) {
        this.userMapper = userMapper;
        this.userServices = userServices;
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.FOUND)
    public UserDTO read(@PathVariable UUID userId) {
        UserModel user = userServices.find(userId);
        return userMapper.toFullDTO(user);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@PathVariable UUID userId, @RequestBody UserDTO updatedUserDTO) {
        String username = SecurityUtil.getCurrentUserName();
        return userMapper.toFullDTO(userServices.update(userId, updatedUserDTO, username));
    }

    @PutMapping(value = "/{userId}/media", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public UserDTO uploadProfilePicture(@PathVariable UUID userId, @RequestPart("file") MultipartFile file) {
        String authUser = SecurityUtil.getCurrentUserName();
        fileService.uploadProfilePicture(userId, file, authUser);
        UserModel updatedUser = userServices.find(userId);
        return userMapper.toFullDTO(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO delete(@PathVariable UUID userId) {
        String username = SecurityUtil.getCurrentUserName();
        UserModel deletedUser = userServices.delete(userId, username);
        return userMapper.toFullDTO(deletedUser);
    }
}