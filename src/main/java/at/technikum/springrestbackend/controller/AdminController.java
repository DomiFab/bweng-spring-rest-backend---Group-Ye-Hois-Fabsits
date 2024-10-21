package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.EventDTO;
import at.technikum.springrestbackend.dto.UserDTO;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.mapper.UserMapper;
import at.technikum.springrestbackend.services.AdminService;
import at.technikum.springrestbackend.services.EventServices;
import at.technikum.springrestbackend.services.UserServices;
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
    private final UserServices userServices;
    private final UserMapper userMapper;

    @Autowired
    public AdminController(
            AdminService adminService,
            EventServices eventServices,
            EventMapper eventMapper,
            UserServices userServices,
            UserMapper userMapper) {
        this.adminService = adminService;
        this.eventServices = eventServices;
        this.eventMapper = eventMapper;
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

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> readAllUsers() {
        return userServices.findAll().stream()
                .map(userMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteUser(@PathVariable Long userId, @RequestParam String reason) {
        return adminService.deleteUser(userId, reason);
    }

    @PutMapping("/user/restore/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String restoreUser(@PathVariable Long userId) {
        return adminService.restoreUser(userId);
    }

    @DeleteMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteEvent(@PathVariable Long eventId, @RequestParam String reason) {
        return adminService.deleteEvent(eventId, reason);
    }

    @PutMapping("/event/restore/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public String restoreEvent(@PathVariable Long eventId) {
        return adminService.restoreEvent(eventId);
    }
}