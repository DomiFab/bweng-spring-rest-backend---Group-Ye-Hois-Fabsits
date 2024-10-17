package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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
