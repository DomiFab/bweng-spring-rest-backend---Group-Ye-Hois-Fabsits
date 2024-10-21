package at.technikum.springrestbackend.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class MediaDTO {
    private String mediaID;
    @NotBlank
    @Valid
    private String fileLocation;
    private String eventID;
    private boolean isFrontPic = false;
    @NotBlank
    @Valid
    private UserDTO uploader;
    private String post;
    private ForumThreadDTO comment;

    public MediaDTO() {
    }

    //profile picture
    public MediaDTO(String mediaID, String fileLocation, UserDTO uploader) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.uploader = uploader;
    }

    //gallery picture with is FrontPic tag
    public MediaDTO(String mediaID, String fileLocation, String eventID, UserDTO uploader,
                    boolean isFrontPic) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.eventID = eventID;
        this.uploader = uploader;
        this.isFrontPic = isFrontPic;
    }

    //post media
    public MediaDTO(String mediaID, String fileLocation, String eventID, UserDTO uploader,
                    String post) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.eventID = eventID;
        this.uploader = uploader;
        this.post = post;
    }

    //comment media
    public MediaDTO(String mediaID, String fileLocation, String eventID, UserDTO uploader,
                    String post, ForumThreadDTO comment) {
        this.mediaID = mediaID;
        this.fileLocation = fileLocation;
        this.eventID = eventID;
        this.uploader = uploader;
        this.post = post;
        this.comment = comment;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
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
}
