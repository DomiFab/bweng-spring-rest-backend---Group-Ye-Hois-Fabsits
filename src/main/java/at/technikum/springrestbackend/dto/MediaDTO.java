package at.technikum.springrestbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class MediaDTO {
    private UUID mediaID;
    @NotBlank
    @Valid
    private String fileLocation;
    private UUID eventID;
    private boolean isFrontPic = false;
    @NotBlank
    @Valid
    private UserDTO uploader;
    private String post;
    private ForumThreadDTO comment;

    public MediaDTO() {
    }

    // Profile picture
    public MediaDTO(UUID mediaID, String fileLocation, UserDTO uploader) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.uploader = uploader;
    }

    // Gallery picture with isFrontPic tag
    public MediaDTO(UUID mediaID, String fileLocation, UUID eventID, UserDTO uploader, boolean isFrontPic) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.eventID = eventID;
        this.uploader = uploader;
        this.isFrontPic = isFrontPic;
    }

    // Post media
    public MediaDTO(UUID mediaID, String fileLocation, UUID eventID, UserDTO uploader, String post) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.eventID = eventID;
        this.uploader = uploader;
        this.post = post;
    }

    // Comment media
    public MediaDTO(UUID mediaID, String fileLocation, UUID eventID, UserDTO uploader, String post, ForumThreadDTO comment) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.eventID = eventID;
        this.uploader = uploader;
        this.post = post;
        this.comment = comment;
    }

    // Getters and Setters
    public UUID getMediaID() {
        return mediaID;
    }

    public void setMediaID(UUID mediaID) {
        this.mediaID = mediaID;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public UUID getEventID() {
        return eventID;
    }

    public void setEventID(UUID eventID) {
        this.eventID = eventID;
    }

    public UserDTO getUploader() {
        return uploader;
    }

    public void setUploader(UserDTO uploader) {
        this.uploader = uploader;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public ForumThreadDTO getComment() {
        return comment;
    }

    public void setComment(ForumThreadDTO comment) {
        this.comment = comment;
    }

    public boolean isFrontPic() {
        return isFrontPic;
    }

    public void setFrontPic(boolean frontPic) {
        isFrontPic = frontPic;
    }
}