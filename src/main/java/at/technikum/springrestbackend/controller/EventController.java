package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.*;
import at.technikum.springrestbackend.mapper.CommentMapper;
import at.technikum.springrestbackend.mapper.EventMapper;
import at.technikum.springrestbackend.model.CommentModel;
import at.technikum.springrestbackend.model.EventModel;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.security.SecurityUtil;
import at.technikum.springrestbackend.services.CommentServices;
import at.technikum.springrestbackend.services.EventServices;
import at.technikum.springrestbackend.services.FileService;
import at.technikum.springrestbackend.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private CommentMapper commentMapper;
    @Autowired
    private CommentServices commentServices;

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
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public DisplayEventDTO createEvent (@RequestBody @Valid CreateEventDTO eventDTO) {

        String username = SecurityUtil.getCurrentUserName();
        UserModel creator = userServices.findByUsername(username);
        EventModel event = eventMapper.toEntity(eventDTO, creator);
        eventServices.createEvent(event);
        return eventMapper.toDisplayDTO(event, creator.getUserID());
    }

    @GetMapping("/{eventID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO readEvent (@PathVariable String eventID) {

        String username = SecurityUtil.getCurrentUserName();
        EventModel event = eventServices.find(eventID);
        return eventServices.eventDisplay(event, userServices.findByUsername(username));
    }

    @PutMapping("/{eventID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO updateEvent (@PathVariable String eventID,
                                   @RequestBody @Valid CreateEventDTO updatedEventDTO) {

        String username = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(username);
        EventModel updatedEvent = eventServices.update(eventID, updatedEventDTO, user);
        return eventMapper.toDisplayDTO(updatedEvent, user.getUserID());
    }

    @PutMapping("/{eventID}/status")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO updateStatus (@PathVariable String eventID,
                                         @RequestBody String status) {

        String username = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(username);
        EventModel event = eventServices.find(eventID);
        EventModel updatedEvent = eventServices.updateStatus(event, user, status);
        return eventMapper.toDisplayDTO(updatedEvent, user.getUserID());
    }

    @PutMapping(value = "/{eventID}/banner", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO updateEventBanner (@PathVariable String eventID,
                                         @RequestPart("file") MultipartFile file) {

        String username = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.find(eventID);
        fileService.updateFrontPicture(file, updatedEvent);
        return eventMapper.toDisplayDTO(updatedEvent, userServices.findByUsername(username).getUserID());
    }

    @DeleteMapping("/{eventID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO deleteEvent (@PathVariable String eventID) {

        String username = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(username);
        return eventMapper.toDisplayDTO(eventServices.deleteFinal(eventID, user.getUserID()), user.getUserID());
    }

    //===========================
    // Join, Leave Event
    //===========================
    @GetMapping("/{eventID}/attendingCount")
    @ResponseStatus(HttpStatus.OK)
    public Integer getAttendingCount (@PathVariable String eventID) {

        EventModel event = eventServices.find(eventID);
        return eventServices.getAttendingCount(event);
    }

    @GetMapping("/{eventID}/attendees")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAttendees (@PathVariable String eventID) {

        EventModel event = eventServices.find(eventID);
        return userServices.getAttendees(event);
    }

    @PutMapping("/{eventID}/join")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO joinEvent (@PathVariable String eventID) {

        String username = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(username);
        EventModel updatedEvent = eventServices.joinEvent(eventID, user.getUserID());
        return eventMapper.toDisplayDTO(updatedEvent, user.getUserID());
    }

    @PutMapping("/{eventID}/leave/")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO leaveEvent (@PathVariable String eventID) {

        String userName = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(userName);
        EventModel updatedEvent = eventServices.leaveEvent(eventID, user.getUserID());
        return eventMapper.toDisplayDTO(updatedEvent, user.getUserID());
    }

    @PutMapping("/{eventID}/removeUser/{userID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayEventDTO removeUserFromEvent (@PathVariable String eventID, @PathVariable String userID) {

        String username = SecurityUtil.getCurrentUserName();
        EventModel updatedEvent = eventServices.removeUserFromEvent(eventID, userID);
        return eventMapper.toDisplayDTO(updatedEvent, userServices.findByUsername(username).getUserID());
    }

    //================================
    // Event Comment CRUD Operations:
    //================================
    @PostMapping(path = "/{eventID}/comments", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public DisplayCommentDTO createComment (@PathVariable String eventID,
                                     @RequestPart("commentData") @Valid CreateCommentDTO commentDTO,
                                     @RequestPart(value ="files",required = false) MultipartFile file) {

        String username = SecurityUtil.getCurrentUserName();
        EventModel event = eventServices.find(eventID);
        UserModel author = userServices.findByUsername(username);
        CommentModel comment = commentMapper.toEntity(commentDTO, event, author);
        commentServices.save(comment);
        fileService.updateCommentMedia(file, comment, event, author);
        return commentMapper.toDisplayDTO(comment);
    }

    @GetMapping("/{eventID}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<DisplayCommentDTO> readAllComments (@PathVariable String eventID) {

        EventModel event = eventServices.find(eventID);
        return eventServices.getCommentsFromEvent(event);
    }

    @GetMapping("/{eventID}/commentCount")
    @ResponseStatus(HttpStatus.OK)
    public Integer getCommentCount (@PathVariable String eventID) {

        EventModel event = eventServices.find(eventID);
        return eventServices.getCommentCount(event);
    }

    @PutMapping("/{eventID}/comments/{commentID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayCommentDTO updateComment (@PathVariable String eventID,
                                            @PathVariable String commentID,
                                            @RequestBody @Valid CreateCommentDTO commentDTO) {

        String username = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(username);
        CommentModel comment = commentServices.update(eventID, commentID, commentDTO, user.getUserID());
        return commentMapper.toDisplayDTO(comment);
    }

    @DeleteMapping("/{eventID}/comments/{commentID}")
    @ResponseStatus(HttpStatus.OK)
    public DisplayCommentDTO deleteComment (@PathVariable String eventID,
                                          @PathVariable String commentID) {

        String username = SecurityUtil.getCurrentUserName();
        UserModel user = userServices.findByUsername(username);
        return commentMapper.toDisplayDTO(commentServices.delete(eventID, commentID, user.getUserID()));
    }

    //=========================
    // Event Media Operations:
    //=========================

    @GetMapping("/{eventID}/media")
    @ResponseStatus(HttpStatus.OK)
    public List<MediaDTO> readAllFiles (@PathVariable String eventID) {

        EventModel event = eventServices.find(eventID);
        return eventServices.getMediaFromEvent(event);
    }
}
