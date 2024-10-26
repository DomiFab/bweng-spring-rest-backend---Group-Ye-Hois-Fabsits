package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.CreateEventDTO;
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

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.FOUND)
    public CreateEventDTO read(@PathVariable String eventId) {
        return eventServices.getEventDetails(eventId);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public CreateEventDTO create(@RequestPart("eventData") @Valid CreateEventDTO eventDTO,
                                 @RequestPart("files") List<MultipartFile> files) {
        UserModel creator = userServices.find(eventDTO.getCreatorID());
        EventModel event = eventMapper.toEntity(eventDTO, creator);
//        String username = SecurityUtil.getCurrentUserName();
//        List<MediaModel> mediaList = fileService.handleCreateEventUpload(files, event, username);
//        event.getGalleryPictures().addAll(mediaList);
        eventServices.save(event);
        return eventMapper.toSimpleDTO(event);
    }

    @PutMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventDTO update(
            @PathVariable String eventId,
            @RequestPart("eventData") @Valid CreateEventDTO updatedEventDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        String username = SecurityUtil.getCurrentUserName();
        return eventMapper.toFullDTO(
                eventServices.update(eventId, updatedEventDTO, files, username));
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventDTO delete(@PathVariable String eventId){
        String username = SecurityUtil.getCurrentUserName();
        return eventMapper.toFullDTO(eventServices.deleteFinal(eventId, username));
    }

    @PutMapping("/{eventId}/addUser/{userID}")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventDTO addUserToEvent(@PathVariable String eventId, @PathVariable String userID) {
        EventModel updatedEvent = eventServices.joinEvent(eventId, userID);
        return eventMapper.toFullDTO(updatedEvent);
    }

    @PutMapping("/{eventId}/removeUser/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventDTO removeUserFromEvent(@PathVariable String eventId, @PathVariable String userId) {
        String userName = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.removeUserFromEvent(eventId, userId, userName);
        return eventMapper.toFullDTO(updatedEvent);
    }

    @PutMapping("/{eventId}/removeUser/")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventDTO leaveEvent(@PathVariable String eventId) {
        String userName = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.leaveEvent(eventId, userName);
        return eventMapper.toFullDTO(updatedEvent);
    }

    @PutMapping("/{eventId}/upload")
    @ResponseStatus(HttpStatus.OK)
    public CreateEventDTO uploadGalleryPictures(@PathVariable String eventId,
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
    public ResponseEntity<String> deletePictureFromGallery(@PathVariable String eventId,
                                                           @PathVariable String mediaId) {
//        String currentUserName = SecurityUtil.getCurrentUserName();
//        boolean isDeleted = eventServices.deletePictureFromGallery(eventId, mediaId, currentUserName);
        boolean isDeleted = true;
        return isDeleted
                ? ResponseEntity.ok("Picture deleted successfully.")
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to delete this picture.");
    }

}
