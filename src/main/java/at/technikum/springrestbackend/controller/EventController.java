package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.EventDTO;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.security.SecurityUtil;
import at.technikum.springrestbackend.services.EventServices;
import at.technikum.springrestbackend.services.FileService;
import at.technikum.springrestbackend.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@CrossOrigin
public class EventController {

    private final EventMapper eventMapper;
    private final EventServices eventServices;
    private final FileService fileService;
    private final UserServices userServices;

    @Autowired
    public EventController(EventMapper eventMapper, EventServices eventServices, FileService fileService, UserServices userServices) {
        this.eventMapper = eventMapper;
        this.eventServices = eventServices;
        this.fileService = fileService;
        this.userServices = userServices;
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDTO read(@PathVariable UUID eventId) {
        return eventServices.getEventDetails(eventId);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO create(@RequestPart("eventData") @Valid EventDTO eventDTO,
                           @RequestPart("files") List<MultipartFile> files) {
        UserModel creator = userServices.find(eventDTO.getCreator().getUserID());
        EventModel event = eventMapper.toEntity(eventDTO, creator);
        String username = SecurityUtil.getCurrentUserName();
        List<MediaModel> mediaList = fileService.handleCreateEventUpload(files, event, username);
        event.getGalleryPictures().addAll(mediaList);
        eventServices.save(event);
        return eventMapper.toDTO(event);
    }

    @PutMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDTO update(@PathVariable UUID eventId,
                           @RequestPart("eventData") @Valid EventDTO updatedEventDTO,
                           @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        String username = SecurityUtil.getCurrentUserName();
        return eventMapper.toDTO(eventServices.update(eventId, updatedEventDTO, files, username));
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDTO delete(@PathVariable UUID eventId) {
        String username = SecurityUtil.getCurrentUserName();
        return eventMapper.toDTO(eventServices.deleteFinal(eventId, username));
    }

    @PutMapping("/{eventId}/addUser/{userID}")
    @ResponseStatus(HttpStatus.OK)
    public EventDTO addUserToEvent(@PathVariable UUID eventId, @PathVariable UUID userID) {
        EventModel updatedEvent = eventServices.addUserToEvent(eventId, userID);
        return eventMapper.toDTO(updatedEvent);
    }

    @PutMapping("/{eventId}/removeUser/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDTO removeUserFromEvent(@PathVariable UUID eventId, @PathVariable UUID userId) {
        String username = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.removeUserFromEvent(eventId, userId, username);
        return eventMapper.toDTO(updatedEvent);
    }

    @PutMapping("/{eventId}/leave")
    @ResponseStatus(HttpStatus.OK)
    public EventDTO leaveEvent(@PathVariable UUID eventId) {
        String username = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.leaveEvent(eventId, username);
        return eventMapper.toDTO(updatedEvent);
    }

    @PutMapping("/{eventId}/upload")
    @ResponseStatus(HttpStatus.OK)
    public EventDTO uploadGalleryPictures(@PathVariable UUID eventId,
                                          @RequestParam("files") List<MultipartFile> files) {
        EventModel event = eventServices.find(eventId);
        String username = SecurityUtil.getCurrentUserName();
        List<MediaModel> mediaList = fileService.uploadMediaToEvent(files, eventId, username);
        event.getGalleryPictures().addAll(mediaList);
        eventServices.save(event);
        return eventMapper.toDTO(event);
    }

    @DeleteMapping("/{eventId}/gallery/{mediaId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePictureFromGallery(@PathVariable UUID eventId,
                                                           @PathVariable UUID mediaId) {
        String currentUserName = SecurityUtil.getCurrentUserName();
        boolean isDeleted = eventServices.deletePictureFromGallery(eventId, mediaId, currentUserName);
        return isDeleted
                ? ResponseEntity.ok("Picture deleted successfully.")
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to delete this picture.");
    }
}