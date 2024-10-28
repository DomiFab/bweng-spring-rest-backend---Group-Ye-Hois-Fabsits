package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.CreateEventDTO;
import at.technikum.springrestbackend.dto.DisplayEventDTO;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.model.EventModel;
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

@RestController
@RequestMapping("/events")
@CrossOrigin
public class EventController {

    private final EventMapper eventMapper;
    private final EventServices eventServices;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserServices userServices;

    @Autowired
    public EventController(
            EventMapper eventMapper,
            EventServices eventServices) {

        this.eventMapper = eventMapper;
        this.eventServices = eventServices;
    }

    //========================
    // Event CRUD Operations:
    //========================
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public DisplayEventDTO create (@RequestPart("eventData") @Valid CreateEventDTO eventDTO,
                                   @RequestPart("file") MultipartFile file) {

        String username = SecurityUtil.getCurrentUserName();
        UserModel creator = userServices.findByUsername(username);
        EventModel event = eventMapper.toEntity(eventDTO, creator);
        fileService.updateFrontPicture(file, event);
        return eventMapper.toDisplayDTO(event, username);
    }

    @GetMapping("/{eventID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO read (@PathVariable String eventID) {

        String username = SecurityUtil.getCurrentUserName();
        EventModel event = eventServices.find(eventID);
        return eventMapper.toDisplayDTO(event, username);
    }

    @PutMapping("/{eventID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO update (@PathVariable String eventID,
                                   @Valid CreateEventDTO updatedEventDTO) {

        String username = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.update(eventID, updatedEventDTO, username);
        return eventMapper.toDisplayDTO(updatedEvent, username);
    }

    @PutMapping("/{eventID}/cover")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO updateBanner (@PathVariable String eventID,
                                         @RequestPart("file") MultipartFile file) {

        String username = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.find(eventID);
        fileService.updateFrontPicture(file, updatedEvent);
        return eventMapper.toDisplayDTO(updatedEvent, username);
    }

    @DeleteMapping("/{eventID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO delete (@PathVariable String eventID) {

        String username = SecurityUtil.getCurrentUserName();
        return eventMapper.toDisplayDTO(eventServices.deleteFinal(eventID, username), username);
    }

    //===========================
    // Join, Leave Event
    //===========================
    @PutMapping("/{eventId}/join/{userID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO joinEvent (@PathVariable String eventId, @PathVariable String userID) {

        String username = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.joinEvent(eventId, userID);
        return eventMapper.toDisplayDTO(updatedEvent, username);
    }

    @PutMapping("/{eventId}/leave/")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventDTO leaveEvent (@PathVariable String eventId) {
        String userName = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.leaveEvent(eventId, userName);
        return eventMapper.toFullDTO(updatedEvent);
    }

    @PutMapping("/{eventId}/removeUser/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO removeUserFromEvent (@PathVariable String eventId, @PathVariable String userId) {

        String username = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.removeUserFromEvent(eventId, userId, username);
        return eventMapper.toDisplayDTO(updatedEvent, username);
    }

    //================================
    // Event Comment CRUD Operations:
    //================================

    //=========================
    // Event Media Operations:
    //=========================

    @PutMapping("/{eventId}/upload")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventDTO uploadGalleryPictures (@PathVariable String eventId,
                                                 @RequestParam("files") List<MultipartFile> files) {
        EventModel event = eventServices.find(eventId);
//        String userName = SecurityUtil.getCurrentUserName();
//        List<MediaModel> mediaList = fileService.uploadMediaToEvent(files, eventId, userName);
//        event.getGalleryPictures().addAll(mediaList);
        eventServices.save(event);
        return eventMapper.toFullDTO(event);
    }

    @DeleteMapping("/{eventId}/gallery/{mediaId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePictureFromGallery (@PathVariable String eventId,
                                                            @PathVariable String mediaId) {
//        String currentUserName = SecurityUtil.getCurrentUserName();
//        boolean isDeleted = eventServices.deletePictureFromGallery(eventId, mediaId, currentUserName);
        boolean isDeleted = true;
        return isDeleted
                ? ResponseEntity.ok("Picture deleted successfully.")
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to delete this picture.");
    }

}
