package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "Admin API für das Löschen und Wiederherstellen von Benutzern und Events")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "Löscht einen Benutzer")
    @DeleteMapping("/user/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return adminService.deleteUser(userId);
    }

    @Operation(summary = "Stellt einen gelöschten Benutzer wieder her")
    @PutMapping("/user/restore/{userId}")
    public String restoreUser(@PathVariable String userId) {
        return adminService.restoreUser(userId);
    }

    @Operation(summary = "Löscht ein Event")
    @DeleteMapping("/event/{eventId}")
    public String deleteEvent(@PathVariable String eventId) {
        return adminService.deleteEvent(eventId);
    }

    @Operation(summary = "Stellt ein gelöschtes Event wieder her")
    @PutMapping("/event/restore/{eventId}")
    public String restoreEvent(@PathVariable String eventId) {
        return adminService.restoreEvent(eventId);
    }
}
