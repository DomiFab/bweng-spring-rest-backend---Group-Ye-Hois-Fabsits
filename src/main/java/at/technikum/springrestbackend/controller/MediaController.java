package at.technikum.springrestbackend.controller;

import at.technikum.springrestbackend.dto.MediaDTO;
import at.technikum.springrestbackend.mapper.MediaMapper;
import at.technikum.springrestbackend.model.MediaModel;
import at.technikum.springrestbackend.security.SecurityUtil;
import at.technikum.springrestbackend.services.FileService;
import at.technikum.springrestbackend.services.MediaServices;
import at.technikum.springrestbackend.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/media")
@CrossOrigin
public class MediaController {

    private final MediaServices mediaServices;
    private final FileService fileService;
    private final MediaMapper mediaMapper;
    private final UserServices userServices;

    @Autowired
    public MediaController(MediaServices mediaServices, FileService fileService, MediaMapper mediaMapper, UserServices userServices) {
        this.mediaServices = mediaServices;
        this.fileService = fileService;
        this.mediaMapper = mediaMapper;
        this.userServices = userServices;
    }

    @PostMapping("/upload/profile/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MediaDTO uploadProfilePicture(@PathVariable UUID userId, @RequestPart("file") MultipartFile file) {
        String authUser = SecurityUtil.getCurrentUserName();
        String filePath = fileService.uploadProfilePicture(userId, file, authUser);
        MediaModel media = new MediaModel(filePath, userServices.find(userId));
        mediaServices.save(media);
        return mediaMapper.toSimpleDTO(media);
    }

    @PostMapping("/upload/event/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<MediaDTO> uploadEventMedia(@PathVariable UUID eventId, @RequestPart("files") List<MultipartFile> files) {
        String username = SecurityUtil.getCurrentUserName();
        List<MediaModel> mediaList = fileService.uploadMediaToEvent(files, eventId, username);
        return mediaList.stream().map(mediaMapper::toSimpleDTO).toList();
    }

    @GetMapping("/{mediaId}")
    @ResponseStatus(HttpStatus.OK)
    public MediaDTO getMedia(@PathVariable UUID mediaId) {
        MediaModel media = mediaServices.find(mediaId);
        return mediaMapper.toSimpleDTO(media);
    }

    @DeleteMapping("/{mediaId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteMedia(@PathVariable UUID mediaId) {
        String username = SecurityUtil.getCurrentUserName();
        MediaModel media = mediaServices.find(mediaId);
        if (!media.getUploader().getUsername().equals(username) && !userServices.findByUsername(username).isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to delete this media.");
        }
        fileService.deleteFile(media.getFileLocation());
        mediaServices.delete(media);
        return ResponseEntity.ok("Media deleted successfully.");
    }
}