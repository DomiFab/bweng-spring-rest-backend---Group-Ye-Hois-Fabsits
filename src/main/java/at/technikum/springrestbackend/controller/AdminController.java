package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.EventDTO;
import at.technikum.springrestbackend.dto.ForumPostDTO;
import at.technikum.springrestbackend.dto.ForumThreadDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.mapper.ForumPostMapper;
import at.technikum.springrestbackend.mapper.ForumThreadMapper;
import at.technikum.springrestbackend.mapper.UserMapper;
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
    private final ForumPostServices postServices;
    private final ForumPostMapper postMapper;
    private final ForumThreadServices commentServices;
    private final ForumThreadMapper commentMapper;
    private final UserServices userServices;
    private final UserMapper userMapper;

    @Autowired
    public AdminController(
            AdminService adminService,
            EventServices eventServices,
            EventMapper eventMapper,
            ForumPostServices postServices,
            ForumPostMapper postMapper,
            ForumThreadServices commentServices,
            ForumThreadMapper commentMapper,
            UserServices userServices,
            UserMapper userMapper) {
        this.adminService = adminService;
        this.eventServices = eventServices;
        this.eventMapper = eventMapper;
        this.postServices = postServices;
        this.postMapper = postMapper;
        this.commentServices = commentServices;
        this.commentMapper = commentMapper;
        this.userServices = userServices;
        this.userMapper = userMapper;
    }


    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventDTO> readAllEvents() {
        return eventServices.findAll().stream()
                .map(eventMapper::toFullDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/forumposts")
    @ResponseStatus(HttpStatus.OK)
    public List<ForumPostDTO> readAllPosts() {
        return postServices.findAll().stream()
                .map(postMapper::toFullDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/forumthreads")
    @ResponseStatus(HttpStatus.OK)
    public List<ForumThreadDTO> readAllComments() {
        return commentServices.findAll().stream()
                .map(commentMapper::toFullDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> readAll() {
        return userServices.findAll().stream()
                .map(userMapper::toFullDTO)
                .collect(Collectors.toList());
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
