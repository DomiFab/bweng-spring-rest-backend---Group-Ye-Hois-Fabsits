package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.CreateEventDTO;
import at.technikum.springrestbackend.dto.CreateCommentDTO;
import at.technikum.springrestbackend.dto.FullUserDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.mapper.CommentMapper;
import at.technikum.springrestbackend.mapper.UserMapper;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    private final AdminService adminService;
    private final EventServices eventServices;
    private final EventMapper eventMapper;
    private final CommentServices postServices;
    private final CommentMapper postMapper;
    private final UserServices userServices;
    private final UserMapper userMapper;

    @Autowired
    public AdminController(
            AdminService adminService,
            EventServices eventServices,
            EventMapper eventMapper,
            CommentServices postServices,
            CommentMapper postMapper,
            UserServices userServices,
            UserMapper userMapper) {
        this.adminService = adminService;
        this.eventServices = eventServices;
        this.eventMapper = eventMapper;
        this.postServices = postServices;
        this.postMapper = postMapper;
        this.userServices = userServices;
        this.userMapper = userMapper;
    }


    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<CreateEventDTO> readAllEvents() {
        return eventServices.findAll().stream()
                .map(eventMapper::toFullDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CreateCommentDTO> readAllPosts() {
        return postServices.findAll().stream()
                .map(postMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> readAll() {
        return userServices.findAll().stream()
                .map(userMapper::toSimpleDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{userID}")
    @ResponseStatus(HttpStatus.OK)
    public FullUserDTO readFullUser(@PathVariable String userID) {

        UserModel user = userServices.findByID(userID);
        FullUserDTO userDTO = userMapper.toFullDTO(user);
        userDTO.setCreatedEvents(adminService.getCreatedEventsByUser(user));
        userDTO.setAttendingEvents(adminService.getAttendingEvents(user));
        return userDTO;
    }

    @PutMapping("/users/{userID}")
    @ResponseStatus(HttpStatus.OK)
    public FullUserDTO setUserAdmin(@PathVariable String userID,
                                    @RequestBody boolean isAdmin) {

        UserModel user = userServices.findByID(userID);
        return userMapper.toFullDTO(adminService.setUserAdmin(user, isAdmin));
    }

    @DeleteMapping("/user/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        return adminService.deleteUser(userId);
    }

    @PutMapping("/user/restore/{userId}")
    public String restoreUser(@PathVariable Long userId) {
        return adminService.restoreUser(userId);
    }

    @DeleteMapping("/event/{eventId}")
    public String deleteEvent(@PathVariable Long eventId) {
        return adminService.deleteEvent(eventId);
    }

    @PutMapping("/event/restore/{eventId}")
    public String restoreEvent(@PathVariable Long eventId) {
        return adminService.restoreEvent(eventId);
    }
}
